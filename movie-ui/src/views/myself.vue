<template>
  <div class="user-profile-container">
    <!-- 导航栏 -->
    <Navigation />

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
            <div class="info-item">
              <div class="label">邮箱：</div>
              <div class="value">{{ userInfo.email || '未设置邮箱' }}</div>
            </div>
            <div class="info-item">
              <div class="label">电话：</div>
              <div class="value">{{ userInfo.phone || '未设置电话' }}</div>
            </div>
          </div>

          <!-- 按钮区域 -->
          <div class="action-buttons">
            <button class="logout-btn" @click="logout">退出登录</button>
            <button class="recharge-btn" @click="goToRecharge">充值</button>
          </div>
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
import { getUsers } from '@/api/users/users'
import { getCollections } from "@/api/collections/collections"
import { getGenres } from "@/api/genres/genres"
import defaultAvatar from '@/assets/images/none.png'
import { createAlipayOrder } from '@/api/order'
import userInfo from "@/views/system/user/profile/userInfo.vue";

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
    this.loadUserInfoFromSession()
    this.fetchUserData()
    this.fetchCollections()
    if (!this.userInfo || !this.userInfo.id) {
      this.$router.replace('/user-login') // 或 window.location.replace
    }
  },
  methods: {
    loadUserInfoFromSession() {
      const userInfoStr = sessionStorage.getItem('userInfo')
      if (userInfoStr) {
        try {
          this.userInfo = JSON.parse(userInfoStr)
        } catch (err) {
          console.error('sessionStorage 中 userInfo 解析失败', err)
        }
      }
    },

    getFullAvatarPath(filename) {
      if (!filename) return defaultAvatar
      try {
        return require(`@/assets/user_avatars/${filename}`)
      } catch (e) {
        console.warn('头像加载失败:', filename)
        return defaultAvatar
      }
    },

    async fetchUserData() {
      try {
        const userId = this.$store.state.user.id || localStorage.getItem('userId')
        if (!userId) {
          this.$message.error('用户未登录')
          return
        }
        const response = await getUsers(userId)
        this.userInfo = response.data
        sessionStorage.setItem('userInfo', JSON.stringify(response.data))
      } catch (error) {
        this.$message.error('获取用户信息失败')
      }
    },

    async fetchCollections() {
      try {
        const userId = this.$store.state.user.id || localStorage.getItem('userId')
        if (!userId) {
          this.$message.error('用户未登录')
          return
        }

        const response = await getCollections(userId)
        const rawMovies = response.rows || response.data || []

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

    async goToRecharge() {
      try {
        const userId = this.userInfo.id || this.$store.state.user.id || localStorage.getItem('userId')
        const timestamp = new Date().getTime()
        const outTradeNo = `${userId}_${timestamp}`

        await createAlipayOrder(outTradeNo)

        const paymentUrl = `http://localhost:8080/alipay/pay?outTradeNo=${outTradeNo}&totalAmount=8.88&subject=购买VIP`
        window.location.href = paymentUrl
      } catch (error) {
        console.error('支付请求失败:', error)
        this.$message.error('支付请求失败，请稍后重试')
      }
    },
    logout() {
      // 清除 sessionStorage 中的登录信息
      sessionStorage.removeItem('userInfo')
      localStorage.removeItem('userId')

      // 强制跳转登录页，并刷新页面（重要）
      window.location.replace('/user-login')  // ✅ 硬跳转刷新整个应用，无法“返回”
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
  position: relative;
}

.movie-collections {
  flex: 1;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
  margin-left: 320px;
}

.user-sidebar {
  width: 300px;
  position: fixed;
  top: 80px;
  bottom: 0;
  left: 20px;
  height: calc(100vh - 100px);
  overflow-y: auto;
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
  width: 100%;
  font-size: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px; /* 每项之间间距 */
}

.info-item .label {
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.info-item .value {
  color: #666;
  word-break: break-all;
}

.user-details h3 {
  font-size: 40px;
  text-align: center;
  margin: 0 0 20px;
  color: #333;
}

.user-details p {
  margin: 20px 0;
  color: #666;
}

.user-details .label {
  font-weight: bold;
  color: #333;
}

.action-buttons {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  align-items: center;
}

.recharge-btn,
.logout-btn {
  width: 80%;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.recharge-btn {
  background-color: #ff5f5f;
  color: white;
}

.recharge-btn:hover {
  background-color: #ff3f3f;
}

.logout-btn {
  background-color: #666;
  color: white;
}

.logout-btn:hover {
  background-color: #444;
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
    margin-left: 0;
    margin-top: 20px;
  }

  .user-sidebar {
    position: static;
    width: 100%;
    height: auto;
    margin-bottom: 20px;
  }
}
</style>
