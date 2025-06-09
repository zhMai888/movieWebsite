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
          :src="movie.coverurl"
          :alt="movie.name"
          class="carousel-image"
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
  computed: {
    firstFiveMovies() {
      return this.movies.slice(0, 5)
    }
  },
  methods: {
    handleMovieClick(movieId) {
      // 可以在这里实现点击跳转到电影详情页
      console.log('Clicked movie:', movieId)
      // this.$router.push(`/movie/${movieId}`)
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
