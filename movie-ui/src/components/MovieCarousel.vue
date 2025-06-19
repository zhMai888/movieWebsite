<template>
  <div class="movie-carousel">
    <el-carousel
      :interval="4000"
      type="card"
      height="350px"
      indicator-position="outside"
    >
      <el-carousel-item
        v-for="movie in firstFiveMovies"
        :key="movie.id"
        @click="handleMovieClick(movie.id)"
      >
        <img
          :src="getFullPosterPath(movie.coverurl)"
          :alt="movie.name"
          class="carousel-image"
          @error="handleImageError"
        />
        <div class="movie-info">
          <h3 class="movie-title">{{ movie.name }}</h3>
          <div class="movie-rating">
            ⭐ {{ (movie.scoreTotal / movie.scoreCount).toFixed(1) }}
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  props: {
    movies: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      defaultPoster: require('@/assets/images/none.png') // 新增默认图片
    }
  },
  computed: {
    firstFiveMovies() {
      return this.movies.slice(7, 12)
    }
  },
  methods: {
    // 新增方法：拼接完整路径
    getFullPosterPath(filename) {
      if (!filename) return this.defaultPoster;
      try {
        return require(`@/assets/thumbnails_done/movie_posters/${filename}`);
      } catch {
        return this.defaultPoster;
      }
    },

    // 新增方法：图片加载错误处理
    handleImageError(event) {
      event.target.src = this.defaultPoster;
      event.target.onerror = null;
    },

    // 保持原有方法不变
    handleMovieClick(movieId) {
      console.log('Clicked movie:', movieId)
    }
  }
}
</script>

<style scoped>
.movie-carousel {
  margin-bottom: 30px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s;
}

.carousel-image:hover {
  transform: scale(1.02);
}

.movie-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

.movie-title {
  color: white;
  margin: 0;
  font-size: 1.2rem;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.8);
}

.movie-rating {
  color: #ffd700;
  font-weight: bold;
  margin-top: 5px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .movie-carousel {
    margin-bottom: 20px;
  }

  .movie-title {
    font-size: 1rem;
  }
}
</style>
