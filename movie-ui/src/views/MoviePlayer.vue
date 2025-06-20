<template>
  <div class="player-wrapper">
    <Navigation />

    <div v-if="!movie || Object.keys(movie).length === 0" class="loading-message">
      正在加载电影信息...
    </div>

    <div v-else-if="movie.movieurl" class="player-container">
      <!-- 标题和评分区域 -->
      <div class="title-rating-wrapper">
        <h1 class="movie-title">{{ movie.name }}</h1>
        <div class="movie-score" :title="`平均评分：${avgScore}（${movie.scoreCount}人评分）`">
          <div class="score-main">
            <span class="score-number">{{ avgScore }}</span>
            <span class="score-out-of">/5.0</span>
          </div>
          <div class="stars-wrapper">
            <span
              v-for="(star, index) in starTypes"
              :key="index"
              class="star"
              :class="star"
            ></span>
            <span class="score-count">（{{ movie.scoreCount }}人评分）</span>
          </div>
        </div>
      </div>

      <!-- 播放器 -->
      <video
        class="movie-player"
        controls
        autoplay
        :src="movie.movieurl"
        preload="metadata"
      >
        您的浏览器不支持 video 标签。
      </video>

      <!-- 电影详情信息区域（只显示文字） -->
      <div class="movie-info-section">
        <div class="movie-details-only">
          <p class="movie-date"><strong>上映日期：</strong>{{ movie.releaseDate || '未知' }}</p>
          <p class="movie-genre"><strong>类型：</strong>{{ genreName || '未知' }}</p>
          <p class="movie-area"><strong>地区：</strong>{{ areaName || '未知' }}</p>
          <p class="movie-description" v-if="movie.description && movie.description.trim() !== ''">
            {{ movie.description }}
          </p>
          <p v-else class="movie-description empty-desc">暂无简介</p>
        </div>
      </div>

      <!-- 导演展示 -->
      <div class="people-section">
        <h2>导演</h2>
        <div class="people-list">
          <router-link
            v-for="d in directors"
            :key="d.id"
            :to="`/person/director/${d.id}`"
            class="person-card"
            :title="d.name"
          >
            <img :src="getFullPhotoPath(d.photourl)" alt="导演头像" class="avatar" />
            <div class="person-name">{{ d.name }}</div>
          </router-link>
        </div>
      </div>

      <!-- 演员展示 -->
      <div class="people-section">
        <h2>演员</h2>
        <div class="people-list">
          <router-link
            v-for="a in actors"
            :key="a.id"
            :to="`/person/actor/${a.id}`"
            class="person-card"
            :title="a.name"
          >
            <img :src="getFullPhotoPath(a.photourl)" alt="演员头像" class="avatar" />
            <div class="person-name">{{ a.name }}</div>
          </router-link>
        </div>
      </div>
    </div>

    <div v-else class="error-message">
      播放地址为空或无效，请稍后再试。
    </div>
  </div>
</template>

<script>
import Navigation from "@/components/Navigation.vue";
import { getMovies, getActorIdsByMovie, getDirectorIdsByMovie } from "@/api/movies/movies";
import { getActors } from "@/api/actors/actors";
import { getDirectors } from "@/api/directors/directors";
import { listGenres } from "@/api/genres/genres";
import { listAreas } from "@/api/areas/areas";

export default {
  name: "MoviePlayer",
  components: { Navigation },
  data() {
    return {
      movie: {},
      directors: [],
      actors: [],
      genres: [],
      areas: []
    };
  },
  computed: {
    genreName() {
      const genre = this.genres.find(g => g.id === this.movie.genreId);
      return genre ? genre.type : "";
    },
    areaName() {
      const area = this.areas.find(a => a.id === this.movie.areaId);
      return area ? area.areaname : "";
    },
    avgScore() {
      if (!this.movie.scoreCount) return 0;
      // 10分制转5分制，保留1位小数
      return +((this.movie.scoreTotal / this.movie.scoreCount)).toFixed(1);
    },
    starTypes() {
      const stars = [];
      const roundedStars = Math.round(this.avgScore); // 四舍五入取整星数，0~5
      for (let i = 0; i < 5; i++) {
        stars.push(i < roundedStars ? 'full' : 'empty');
      }
      return stars;
    }
  },
  methods: {
    getFullPosterPath(filename) {
      if (!filename) return null;
      try {
        return require(`@/assets/thumbnails_done/movie_posters/${filename}`);
      } catch (e) {
        return `/assets/thumbnails_done/movie_posters/${filename}`;
      }
    },
    getFullPhotoPath(filename) {
      if (!filename) return null;
      try {
        return require(`@/assets/thumbnails_done/preson_posters/${filename}`);
      } catch (e) {
        return `/assets/thumbnails_done/preson_posters/${filename}`;
      }
    }
  },
  async created() {
    const movieId = this.$route.params.id;
    try {
      const res = await getMovies(movieId);
      if (res.code === 200 && res.data) this.movie = res.data;

      const directorIdList = await getDirectorIdsByMovie(movieId);
      if (Array.isArray(directorIdList)) {
        const directorDetails = await Promise.all(
          directorIdList.map(id => getDirectors(id).then(res => res.data || res))
        );
        this.directors = directorDetails;
      }

      const actorIdList = await getActorIdsByMovie(movieId);
      if (Array.isArray(actorIdList)) {
        const actorDetails = await Promise.all(
          actorIdList.map(id => getActors(id).then(res => res.data || res))
        );
        this.actors = actorDetails;
      }

      const genresRes = await listGenres({ pageSize: 100 });
      if (genresRes.rows) this.genres = genresRes.rows;
      const areasRes = await listAreas({ pageSize: 100 });
      if (areasRes.rows) this.areas = areasRes.rows;
    } catch (error) {
      console.error("加载电影失败：", error);
    }
  }
};
</script>

<style scoped>
.player-wrapper {
  background-color: #f5f5f5;
  min-height: 100vh;
  color: #333;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  padding-bottom: 50px;
}

/* 容器 */
.player-container {
  max-width: 1100px;
  margin: 3rem auto 5rem auto;
  padding: 40px 48px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border: 1px solid #e0e0e0;
}

/* 标题和评分 */
.title-rating-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  border-bottom: 1px solid #eee;
  padding-bottom: 12px;
}

.movie-title {
  font-size: 2.8rem;
  font-weight: 700;
  color: #222;
  line-height: 1.2;
  flex: 1;
}

.movie-score {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-weight: 700;
  color: #e3b23c;
  user-select: none;
}

.score-main {
  display: flex;
  align-items: baseline;
  font-size: 3.2rem;
  font-weight: 700;
  color: #f5c518; /* IMDb 黄色 */
  letter-spacing: -0.04em;
}

.score-number {
  line-height: 1;
}

.score-out-of {
  font-size: 1.6rem;
  font-weight: 400;
  color: #888;
  margin-left: 4px;
}

.stars-wrapper {
  margin-top: 6px;
  display: flex;
  align-items: center;
  font-size: 1.8rem;
}

.score-count {
  font-weight: 400;
  font-size: 1rem;
  color: #666;
  margin-left: 8px;
}

/* 星星样式 */
.star {
  display: inline-block;
  color: #f5c518; /* IMDb黄色 */
  position: relative;
  margin-right: 2px;
  user-select: none;
}

/* 满星 */
.star.full::before {
  content: "★";
  font-size: 1.8rem;
}

/* 空星 */
.star.empty::before {
  content: "☆";
  color: #ccc;
  font-size: 1.8rem;
}

/* 播放器 */
.movie-player {
  width: 100%;
  height: 560px;
  border-radius: 14px;
  background: #000;
  box-shadow: 0 8px 18px rgba(0, 0, 0, 0.25);
  outline: none;
  margin: 32px 0;
  transition: box-shadow 0.3s ease;
}

.movie-player:hover {
  box-shadow: 0 12px 36px rgba(0, 0, 0, 0.4);
}

/* 电影详情信息区，去除封面，文字宽度限制和居中 */
.movie-info-section {
  max-width: 720px;
  margin: 0 auto;
  padding: 0 12px;
}

.movie-details-only {
  font-size: 1.1rem;
  color: #444;
  line-height: 1.7;
  user-select: text;
}

.movie-date,
.movie-genre,
.movie-area {
  font-weight: 600;
  margin-bottom: 12px;
  color: #555;
}

/* 简介 */
.movie-description {
  margin-top: 20px;
  font-size: 1rem;
  color: #555;
  white-space: pre-wrap;
  line-height: 1.7;
}

.empty-desc {
  font-style: italic;
  color: #999;
}

/* 导演和演员展示 */
.people-section {
  max-width: 840px;
  margin: 60px auto 0 auto;
}

.people-section h2 {
  font-weight: 700;
  color: #222;
  margin-bottom: 20px;
  font-size: 1.75rem;
  border-left: 5px solid #e3b23c;
  padding-left: 12px;
  user-select: none;
}

.people-list {
  display: flex;
  gap: 28px;
  flex-wrap: wrap;
}

.person-card {
  width: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  text-decoration: none;
  color: inherit;
  transition: transform 0.3s ease;
  user-select: none;
}

.person-card:hover {
  transform: translateY(-6px);
}

.avatar {
  width: 100px;
  height: 135px;
  border-radius: 10px;
  object-fit: cover;
  margin-bottom: 8px;
  box-shadow:
    0 6px 16px rgba(0, 0, 0, 0.12);
  border: 1px solid #ddd;
  transition: box-shadow 0.3s ease;
}

.person-card:hover .avatar {
  box-shadow:
    0 10px 30px rgba(0, 0, 0, 0.25);
}

.person-name {
  font-size: 1rem;
  text-align: center;
  color: #444;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

/* 加载和错误信息 */
.loading-message,
.error-message {
  text-align: center;
  color: #888;
  font-size: 1.3rem;
  padding: 80px 0;
}
</style>
