<template>
  <div class="douban-movie-detail">
    <!-- 顶部导航栏 -->
    <Navigation />

    <div class="container" v-if="!loading">
      <!-- 海报 -->
      <div class="poster">
        <img :src="movie.coverurl" alt="电影海报" />
      </div>

      <!-- 电影信息卡片 -->
      <div class="info">
        <h1 class="title">
          {{ movie.name }}
          <span class="year">({{ movie.releaseDate }})</span>
        </h1>

        <!-- 收藏和播放按钮 -->
        <div class="action-buttons">
          <button
            @click="onFavoriteClick"
            :class="['btn', 'favorite-btn', { active: isFavorited }]"
          >
            {{ isFavorited ? "已收藏" : "收藏" }}
          </button>
          <button @click="onPlayClick" class="btn play-btn">播放</button>
        </div>

        <!-- 评分 -->
        <div class="rating" v-if="movie.scoreCount > 0">
          <div class="score">
            {{ (movie.scoreTotal / movie.scoreCount).toFixed(1) }}
          </div>
          <div class="stars">
            <span
              v-for="n in 5"
              :key="n"
              class="star"
              :class="{ full: n <= Math.round(movie.scoreTotal / movie.scoreCount / 2) }"
            >★</span
            >
          </div>
          <div class="count">{{ movie.scoreCount }}人评分</div>
        </div>

        <!-- 简要信息 -->
        <div class="details">
          <p><strong>类型：</strong>{{ genreName }}</p>
          <p><strong>地区：</strong>{{ areaName }}</p>
          <p><strong>简介：</strong></p>
          <p class="description">{{ movie.description }}</p>
        </div>

        <!-- 导演 -->
        <div class="people-scroll-section">
          <h3>导演</h3>
          <div class="people-scroll" v-if="directors.length">
            <router-link
              class="person"
              v-for="director in directors"
              :key="'director-' + director.id"
              :to="`/person/director/${director.id}`"
              :title="director.name"
            >
              <img :src="director.avatar || defaultAvatar" alt="导演头像" />
              <div class="name">{{ director.name }}</div>
            </router-link>
          </div>
          <div v-else>暂无导演信息</div>
        </div>

        <!-- 演员 -->
        <div class="people-scroll-section">
          <h3>演员</h3>
          <div class="people-scroll" v-if="actors.length">
            <router-link
              class="person"
              v-for="actor in actors"
              :key="'actor-' + actor.id"
              :to="`/person/actor/${actor.id}`"
              :title="actor.name"
            >
              <img :src="actor.avatar || defaultAvatar" alt="演员头像" />
              <div class="name">{{ actor.name }}</div>
            </router-link>
          </div>
          <div v-else>暂无演员信息</div>
        </div>
      </div>
    </div>

    <!-- 加载中提示 -->
    <div v-else class="loading">加载中...</div>
  </div>
</template>

<script>
import Navigation from "@/components/Navigation.vue";
import {
  getMovies,
  listDirectors,
  listActors,
  searchGenres,
  searchAreas,
} from "@/api/movies/movies";

export default {
  name: "DoubanMovieDetail",
  components: { Navigation },
  data() {
    return {
      movie: {},
      genreName: "",
      areaName: "",
      directors: [],
      actors: [],
      defaultAvatar: "https://via.placeholder.com/72?text=无图",
      loading: true,
      isFavorited: false,
    };
  },
  async created() {
    const id = this.$route.params.id;
    try {
      const res = await getMovies(id);
      if (res.code === 200) {
        this.movie = res.data;

        const [directorsRes, actorsRes, genresRes, areasRes] = await Promise.all([
          listDirectors({ movieId: id }),
          listActors({ movieId: id }),
          searchGenres({ id: this.movie.genreId }),
          searchAreas({ id: this.movie.areaId }),
        ]);

        this.genreName =
          genresRes.code === 200 && genresRes.data.length > 0
            ? genresRes.data[0].name
            : "";
        this.areaName =
          areasRes.code === 200 && areasRes.data.length > 0
            ? areasRes.data[0].name
            : "";

        this.directors =
          directorsRes.code === 200
            ? directorsRes.data.map((d) => ({
              id: d.id,
              name: d.name,
              avatar: d.avatarUrl || d.photo || "",
            }))
            : [];

        this.actors =
          actorsRes.code === 200
            ? actorsRes.data.map((a) => ({
              id: a.id,
              name: a.name,
              avatar: a.avatarUrl || a.photo || "",
            }))
            : [];
      }
    } catch (e) {
      console.error(e);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    onFavoriteClick() {
      this.isFavorited = !this.isFavorited;
      alert(this.isFavorited ? "已收藏" : "已取消收藏");
    },
    onPlayClick() {
      alert(`准备播放电影：《${this.movie.name}》`);
    },
  },
};
</script>

<style scoped>
.douban-movie-detail {
  padding-top: 64px;
  background-color: #0f0f0f;
  color: #fff;
  min-height: 100vh;
  font-family: Arial, sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 320px 1fr;
  padding: 2rem;
  gap: 2rem;
}

.poster img {
  width: 100%;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.6);
}

.info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background-color: #1c1c1c;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 8px 20px rgba(255, 255, 255, 0.05);
}

.title {
  font-size: 2rem;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 0.2rem;
}

.year {
  font-weight: normal;
  font-size: 1.2rem;
  color: #bbbbbb;
  margin-left: 0.5rem;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
}

.btn {
  padding: 0.5rem 1.5rem;
  font-size: 1rem;
  border-radius: 24px;
  border: none;
  cursor: pointer;
  color: #fff;
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.favorite-btn {
  background-color: #f5c518;
  color: #000;
}

.favorite-btn:hover {
  background-color: #e0b300;
}

.favorite-btn.active {
  background-color: #e0b300;
}

.play-btn {
  background-color: #2f80ed;
}

.play-btn:hover {
  background-color: #1366d6;
}

.rating {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: #fff;
}

.score {
  font-size: 2.5rem;
  color: #f5c518;
}

.stars {
  font-size: 1.5rem;
  color: #f5c518;
}

.star {
  opacity: 0.3;
}

.star.full {
  opacity: 1;
}

.count {
  font-size: 0.9rem;
  color: #aaa;
}

.details {
  margin-top: 1rem;
  font-size: 0.95rem;
  color: #ccc;
}

.details p {
  margin: 0.2rem 0;
}

.description {
  margin-top: 0.5rem;
  line-height: 1.6;
  color: #ddd;
}

.people-scroll-section {
  margin-top: 2rem;
}

.people-scroll-section h3 {
  margin-bottom: 0.5rem;
  font-size: 1.1rem;
  color: #f5c518;
}

.people-scroll {
  display: flex;
  gap: 1rem;
  overflow-x: auto;
  padding-bottom: 0.5rem;
}

.person {
  flex: 0 0 auto;
  width: 88px;
  text-align: center;
  color: #ccc;
  transition: transform 0.3s ease;
}

.person:hover {
  transform: scale(1.05);
}

.person img {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 0.25rem;
  border: 2px solid #f5c518;
  box-shadow: 0 0 12px rgba(245, 197, 24, 0.5);
}

.name {
  font-size: 0.8rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 加载提示 */
.loading {
  color: #aaa;
  text-align: center;
  margin-top: 150px;
  font-size: 1.2rem;
}
</style>
