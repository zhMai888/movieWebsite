<template>
  <div class="user-profile-container">
    <!-- 导航栏 -->
    <Navigation/>

    <div class="main-content">
      <!-- 左侧收藏电影区域 -->
      <div class="movie-collections">
        <h2>我的收藏</h2>
        <div v-if="initialLoading" class="loading-indicator">
          加载中...
        </div>
        <MovieCard
          v-else
          :movies="displayMovies"
          :genres="genres"
        />
      </div>

      <!-- 右侧用户信息侧边栏 -->
      <div class="user-sidebar">
        <div class="user-info-card">
          <!-- 用户头像 -->
          <div class="user-avatar">
            <img :src="userInfo.avatar || defaultAvatar" alt="用户头像">
          </div>

          <!-- 用户基本信息 -->
          <div class="user-details">
            <h3>{{ userInfo.username || '未设置用户名' }}</h3>
            <p><span class="label">邮箱:</span> {{ userInfo.email || '未设置邮箱' }}</p>
            <p><span class="label">电话:</span> {{ userInfo.phone || '未设置电话' }}</p>
          </div>

          <!-- 充值按钮 -->
          <button class="recharge-btn" @click="goToRecharge">
            充值
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from '@/components/Navigation.vue'
import MovieCard from '@/components/MovieCard.vue'
import {getUsers} from '@/api/users/users'
import {getCollections} from "@/api/collections/collections";
import defaultAvatar from '@/assets/images/none.png'

export default {
  name: 'UserProfile',
  components: {
    Navigation,
    MovieCard
  },
  data() {
    return {
      initialLoading: true,
      userInfo: {},
      displayMovies: [],
      genres: [], // 假设这是从其他地方获取的类型数据
      defaultAvatar
    }
  },
  created() {
    this.fetchUserData()
    this.fetchCollections()
  },
  methods: {
    async fetchUserData() {
      try {
        // 假设用户ID存储在Vuex或本地存储中
        const userId = this.$store.state.user.id || localStorage.getItem('userId')
        const response = await getUsers(userId)
        this.userInfo = response.data
      } catch (error) {
        console.error('获取用户信息失败:', error)
        this.$message.error('获取用户信息失败')
      }
    },
    async fetchCollections() {
      try {
        const userId = this.$store.state.user.id || localStorage.getItem('userId')
        const response = await getCollections(userId)
        this.displayMovies = response.data
      } catch (error) {
        console.error('获取收藏列表失败:', error)
        this.$message.error('获取收藏列表失败')
      } finally {
        this.initialLoading = false
      }
    },
    goToRecharge() {
      this.$router.push('/recharge')
    }
  }
}
</script>

<style scoped>
.user-profile-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
  padding: 20px;
  gap: 20px;
}

.movie-collections {
  flex: 1;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.user-sidebar {
  width: 300px;
  position: sticky;
  top: 20px;
  height: fit-content;
}

.user-info-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.user-avatar img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #eee;
}

.user-details {
  margin: 20px 0;
  text-align: center;
}

.user-details h3 {
  margin: 0 0 10px;
  color: #333;
}

.user-details p {
  margin: 5px 0;
  color: #666;
}

.user-details .label {
  font-weight: bold;
  color: #333;
}

.recharge-btn {
  padding: 10px 20px;
  background-color: #ff5f5f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.recharge-btn:hover {
  background-color: #ff3f3f;
}

.loading-indicator {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #666;
}

@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }

  .user-sidebar {
    width: 100%;
    position: static;
    order: -1;
    margin-bottom: 20px;
  }
}
</style>
