<template>
  <div class="rankings-container">
    <Navigation />

    <!-- 选项卡按钮 -->
    <div class="tab-header">
      <button
        v-for="(tab, index) in tabs"
        :key="index"
        :class="['tab-button', { active: activeTab === tab.key }]"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- 排行榜内容 -->
    <div class="rankings">
      <RankSection
        v-if="activeTab === 'week'"
        title="周播放排行"
        :movies="topWeekList"
        playKey="weekcount"
        @select="goToDetail"
      />
      <RankSection
        v-if="activeTab === 'month'"
        title="月播放排行"
        :movies="topMonthList"
        playKey="monthcount"
        @select="goToDetail"
      />
      <RankSection
        v-if="activeTab === 'total'"
        title="总播放排行"
        :movies="topTotalList"
        playKey="count"
        @select="goToDetail"
      />
    </div>
  </div>
</template>

<script>
import { listMovies } from '@/api/movies/movies'
import Navigation from '@/components/Navigation.vue'
import RankSection from '@/components/RankSection.vue'

export default {
  name: 'MovieRankings',
  components: {
    Navigation,
    RankSection
  },
  data() {
    return {
      movies: [],
      topWeekList: [],
      topMonthList: [],
      topTotalList: [],
      activeTab: 'week',
      tabs: [
        { key: 'week', label: '周排行' },
        { key: 'month', label: '月排行' },
        { key: 'total', label: '总排行' }
      ]
    }
  },
  methods: {
    goToDetail(id) {
      this.$router.push(`/movies/${id}`)
    },
    processRankings() {
      const sortedWeek = [...this.movies].sort((a, b) => (b.weekcount || 0) - (a.weekcount || 0))
      const sortedMonth = [...this.movies].sort((a, b) => (b.monthcount || 0) - (a.monthcount || 0))
      const sortedTotal = [...this.movies].sort((a, b) => (b.count || 0) - (a.count || 0))
      this.topWeekList = sortedWeek.slice(0, 15)
      this.topMonthList = sortedMonth.slice(0, 15)
      this.topTotalList = sortedTotal.slice(0, 15)
    }
  },
  mounted() {
    listMovies({ pageNum: 1, pageSize: 1000 })
      .then(res => {
        if (res.code === 0) {
          this.movies = res.rows || []
          this.processRankings()
        } else {
          console.error('接口返回异常:', res.msg)
        }
      })
      .catch(err => {
        console.error('接口请求失败:', err)
      })
  }
}
</script>

<style scoped>
.rankings-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  color: #e5e7eb;
  background-color: #1f2937;
  min-height: 100vh;
}

/* 顶部 tab 按钮组 */
.tab-header {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin-bottom: 20px;
}

.tab-button {
  padding: 10px 20px;
  border-radius: 9999px;
  border: none;
  background-color: #374151;
  color: #9ca3af;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-button:hover {
  background-color: #4b5563;
  color: white;
}

.tab-button.active {
  background-color: #3b82f6;
  color: white;
}

/* 排行榜容器 */
.rankings {
  display: flex;
  flex-direction: column;
  gap: 0;
}

section {
  flex: 1;
  background-color: #1f2937;
  padding: 0 16px 20px 16px;
  border-bottom: 1px solid #374151;
}
</style>
