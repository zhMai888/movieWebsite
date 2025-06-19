<template>
  <div class="home-container">
    <Navigation />
    <div class="home-main-content">
      <MovieCarousel :movies="displayMovies" v-if="!loading" />

      <!-- 分类筛选框 -->
      <div class="compact-category-filter" v-if="!loading">
        <table class="filter-table">
          <thead>
          <tr>
            <th class="release-year-header">发布时间</th>
            <th class="genre-header">电影风格</th>
            <th class="region-header">电影地区</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td class="release-year-cell">
              <div class="year-options">
                <router-link
                  v-for="year in releaseYears"
                  :key="year.value"
                  :to="`/category?type=year&value=${year.value}`"
                  class="capsule-option"
                >
                  {{ year.label }}
                </router-link>
              </div>
            </td>
            <td class="genre-cell">
              <div class="genre-options">
                <router-link
                  v-for="genre in popularGenres"
                  :key="genre.value"
                  :to="`/category?type=genre&value=${genre.value}`"
                  class="capsule-option"
                >
                  {{ genre.label }}
                </router-link>
              </div>
            </td>
            <td class="area-cell">
              <div class="area-options">
                <router-link
                  v-for="area in area"
                  :key="area.value"
                  :to="`/category?type=area&value=${area.value}`"
                  class="capsule-option"
                >
                  {{ area.label }}
                </router-link>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <MovieCard :movies="displayMovies" :genres="genres" v-if="!initialLoading" />

      <!-- 底部加载提示 -->
      <div v-if="loadingMore" class="loading-more">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>

      <!-- 底线提示 -->
      <div v-if="showBottomLine" class="bottom-line">
        <span>我也是有底线的哦 😊</span>
      </div>

      <!-- 初始加载提示 -->
      <div v-if="initialLoading" class="fullscreen-loading">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from '@/components/Navigation.vue'
import MovieCard from '@/components/MovieCard.vue'
import MovieCarousel from '@/components/MovieCarousel.vue'
import { listMovies } from '@/api/movies/movies'
import { listGenres } from '@/api/genres/genres'
import { throttle } from 'lodash'
import genres from "@/views/genres/genres/index.vue";

export default {
  name: 'Home',
  computed: {
    genres() {
      return genres
    }
  },
  components: {
    Navigation,
    MovieCard,
    MovieCarousel,
  },
  data() {
    return {
      allMovies: [],
      displayMovies: [],
      initialLoading: true,  // 初始加载状态
      loadingMore: false,    // 加载更多状态
      error: null,
      genreCache: {},        // 类型缓存，id => type 映射
      currentPage: 1,
      pageSize: 10,
      hasMore: true,
      showBottomLine: false,
      isFetching: false,
      loading:false,
      // 发布时间选项
      releaseYears: [
        {label:'90年代',value:'1990'},
        {label:'00年代',value:'2000'},
        {label:'其他',value:'Other'}
      ],
      // 电影风格选项
      popularGenres: [
        { label: '动作', value: '13' },
        { label: '犯罪', value: '12' },
        { label: '戏剧', value: '15' },
        { label: '科幻', value: '14' },
        { label: '恐怖', value: '16' },
        { label: '奇幻', value: '18' },
        { label: '其他', value: 'Other' }
      ],
      // 电影地区选项
      area: [
        { label: '中国', value: '1' },
        { label: '美国', value: '6' },
        { label: '英国', value: '9' },
        { label: '日本', value: '8' },
        { label: '韩国', value: '7' },
        { label: '其他', value: 'Other' }
      ]
    }
  },
  methods: {
    // 预加载全部电影类型，构建 genreCache
    async getAllGenres() {
      try {
        const res = await listGenres({ pageSize: 100 }) // 这里改用能返回所有类型的接口
        const genreList = res.rows || []

        this.genreCache = {}
        genreList.forEach(genre => {
          this.genreCache[genre.id] = genre.type
        })
      } catch (e) {
        console.error('获取全部类型失败:', e)
      }
    },

    // 初始化加载电影数据
    async loadMovies() {
      if (this.isFetching || !this.hasMore) return

      this.isFetching = true
      // 区分初始加载和加载更多
      if (this.currentPage === 1) {
        this.initialLoading = true
      } else {
        this.loadingMore = true
      }

      try {
        const moviesRes = await listMovies({
          pageSize: this.pageSize,
          pageNum: this.currentPage,
        })

        const rawMovies = moviesRes["rows"] || []

        // 直接用 genreCache 映射类型，避免重复请求
        const moviesWithTypes = rawMovies.map(movie => ({
          ...movie,
          type: this.genreCache[movie.genreId] || 'Unknown'
        }))

        this.allMovies = [...this.allMovies, ...moviesWithTypes]
        this.displayMovies = this.allMovies

        this.hasMore = rawMovies.length >= this.pageSize
        this.currentPage++

        if (!this.hasMore) {
          this.showBottomLine = true
        }
      } catch (err) {
        console.error('数据加载失败:', err)
        this.error = '加载电影数据失败，请稍后重试'
      } finally {
        this.initialLoading = false
        this.loadingMore = false
        this.isFetching = false
        this.loading = false
      }
    },

    // 检查是否滚动到底部
    checkScroll() {
      const scrollPosition = window.innerHeight + window.scrollY
      const documentHeight = document.body.offsetHeight
      const threshold = 50 // 提前50px加载

      if (documentHeight - scrollPosition < threshold && this.hasMore && !this.loadingMore) {
        this.loadMovies()
      }
    },

    // 初始化滚动监听
    initScrollListener() {
      window.addEventListener('scroll', throttle(this.checkScroll, 200))
    },

    // 移除滚动监听
    removeScrollListener() {
      window.removeEventListener('scroll', this.checkScroll)
    }
  },
  async created() {
    await this.getAllGenres()
    await this.loadMovies()
    this.initScrollListener()
  },
  beforeDestroy() {
    this.removeScrollListener()
  }
}
</script>

<style scoped>
/* 样式保持不变 */
.home-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.home-main-content {
  flex: 1;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

/* 紧凑型分类筛选框样式 */
.compact-category-filter {
  margin: 30px 0;
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  border: none;
}

.filter-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 10px;
}

.filter-table th {
  padding: 0 15px 8px 15px;
  text-align: left;
  background-color: transparent;
  font-weight: 600;
  color: #333;
  border-bottom: 1px solid #eaeaea;
}

.filter-table td {
  padding: 5px 15px;
  border-top: none;
}

/* 列宽设置 */
.release-year-header,
.release-year-cell {
  width: 25%;  /* 发布时间列宽度较小 */
}

.genre-header,
.genre-cell {
  width: 40%;  /* 电影风格列宽度较大 */
}

.area-cell {
  width: 35%;  /* 电影地区列宽度较大 */
}

/* 胶囊状选项按钮 */
.capsule-option {
  padding: 6px 12px;
  background: #f5f5f5;
  border-radius: 15px;
  text-align: center;
  color: #333;
  text-decoration: none;
  transition: all 0.3s ease;
  border: none;
  font-size: 0.9rem;
  white-space: nowrap;
  display: inline-block;
  margin: 2px 4px 2px 0;
  border-top: 1px solid #e0e0e0;
  border-bottom: 1px solid #e0e0e0;
}

.capsule-option:hover {
  background: #757575;
  color: white;
  border-top: 1px solid #757575;
  border-bottom: 1px solid #757575;
}

/* 选项布局 */
.year-options,
.genre-options,
.area-options {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .compact-category-filter {
    overflow-x: auto;
  }

  .filter-table {
    min-width: 600px;
  }

  .capsule-option {
    padding: 5px 10px;
    font-size: 0.85rem;
  }
}

/* 底部加载更多提示 */
.loading-more {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  color: #999;
}

/* 初始全屏加载提示 */
.fullscreen-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  z-index: 1000;
}

/* 加载动画 */
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #1890ff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 底线提示样式 */
.bottom-line {
  text-align: center;
  padding: 20px 0;
  color: #999;
  font-size: 14px;
  margin-top: 20px;
  border-top: 1px solid #eee;
}

.bottom-line span {
  display: inline-block;
  padding: 0 15px;
  background: #fff;
  position: relative;
  top: -12px;
}
</style>
