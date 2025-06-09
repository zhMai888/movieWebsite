<template>
  <div class="actor-container">
    <Navigation />
    <div class="actor-main-content">
      <!-- 演员卡片网格 -->
      <div class="person-grid" v-if="!initialLoading">
        <div
          class="person-card-wrapper"
          v-for="actor in displayActors"
          :key="actor.id"
        >
          <PeopleGrid :people="[actor]"/>
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
import { listActors } from '@/api/actors/actors'
import { throttle } from 'lodash'

export default {
  name: 'ActorPage',
  components: {
    PeopleGrid,
    Navigation,
    ActorCard: PeopleGrid
  },
  data() {
    return {
      allActors: [],
      displayActors: [],
      initialLoading: true,
      loadingMore: false,
      currentPage: 1,
      pageSize: 12, // 每页加载数量适当增加
      hasMore: true,
      showBottomLine: false,
      isFetching: false
    }
  },
  methods: {
    // 加载演员数据
    async loadActors() {
      if (this.isFetching || !this.hasMore) return

      this.isFetching = true
      this.currentPage === 1
        ? this.initialLoading = true
        : this.loadingMore = true

      try {
        const res = await listActors({
          pageNum: this.currentPage,
          pageSize: this.pageSize
        })

        const newActors = res["rows"] || []
        this.allActors = [...this.allActors, ...newActors]
        this.displayActors = this.allActors

        this.hasMore = newActors.length >= this.pageSize
        this.currentPage++

        if (!this.hasMore) {
          this.showBottomLine = true
        }
      } catch (error) {
        console.error('加载演员数据失败:', error)
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
      const threshold = 100 // 提前100px加载

      if (documentHeight - scrollPosition < threshold && this.hasMore && !this.loadingMore) {
        this.loadActors()
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
    await this.loadActors()
    this.initScrollListener()
  },
  beforeDestroy() {
    this.removeScrollListener()
  }
}
</script>

<style scoped>
.actor-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.actor-main-content {
  flex: 1;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

/* 演员网格布局 */
.person-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

.person-card-wrapper {
  aspect-ratio: 1/1.2; /* 卡片宽高比 */
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

/* 加载状态样式（保持原样） */
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
