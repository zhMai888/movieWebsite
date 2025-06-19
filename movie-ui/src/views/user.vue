<template>
  <div class="user-profile-container">
    <!-- 导航栏 -->
    <Navigation/>

    <div class="main-content">
      <!-- 左侧用户信息侧边栏 -->
      <div class="user-sidebar">
        <div class="user-info-card">
          <!-- 用户头像 -->
          <div class="user-avatar">
            <img :src="getFullAvatarPath(userInfo.userurl)" alt="用户头像">
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

      <!-- 右侧收藏电影区域 -->
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
    </div>
  </div>
</template>

<script>
import Navigation from '@/components/Navigation.vue'
import MovieCard from '@/components/MovieCard.vue'
import {getUsers} from '@/api/users/users'
import {getCollections} from "@/api/collections/collections"
import {getGenres} from "@/api/genres/genres";
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
      genres: [],
      defaultAvatar
    }
  },
  created() {
    this.fetchUserData()
    this.fetchCollections()
  },
  methods: {
    getFullAvatarPath(filename) {
      if (!filename) return defaultAvatar;
      return require(`@/assets/user_avatars/${filename}`);
    },
    async fetchUserData() {
      try {
        const userId = this.$store.state.user.id || localStorage.getItem('userId')

        if (!userId) {
          this.$message.error('用户未登录');
          return;
        }

        const response = await getUsers(userId)
        this.userInfo = response.data
      } catch (error) {
        this.$message.error('获取用户信息失败')
      }
    },
    async fetchCollections() {
      try {
        const userId = this.$store.state.user.id || localStorage.getItem('userId')

        if (!userId) {
          this.$message.error('用户未登录');
          return;
        }

        const response = await getCollections(userId)
        const rawMovies = response.rows || response.data || []

        // 获取类型并补充到每部电影上
        const moviesWithTypes = await Promise.all(
          rawMovies.map(async (movie) => {
            let type = 'Unknown'
            try {
              const res = await getGenres(movie.genreId)
              type = res.data?.type || 'Unknown'
            } catch (err) {
              console.error(`获取类型失败: ${movie.genreId}`, err)
            }
            return {
              ...movie,
              type
            }
          })
        )

        this.displayMovies = moviesWithTypes
      } catch (error) {
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
  position: relative; /* 添加相对定位 */
}

.movie-collections {
  flex: 1;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
  margin-left: 320px; /* 为固定侧边栏留出空间 */
}

.user-sidebar {
  width: 300px;
  position: fixed; /* 改为fixed定位 */
  top: 80px; /* 根据导航栏高度调整 */
  bottom: 0; /* 延伸到页面底部 */
  left: 20px; /* 与main-content的padding一致 */
  height: calc(100vh - 100px); /* 计算高度减去导航栏和间距 */
  overflow-y: auto; /* 如果内容过多可以滚动 */
}

.user-info-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
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

  .movie-collections {
    margin-left: 0; /* 移动端取消左边距 */
    margin-top: 20px; /* 添加顶部间距 */
  }

  .user-sidebar {
    position: static; /* 移动端恢复静态定位 */
    width: 100%;
    height: auto;
    margin-bottom: 20px;
  }
}
</style>
