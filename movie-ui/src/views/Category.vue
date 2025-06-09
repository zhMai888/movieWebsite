<template>
  <div class="category-container">
    <Navigation />
    <div class="category-main-content">

      <div class="filter-layout">
        <!-- 筛选条件面板 -->
        <div class="filter-sidebar">
          <div class="filter-section">
            <h3>发行年份</h3>
            <div class="filter-options">
              <span v-for="year in yearOptions" :key="year.value"
                    class="filter-option"
                    :class="{ active: isYearActive(year.value) }"
                    @click="filterByYear(year.value)"
                    :title="year.label">
                {{ truncateText(year.label) }}
              </span>
            </div>
          </div>

          <div class="filter-section">
            <h3>电影风格</h3>
            <div class="filter-options">
              <span v-for="genre in genres" :key="genre.value"
                    class="filter-option"
                    :class="{ active: isGenreActive(genre.value) }"
                    @click="filterByGenre(genre.value)"
                    :title="genre.label">
                {{ truncateText(genre.label) }}
              </span>
            </div>
          </div>

          <div class="filter-section">
            <h3>电影地区</h3>
            <div class="filter-options">
              <span v-for="area in areas" :key="area.value"
                    class="filter-option"
                    :class="{ active: isAreaActive(area.value) }"
                    @click="filterByArea(area.value)"
                    :title="area.label">
                {{ truncateText(area.label) }}
              </span>
            </div>
          </div>
        </div>

        <!-- 筛选结果 -->
        <div class="filter-results">
          <div v-if="filteredMovies.length === 0 && !loading" class="empty-result">
            <p class="empty-text">😕好像没有这部电影哦！</p>
          </div>
          <MovieCard :movies="filteredMovies" :genres="genres" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from '@/components/Navigation.vue'
import MovieCard from '@/components/MovieCard.vue'
import { listMovies } from '@/api/movies/movies'
import { listGenres } from '@/api/genres/genres'
import { listAreas } from '@/api/areas/areas'
import {getGenres} from "@/api/genres/genres";

export default {
  name: 'Category',
  components: {
    Navigation,
    MovieCard
  },
  data() {
    return {
      filteredMovies: [],
      loading: true,
      categoryTitle: '全部电影',
      filterType: '',
      filterValue: '',
      genres: [],
      areas: [],
      genreCache: {},
      yearOptions: [
        { label: '全部', value: '' },
        { label: '2025', value: '2025' },
        { label: '2024', value: '2024' },
        { label: '2023', value: '2023' },
        { label: '2022', value: '2022' },
        { label: '2021', value: '2021' },
        { label: '2020', value: '2020' },
        { label: '2019', value: '2019' },
        { label: '2018', value: '2018' },
        { label: '2017', value: '2017' },
        { label: '2016', value: '2016' },
        { label: '2015-2010', value: '2010-2015' },
        { label: '2009-2005', value: '2005-2009' },
        { label: '2004-2000', value: '2000-2004' },
        { label: '90年代', value: '1990-1999' },
        { label: '更早', value: '-1989' }
      ]
    }
  },
  async created() {
    await this.fetchGenres();
    await this.fetchAreas();
    await this.fetchFilteredMovies();
  },
  watch: {
    '$route.query': {
      handler(newQuery) {
        this.filterType = newQuery.type || ''
        this.filterValue = newQuery.value || ''
        this.fetchFilteredMovies()
      },
      immediate: true
    }
  },
  methods: {
    truncateText(text) {
      if (!text) return ''; // 添加空值检查
      return text.length > 4 ? text.slice(0, 4) + '...' : text;
    },
    // 获取电影类型
    async fetchMovieType(genreId) {
      if (this.genreCache[genreId]) {
        return this.genreCache[genreId]
      }

      try {
        const res = await getGenres(genreId)
        const type = res.data?.type || 'Unknown'
        this.$set(this.genreCache, genreId, type)
        return type
      } catch (error) {
        console.error(`获取类型 ${genreId} 失败:`, error)
        return 'Unknown'
      }
    },
    async fetchGenres() {
      try {
        const res = await listGenres({ pageSize: 100 });
        this.genres = res['rows'] || [];

        // 转换数据结构，确保每个类型有type和genreId
        this.genres = this.genres.map(genre => ({
          label: genre.type,  // 显示文本：喜剧/动作等
          value: genre.id, // 实际值：类型ID
          ...genre
        }));
      } catch (error) {
        console.error('获取电影类型失败:', error);
      }
    },
    async fetchAreas() {
      try {
        const res = await listAreas({ pageSize: 100 });
        this.areas = res['rows'] || [];

        this.areas = this.areas.map(area => ({
            label: area.areaname,  // 显示文本：喜剧/动作等
            value: area.id, // 实际值：类型ID
            ...area
          }));
      } catch (error) {
        console.error('获取地区列表失败:', error);
      }
    },
    async fetchFilteredMovies() {
      this.loading = true;
      try {
        const params = {};

        // 处理搜索查询
        if (this.$route.query.search) {
          params.name = this.$route.query.search;
          this.categoryTitle = `搜索: ${this.$route.query.search}`;
        }
        else if (this.filterType === 'year') {
          if (this.filterValue) {
            if (this.filterValue.includes('-')) {
              // 处理年份范围
              const [start, end] = this.filterValue.split('-').map(Number);
              if (!isNaN(start) && !isNaN(end)) {
                params.releaseDateStart = start;
                params.releaseDateEnd = end;
              }
            } else if (this.filterValue.startsWith('-')) {
              // 处理早于某年
              const year = parseInt(this.filterValue.slice(1));
              if (!isNaN(year)) {
                params.releaseDateEnd = year;
              }
            } else {
              // 处理具体年份
              const year = parseInt(this.filterValue);
              if (!isNaN(year)) {
                params.releaseDateStart = year;
                params.releaseDateEnd = year;
              }
            }
          }
        } else if (this.filterType === 'genre') {
          params.genreId = this.filterValue;
        } else if (this.filterType === 'area') {
          params.areaId = this.filterValue;
        }

        const res = await listMovies({
          ...params,
          pageSize: 100,
          pageNum: 1
        });

        const moviesWithTypes = await Promise.all(
          (res['rows'] || []).map(async movie => {
            const type = await this.fetchMovieType(movie.genreId);
            return {
              ...movie,
              type: type
            };
          })
        );

        this.filteredMovies = moviesWithTypes;

      } catch (error) {
        console.error('获取筛选电影失败:', error);
      } finally {
        this.loading = false;
      }
    },
    filterByYear(year) {
      this.$router.push({
        query: {
          type: 'year',
          value: year
        }
      });
    },
    filterByGenre(genreId) {
      this.$router.push({
        query: {
          type: 'genre',
          value: genreId  // 使用genreId作为value
        }
      });
    },
    filterByArea(areaId) {
      this.$router.push({
        query: {
          type: 'area',
          value: areaId
        }
      });
    },
    isYearActive(year) {
      return this.filterType === 'year' && this.filterValue === year;
    },
    isGenreActive(genreId) {
      return this.filterType === 'genre' && this.filterValue === genreId;
    },
    isAreaActive(areaId) {
      return this.filterType === 'area' && this.filterValue === areaId;
    }
  }
}
</script>

<style scoped>
.category-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.category-main-content {
  flex: 1;
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.filter-layout {
  display: flex;
  gap: 30px;
}

/* 筛选面板现在在左侧 */
.filter-sidebar {
  order: -1; /* 将筛选面板移到左侧 */
  width: 250px;
  padding: 0; /* 去掉内边距 */
  background: none; /* 去掉背景 */
}

.filter-section {
  margin-bottom: 25px;
}

.filter-section h3 {
  margin-bottom: 15px;
  color: #333;
  font-size: 1.1rem;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.filter-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-option {
  padding: 6px 12px;
  background: #f0f0f0; /* 浅灰色背景 */
  border: none;
  border-radius: 20px; /* 胶囊形状 */
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
  min-width: 60px; /* 固定宽度 */
  max-width: 60px; /* 固定宽度 */
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.filter-option:hover {
  background: #a0a0a0; /* 深灰色悬停状态 */
  color: white;
}

.filter-option.active {
  background: #1890ff;
  color: white;
}

.filter-results {
  flex: 1;
  margin-top: 0;
}

/* 添加空状态样式 */
.empty-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  text-align: center;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .filter-layout {
    flex-direction: column;
  }

  .filter-sidebar {
    width: 100%;
    margin-bottom: 20px;
    order: 0; /* 在小屏幕上恢复默认顺序 */
  }
}

@media (max-width: 768px) {
  .category-main-content {
    padding: 15px;
  }

  .filter-option {
    padding: 4px 8px;
    font-size: 0.8rem;
    min-width: 50px;
    max-width: 50px;
  }
}
</style>
