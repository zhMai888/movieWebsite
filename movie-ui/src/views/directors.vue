<template>
  <div class="director-container">
    <Navigation />
    <div class="director-main-content">
      <!-- 搜索提示 -->
      <div v-if="isSearchMode" class="search-tips">
        搜索导演: <strong>{{ searchKeyword }}</strong>
        <button class="clear-search" @click="clearSearch">清除搜索</button>
      </div>

      <!-- 导演卡片网格 -->
      <div class="person-grid" v-if="!initialLoading && displayDirectors.length > 0">
        <div
          class="person-card-wrapper"
          v-for="director in displayDirectors"
          :key="director.id"
        >
          <PeopleGrid :people="[director]" :detail-route="'DirectorDetail'"/>
        </div>
      </div>

      <!-- 空状态提示 -->
      <div v-if="!initialLoading && displayDirectors.length === 0" class="empty-state">
        <div class="empty-icon">👤</div>
        <p v-if="isSearchMode">没有找到与"{{ searchKeyword }}"相关的导演</p>
        <p v-else>暂无导演数据</p>
      </div>

      <!-- 加载更多提示 -->
      <div v-if="loadingMore" class="loading-more">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>

      <!-- 底线提示 -->
      <div v-if="showBottomLine" class="bottom-line">
        <span>已经到底啦 ~</span>
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
import PeopleGrid from "@/components/PeopleGrid.vue";
import Navigation from '@/components/Navigation.vue'
import { listDirectors } from '@/api/directors/directors'
import { throttle } from 'lodash'

export default {
  name: 'DirectorsPage',
  components: {
    PeopleGrid,
    Navigation
  },
  data() {
    return {
      allDirectors: [],
      displayDirectors: [],
      initialLoading: true,
      loadingMore: false,
      currentPage: 1,
      pageSize: 12,
      hasMore: true,
      showBottomLine: false,
      isFetching: false,
      searchKeyword: '',
      isSearchMode: false
    }
  },
  watch: {
    // 监听路由参数变化
    '$route.query': {
      immediate: true,
      handler(newQuery) {
        this.handleRouteQueryChange(newQuery)
      }
    }
  },
  methods: {
    // 处理路由参数变化
    handleRouteQueryChange(query) {
      if (query.director) {
        // 搜索模式
        this.searchKeyword = query.director
        this.isSearchMode = true
        this.resetList()
        this.loadSearchResults(query.director)
      } else {
        // 普通列表模式
        this.isSearchMode = false
        this.resetList()
        this.loadDirectors()
      }
    },

    // 重置列表状态
    resetList() {
      this.currentPage = 1
      this.allDirectors = []
      this.displayDirectors = []
      this.hasMore = true
      this.showBottomLine = false
    },

    // 加载导演数据
    async loadDirectors() {
      if (this.isFetching || !this.hasMore) return

      this.isFetching = true
      this.currentPage === 1
        ? this.initialLoading = true
        : this.loadingMore = true

      try {
        const res = await listDirectors({
          pageNum: this.currentPage,
          pageSize: this.pageSize
        })

        this.handleDataLoaded(res["rows"] || [])
      } catch (error) {
        console.error('加载导演数据失败:', error)
      } finally {
        this.initialLoading = false
        this.loadingMore = false
        this.isFetching = false
      }
    },

    // 加载搜索结果
    async loadSearchResults(keyword) {
      if (this.isFetching) return

      this.isFetching = true
      this.initialLoading = true

      try {
        const res = await listDirectors({
          name: keyword,  // 根据后端SQL，使用name参数进行模糊查询
          pageNum: this.currentPage,
          pageSize: this.pageSize
        })

        this.handleDataLoaded(res["rows"] || [])
      } catch (error) {
        console.error('搜索导演失败:', error)
      } finally {
        this.initialLoading = false
        this.isFetching = false
      }
    },

    // 处理加载的数据
    handleDataLoaded(newDirectors) {
      this.allDirectors = [...this.allDirectors, ...newDirectors]
      this.displayDirectors = this.allDirectors
      this.hasMore = newDirectors.length >= this.pageSize
      this.currentPage++

      if (!this.hasMore) {
        this.showBottomLine = true
      }
    },

    // 清除搜索
    clearSearch() {
      this.$router.replace({ query: {} })
    },

    // 检查滚动位置
    checkScroll() {
      if (this.isSearchMode) return // 搜索模式下不启用无限滚动

      const scrollPosition = window.innerHeight + window.scrollY
      const documentHeight = document.body.offsetHeight
      const threshold = 100

      if (documentHeight - scrollPosition < threshold && this.hasMore && !this.loadingMore) {
        this.loadDirectors()
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
    this.initScrollListener()
  },
  beforeDestroy() {
    this.removeScrollListener()
  }
}
</script>

<style scoped>
.director-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.director-main-content {
  flex: 1;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

/* 导演网格布局 */
.person-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

.person-card-wrapper {
  aspect-ratio: 1/1.2;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .person-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
}

@media (max-width: 480px) {
  .person-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* 加载状态样式 */
.loading-more,
.fullscreen-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  color: #999;
}

.fullscreen-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  z-index: 1000;
}

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

/*搜索功能*/
.search-tips {
  padding: 12px 16px;
  background-color: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.clear-search {
  padding: 4px 8px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  margin-left: 10px;
}

.clear-search:hover {
  background-color: #f0f0f0;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}
</style>
