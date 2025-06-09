<template>
  <div class="director-container">
    <Navigation />
    <div class="director-main-content">
      <!-- 导演卡片网格 -->
      <div class="person-grid" v-if="!initialLoading">
        <div
          class="person-card-wrapper"
          v-for="director in displayDirectors"
          :key="director.id"
        >
          <PeopleGrid :people="[director]" :detail-route="'DirectorDetail'"/>
        </div>
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
      isFetching: false
    }
  },
  methods: {
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

        const newDirectors = res["rows"] || []
        this.allDirectors = [...this.allDirectors, ...newDirectors]
        this.displayDirectors = this.allDirectors

        this.hasMore = newDirectors.length >= this.pageSize
        this.currentPage++

        if (!this.hasMore) {
          this.showBottomLine = true
        }
      } catch (error) {
        console.error('加载导演数据失败:', error)
      } finally {
        this.initialLoading = false
        this.loadingMore = false
        this.isFetching = false
      }
    },

    // 检查滚动位置
    checkScroll() {
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
    await this.loadDirectors()
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
</style>
