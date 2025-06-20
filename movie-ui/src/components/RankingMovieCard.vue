<template>
  <div class="movie-detail-container">
    <!-- 左侧电影卡片 -->
    <div class="left-card-wrapper">
      <div
        class="movie-card"
        @mouseenter="hover = true"
        @mouseleave="hover = false"
      >
        <div class="poster-container">
          <img
            :src="getPosterPath(movie.coverurl)"
            alt="电影海报"
            class="movie-poster"
            @error="handleImageError"
            loading="lazy"
          />
          <div v-if="movie.see === 1" class="vip-tag"><span>VIP</span></div>
          <div class="poster-overlay" :class="{ visible: hover }">
            <span class="view-detail">查看详情</span>
          </div>
        </div>

        <div class="movie-info">
          <h3 class="movie-title" :title="movie.name">{{ movie.name }}</h3>

          <div class="rating-container" v-if="movie.scoreCount > 0">
            <el-rate
              :value="parseFloat((movie.scoreTotal / movie.scoreCount).toFixed(1))"
              disabled
              :max="5"
              :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              show-score
              text-color="#ff9900"
            />
          </div>
          <div v-else class="rating-container no-score">暂无评分</div>

          <div class="meta-container">
            <span class="release-year">{{ safeReleaseYear(movie.releaseDate) }}</span>
            <!-- 类型标签已去除 -->
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧简介介绍 -->
    <div class="right-intro">
      <h2 class="movie-title">{{ movie.name }}</h2>
      <p class="movie-description">{{ movie.description || '暂无简介' }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "SimpleMovieDetail",
  props: {
    movie: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      defaultPoster: require("@/assets/images/none.png"),
      hover: false,
    };
  },
  methods: {
    getPosterPath(filename) {
      if (!filename) return this.defaultPoster;
      try {
        return require(`@/assets/thumbnails_done/movie_posters/${filename}`);
      } catch (e) {
        return `/assets/thumbnails_done/movie_posters/${filename}`;
      }
    },
    handleImageError(event) {
      event.target.src = this.defaultPoster;
      event.target.onerror = null;
    },
    safeReleaseYear(date) {
      if (typeof date === "number" && date > 1900 && date < 2100) {
        return date.toString();
      }
      if (typeof date === "string" && date.length >= 4) {
        return date.substring(0, 4);
      }
      return "未知年份";
    },
  },
};
</script>

<style scoped>
.movie-detail-container {
  display: flex;
  gap: 24px;
  max-width: 900px;
  margin: 0 auto;
}

.left-card-wrapper {
  width: 224px;
  height: 433px;
}

.movie-card {
  width: 224px;
  height: 433px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  cursor: default;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.poster-container {
  position: relative;
  padding-top: 150%; /* 2:3比例 */
  overflow: hidden;
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
  background-color: #ffa500;
  color: white;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  transform: rotate(45deg);
  z-index: 2;
}

/* 悬浮遮罩 */
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
  pointer-events: none;
}

.poster-overlay.visible {
  opacity: 1;
  pointer-events: auto;
}

.view-detail {
  color: white;
  font-weight: bold;
  padding: 6px 12px;
  border: 2px solid white;
  border-radius: 20px;
  user-select: none;
}

.movie-info {
  padding: 12px 16px 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.movie-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
}

.rating-container {
  font-size: 18px;
  color: #ff9900;
  margin-bottom: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.rating-container.no-score {
  color: #999;
  font-size: 14px;
  font-style: italic;
}

.meta-container {
  display: flex;
  justify-content: flex-start; /* 类型去掉了，只靠左 */
  font-size: 13px;
  color: #666;
}

.release-year {
  color: #888;
}

/* 右侧简介 */
.right-intro {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.right-intro .movie-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 16px;
}

.movie-description {
  font-size: 16px;
  line-height: 1.5;
  color: #444;
  white-space: pre-wrap;
}
</style>
