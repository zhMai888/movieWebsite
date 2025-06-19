<template>
  <div class="page-wrapper">
    <Navigation />
    <div class="movie-detail-container">
      <!-- 左侧大海报 -->
      <div class="poster-wrapper">
        <img
          v-if="movie.coverurl"
          :src="getFullPosterPath(movie.coverurl)"
          alt="电影封面"
          class="poster"
        />
      </div>

      <!-- 右侧信息区 -->
      <div class="info-wrapper">
        <h1 class="movie-title">{{ movie.name }}</h1>
        <div class="movie-score">
          <span class="score-number">{{ (movie.scoreTotal / (movie.scoreCount || 1)).toFixed(1) }}</span>
          <span class="score-count">（{{ movie.scoreCount }}人评分）</span>
        </div>
        <p class="movie-description">{{ movie.description }}</p>

        <!-- 播放和收藏按钮 -->
        <div class="action-buttons">
          <button @click="handlePlay" class="btn-play">播放</button>
          <button @click="toggleFavorite" :class="{'btn-favorite': true, 'favorited': isFavorite}">
            {{ isFavorite ? '已收藏' : '收藏' }}
          </button>
        </div>

        <div class="movie-meta">
          <p><strong>上映日期：</strong>{{ movie.releaseDate }}</p>
          <p><strong>地区：</strong>{{ areaName }}</p>
          <p><strong>类型：</strong>{{ genreName }}</p>

          <div class="watch-counts">
            <div class="watch-count-card">
              <div class="count-number">{{ movie.count }}</div>
              <div class="count-label">总观看</div>
            </div>
            <div class="watch-count-card">
              <div class="count-number">{{ movie.weekcount }}</div>
              <div class="count-label">本周观看</div>
            </div>
            <div class="watch-count-card">
              <div class="count-number">{{ movie.monthcount }}</div>
              <div class="count-label">本月观看</div>
            </div>
          </div>

          <p><strong>是否需要 VIP 权限：</strong>{{ vipText }}</p>
          <p><strong>播放地址：</strong><a :href="movie.movieurl" target="_blank" class="movie-url">{{ movie.movieurl }}</a></p>
        </div>

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
  name: "MovieDebug",
  components: { Navigation },
  data() {
    return {
      movie: {},
      directors: [],
      actors: [],
      genres: [],
      areas: [],
      isFavorite: false, // 收藏状态
    };
  },
  computed: {
    areaName() {
      const area = this.areas.find(a => a.id === this.movie.areaId);
      return area ? area.areaname : "未知地区";
    },
    genreName() {
      const genre = this.genres.find(g => g.id === this.movie.genreId);
      return genre ? genre.type : "未知类型";
    },
    vipText() {
      if (this.movie.see === 1 || this.movie.see === true) return "是";
      if (this.movie.see === 0 || this.movie.see === false) return "否";
      return "未知";
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
    },
    handlePlay() {
      if (this.movie.movieurl) {
        window.open(this.movie.movieurl, "_blank");
      } else {
        alert("播放地址为空");
      }
    },
    toggleFavorite() {
      this.isFavorite = !this.isFavorite;
      if (this.isFavorite) {
        alert("收藏成功");
        // TODO: 调用收藏接口
      } else {
        alert("取消收藏");
        // TODO: 调用取消收藏接口
      }
    }
  },
  async created() {
    const movieId = this.$route.params.id;

    try {
      // 电影详情
      const res = await getMovies(movieId);
      if (res.code === 200 && res.data) {
        this.movie = res.data;
      }

      // 导演
      const directorIdList = await getDirectorIdsByMovie(movieId);
      if (Array.isArray(directorIdList)) {
        const details = await Promise.all(
          directorIdList.map(id =>
            getDirectors(id).then(res => res.data || res)
          )
        );
        this.directors = details;
      }

      // 演员
      const actorIdList = await getActorIdsByMovie(movieId);
      if (Array.isArray(actorIdList)) {
        const details = await Promise.all(
          actorIdList.map(id =>
            getActors(id).then(res => res.data || res)
          )
        );
        this.actors = details;
      }

      // 类型和地区列表
      const genresRes = await listGenres({pageSize: 100});
      if (genresRes.rows) {
        this.genres = genresRes.rows;
      }
      const areasRes = await listAreas({pageSize: 100});
      if (areasRes.rows) {
        this.areas = areasRes.rows;
      }
    } catch (err) {
      console.error("获取数据失败：" + err.message);
    }
  }
};
</script>

<style scoped>
.page-wrapper {
  background-color: #f6f6f6;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  color: #333;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.movie-detail-container {
  display: flex;
  max-width: 960px;
  margin: 2rem auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgb(0 0 0 / 0.1);
  padding: 20px;
}

.poster-wrapper {
  flex-shrink: 0;
  width: 280px;
  margin-right: 30px;
}

.poster {
  width: 100%;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgb(0 0 0 / 0.15);
}

.info-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.movie-title {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 8px;
  color: #222;
}

.movie-score {
  display: flex;
  align-items: baseline;
  margin-bottom: 15px;
}

.score-number {
  font-size: 2.4rem;
  font-weight: 700;
  color: #e3a21a;
  margin-right: 8px;
}

.score-count {
  font-size: 1rem;
  color: #666;
}

.movie-description {
  font-size: 1rem;
  line-height: 1.5;
  margin-bottom: 20px;
  color: #444;
}

/* 播放和收藏按钮 */
.action-buttons {
  margin: 15px 0;
  display: flex;
  gap: 15px;
}

.btn-play,
.btn-favorite {
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  font-weight: 600;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.3s;
}

.btn-play {
  background-color: #3b82f6;
  color: white;
}

.btn-play:hover {
  background-color: #2563eb;
}

.btn-favorite {
  background-color: #e5e7eb;
  color: #374151;
}

.btn-favorite.favorited {
  background-color: #fbbf24;
  color: #92400e;
}

.btn-favorite:hover {
  background-color: #d1d5db;
}

.btn-favorite.favorited:hover {
  background-color: #f59e0b;
}

.movie-meta p {
  margin: 5px 0;
  font-size: 0.9rem;
  color: #555;
}

.movie-url {
  color: #3498db;
  text-decoration: none;
}

.movie-url:hover {
  text-decoration: underline;
}

.watch-counts {
  display: flex;
  gap: 24px;
  margin: 12px 0 20px 0;
}

.watch-count-card {
  background-color: #f0f0f0;
  border-radius: 8px;
  width: 120px;
  padding: 12px 0;
  text-align: center;
  box-shadow: 0 1px 4px rgb(0 0 0 / 0.1);
  color: #333;
  user-select: none;
}

.count-number {
  font-size: 1.8rem;
  font-weight: 700;
  color: #222;
  line-height: 1.2;
}

.count-label {
  font-size: 0.85rem;
  color: #666;
  margin-top: 4px;
}

.people-section {
  margin-top: 30px;
}

.people-section h2 {
  font-weight: 600;
  margin-bottom: 10px;
  color: #222;
}

.people-list {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.person-card {
  width: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  text-decoration: none;
  color: inherit;
}

.avatar {
  width: 80px;
  height: 110px;
  border-radius: 6px;
  object-fit: cover;
  margin-bottom: 6px;
  box-shadow: 0 2px 8px rgb(0 0 0 / 0.1);
}

.person-name {
  font-size: 0.85rem;
  text-align: center;
  color: #444;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}
</style>
