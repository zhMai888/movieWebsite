<template>
  <div class="movie-grid">
    <div class="movie-grid-inner">
      <div
        class="movie-card-wrapper"
        v-for="movie in movies"
        :key="movie.id"
      >
        <div class="movie-card">
          <!-- 电影海报区域（含VIP标识） -->
          <div class="poster-container">
            <router-link
              :to="{name: 'MovieDetail', params: {id: movie.id}}"
              class="poster-link"
            >
              <img
                class="movie-poster"
                :src="getFullPosterPath(movie.coverurl) || defaultPoster"
                :alt="movie.name"
                loading="lazy"
                @error="handleImageError"
              />
              <div v-if="movie.see === 1" class="vip-tag">
                <span>VIP</span>
              </div>
              <!-- 悬停遮罩效果 -->
              <div class="poster-overlay">
                <span class="view-detail">查看详情</span>
              </div>
            </router-link>
          </div>

          <!-- 电影信息区域 -->
          <div class="movie-info">
            <h3 class="movie-title">
              <router-link :to="{name: 'MovieDetail', params: {id: movie.id}}">
                {{ movie.name }}
              </router-link>
            </h3>

            <div class="rating-container">
              <el-rate
                :value="calculateRating(movie)"
                disabled
                :max="5"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                :score-template="calculateRating(movie).toFixed(1)"
                show-score
                text-color="#ff9900"
              />
            </div>

            <div class="meta-container">
              <span class="release-year">
                {{ safeReleaseYear(movie.releaseDate) }}
              </span>
              <span class="genre-tag">
                {{ movie.type || '未知' }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    movies: {
      type: Array,
      required: true,
      default: () => []

    }
  },
  data() {
    return {
      defaultPoster: require('@/assets/images/none.png')
    }
  },
  methods: {
    // 安全获取发行年份
    safeReleaseYear(date) {
      // 如果是有效年份数字（1900-2100之间）
      if (typeof date === 'number' && date > 1900 && date < 2100) {
        return date.toString();
      }
      // 如果是日期字符串
      if (typeof date === 'string' && date.length >= 4) {
        return date.substring(0, 4);
      }
      return '未知年份';
    },

    // 获取完整海报路径
    getFullPosterPath(filename) {
      if (!filename) return null;

      try {
        // 开发环境使用require方式加载（webpack会处理）
        return require(`@/assets/thumbnails_done/movie_posters/${filename}`);
      } catch (e) {
        // 生产环境使用直接路径
        return `/assets/thumbnails_done/movie_posters/${filename}`;
      }
    },

    // 修正错误处理方法
    handleImageError(event) {
      event.target.src = this.defaultPoster;
      event.target.onerror = null; // 防止默认图片也出错时无限循环
    },

    // 计算评分
    calculateRating(movie) {
      if (!movie.scoreCount || movie.scoreCount === 0) {
        return 0; // 如果没有评分人数，返回0
      }
      // 计算平均分
      return movie.scoreTotal / movie.scoreCount;
    }
  }
}
</script>

<style scoped>
/* 网格布局系统 */
.movie-grid {
  padding: 0 12px;
}

.movie-grid-inner {
  display: flex;
  flex-wrap: wrap;
  margin: -8px;
}

.movie-card-wrapper {
  padding: 8px;
  width: 50%;
}

/* 卡片主体样式 */
.movie-card {
  height: 100%;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 海报区域样式 */
.poster-container {
  position: relative;
  padding-top: 150%; /* 2:3比例 */
  overflow: hidden;
}

.poster-link {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: block;
}

.movie-poster {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.movie-card:hover .movie-poster {
  transform: scale(1.05);
}

/* VIP标识 */
.vip-tag {
  position: absolute;
  top: 10px;
  right: -18px;
  width: 75px;
  padding: 3px 0;
  background-color: #FFA500; /* 纯金色背景 */
  color: white;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  transform: rotate(45deg);
  z-index: 2;
}

/* 海报悬停效果 */
.poster-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 1;
}

.movie-card:hover .poster-overlay {
  opacity: 1;
}

.view-detail {
  color: white;
  font-weight: bold;
  padding: 6px 12px;
  border: 2px solid white;
  border-radius: 20px;
}

/* 电影信息区域 */
.movie-info {
  padding: 12px;
}

.movie-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.movie-title a {
  color: #333;
  text-decoration: none;
}

.movie-title a:hover {
  color: #ff9900;
}

/* 评分区域 */
.rating-container {
  margin-bottom: 8px;
}

.rating-container {
  font-size: 18px;
}

.rating-container {
  margin-left: 6px;
  font-size: 14px;
  font-weight: bold;
}

/* 元信息区域 */
.meta-container {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #666;
}

.release-year {
  color: #888;
}

.genre-tag {
  background: #f5f5f5;
  padding: 2px 8px;
  border-radius: 4px;
}

/* 响应式设计 */
@media (min-width: 768px) {
  .movie-card-wrapper {
    width: 33.33%;
  }
}

@media (min-width: 992px) {
  .movie-card-wrapper {
    width: 25%;
  }
}

@media (min-width: 1200px) {
  .movie-card-wrapper {
    width: 20%;
  }
}
</style>
