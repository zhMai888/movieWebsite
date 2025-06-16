<template>
  <div class="category-container">
    <Navigation />
    <div class="category-main-content">
      <div class="filter-layout">
        <!-- 筛选条件面板 -->
        <div class="filter-sidebar">
          <!-- 年份筛选 -->
          <div class="filter-section">
            <h3>发行年份</h3>
            <div class="filter-options">
              <span v-for="year in yearOptions" :key="year.value"
                    class="filter-option"
                    :class="{ active: currentYear === year.value }"
                    @click="setYearFilter(year.value)"
                    :title="year.label">
                {{ truncateText(year.label) }}
              </span>
            </div>
          </div>

          <!-- 类型筛选 -->
          <div class="filter-section">
            <h3>电影风格</h3>
            <div class="filter-options">
              <span class="filter-option"
                    :class="{ active: currentGenre === '' }"
                    @click="setGenreFilter('')"
                    title="全部">
                全部
              </span>
              <span v-for="genre in genres" :key="genre.id"
                    class="filter-option"
                    :class="{ active: currentGenre === genre.id.toString() }"
                    @click="setGenreFilter(genre.id)"
                    :title="genre.type">
                {{ truncateText(genre.type) }}
              </span>
            </div>
          </div>

          <!-- 地区筛选 -->
          <div class="filter-section">
            <h3>电影地区</h3>
            <div class="filter-options">
              <span class="filter-option"
                    :class="{ active: currentArea === '' }"
                    @click="setAreaFilter('')"
                    title="全部">
                全部
              </span>
              <span v-for="area in areas" :key="area.id"
                    class="filter-option"
                    :class="{ active: currentArea === area.id.toString() }"
                    @click="setAreaFilter(area.id)"
                    :title="area.areaname">
                {{ truncateText(area.areaname) }}
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
import { getGenres } from "@/api/genres/genres"

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
  computed: {
    currentYear() {
      return this.$route.query.year || ''
    },
    currentGenre() {
      return this.$route.query.genre || ''
    },
    currentArea() {
      return this.$route.query.area || ''
    }
  },
  async created() {
    await this.fetchGenres()
    await this.fetchAreas()
    await this.applyFilters()
  },
  watch: {
    '$route.query': {
      handler() {
        this.applyFilters()
      },
      immediate: true
    }
  },
  methods: {
    truncateText(text) {
      if (!text) return ''
      return text.length > 4 ? text.slice(0, 4) + '...' : text
    },

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
        const res = await listGenres({ pageSize: 100 })
        this.genres = res['rows'] || []
      } catch (error) {
        console.error('获取电影类型失败:', error)
      }
    },

    async fetchAreas() {
      try {
        const res = await listAreas({ pageSize: 100 })
        this.areas = res['rows'] || []
      } catch (error) {
        console.error('获取地区列表失败:', error)
      }
    },

    setYearFilter(year) {
      this.updateRoute({
        year: year || undefined,
        genre: this.$route.query.genre || undefined,
        area: this.$route.query.area || undefined
      })
    },

    setGenreFilter(genreId) {
      this.updateRoute({
        year: this.$route.query.year || undefined,
        genre: genreId || undefined,
        area: this.$route.query.area || undefined
      })
    },

    setAreaFilter(areaId) {
      this.updateRoute({
        year: this.$route.query.year || undefined,
        genre: this.$route.query.genre || undefined,
        area: areaId || undefined
      })
    },

    updateRoute(query) {
      this.$router.push({
        query: {
          ...this.$route.query, // 保留现有参数
          ...query,             // 应用新参数
          search: this.$route.query.search || undefined
        }
      })
    },

    async applyFilters() {
      this.loading = true
      try {
        const params = {
          pageSize: 100,
          pageNum: 1
        }

        // 处理搜索查询
        if (this.$route.query.search) {
          params.name = this.$route.query.search
        }

        // 处理年份筛选
        if (this.currentYear) {
          if (this.currentYear.includes('-')) {
            const [start, end] = this.currentYear.split('-').map(Number)
            if (!isNaN(start) && !isNaN(end)) {
              params.releaseDateStart = start
              params.releaseDateEnd = end
            }
          } else if (this.currentYear.startsWith('-')) {
            const year = parseInt(this.currentYear.slice(1))
            if (!isNaN(year)) {
              params.releaseDateEnd = year
            }
          } else {
            const year = parseInt(this.currentYear)
            if (!isNaN(year)) {
              params.releaseDateStart = year
              params.releaseDateEnd = year
            }
          }
        }

        // 处理类型筛选
        if (this.currentGenre) {
          params.genreId = this.currentGenre
        }

        // 处理地区筛选
        if (this.currentArea) {
          params.areaId = this.currentArea
        }

        const res = await listMovies(params)
        const moviesWithTypes = await Promise.all(
          (res['rows'] || []).map(async movie => {
            const type = await this.fetchMovieType(movie.genreId)
            return {
              ...movie,
              type: type
            }
          })
        )

        this.filteredMovies = moviesWithTypes
      } catch (error) {
        console.error('获取筛选电影失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
/* 样式保持不变 */
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

.filter-sidebar {
  order: -1;
  width: 250px;
  padding: 0;
  background: none;
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
  background: #f0f0f0;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
  min-width: 60px;
  max-width: 60px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.filter-option:hover {
  background: #a0a0a0;
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

.empty-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  text-align: center;
}

@media (max-width: 992px) {
  .filter-layout {
    flex-direction: column;
  }

  .filter-sidebar {
    width: 100%;
    margin-bottom: 20px;
    order: 0;
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
