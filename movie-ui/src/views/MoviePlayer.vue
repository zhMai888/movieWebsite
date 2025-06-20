<template>
  <div class="player-wrapper">
    <Navigation />

    <div class="player-container" v-if="movie.movieurl">
      <h1 class="movie-title">{{ movie.name }}</h1>

      <video
        class="movie-player"
        controls
        autoplay
        :src="movie.movieurl"
      >
        您的浏览器不支持 video 标签。
      </video>
    </div>

    <div v-else class="error-message">
      播放地址无效或未加载
    </div>
  </div>
</template>

<script>
import Navigation from "@/components/Navigation.vue";
import { getMovies } from "@/api/movies/movies";

export default {
  name: "MoviePlayer",
  components: { Navigation },
  data() {
    return {
      movie: {},
    };
  },
  async created() {
    const movieId = this.$route.params.id;
    try {
      const res = await getMovies(movieId);
      if (res.code === 200 && res.data) {
        this.movie = res.data;
      }
    } catch (error) {
      console.error("加载电影失败：", error);
    }
  }
};
</script>

<style scoped>
.player-wrapper {
  background-color: #f6f6f6;
  min-height: 100vh;
  color: #333;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.player-container {
  max-width: 960px;
  margin: 2rem auto;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.movie-title {
  font-size: 2rem;
  margin-bottom: 20px;
  color: #222;
  font-weight: bold;
  text-align: center;
}

.movie-player {
  width: 100%;
  height: 540px;
  border-radius: 10px;
  background: black;
  outline: none;
}

.error-message {
  text-align: center;
  color: #888;
  font-size: 1.2rem;
  padding: 50px;
}
</style>
