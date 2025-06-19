<template>
  <div>
    <Navigation />
    <div class="person-detail" v-if="!loading">
      <!-- 头部信息 -->
      <div class="header">
        <img :src="getFullPhotoPath(person.photourl) || defaultAvatar" alt="头像" />
        <div class="info">
          <h1>{{ person.name || '无姓名' }}</h1>
          <p class="role">{{ person.role || '' }}</p>
        </div>
      </div>

      <!-- 简介部分 -->
      <section class="resume-section">
        <h3>简介</h3>
        <p v-if="person.resume && person.resume.trim() !== ''">{{ person.resume }}</p>
        <p v-else class="empty">暂无简介</p>
      </section>

      <!-- 参与电影展示 -->
      <section class="related-movies" v-if="relatedMovies.length">
        <h3>参与的电影</h3>
        <MovieCard :movies="relatedMovies" />
      </section>
    </div>

    <div v-else class="loading">加载中...</div>
  </div>
</template>

<script>
import Navigation from '@/components/Navigation.vue'  // 导入导航组件
import { getDirectors } from '@/api/directors/directors'
import { getActors } from '@/api/actors/actors'
import { getMovieIdsByActor, getMovieIdsByDirector, getMovies } from '@/api/movies/movies'
import { getGenres } from '@/api/genres/genres'
import MovieCard from '@/components/MovieCard.vue'

export default {
  components: {
    Navigation,
    MovieCard
  },
  data() {
    return {
      person: {},
      loading: true,
      defaultAvatar: 'https://via.placeholder.com/150?text=无图',
      relatedMovieIds: [],
      relatedMovies: [],
      genreCache: {}
    }
  },
  methods: {
    getFullPhotoPath(filename) {
      if (!filename) return null
      try {
        return require(`@/assets/thumbnails_done/preson_posters/${filename}`)
      } catch (e) {
        return `/assets/thumbnails_done/preson_posters/${filename}`
      }
    },
    async fetchGenreName(genreId) {
      if (!genreId) return '未知类型'
      if (this.genreCache[genreId]) {
        return this.genreCache[genreId]
      }
      try {
        const res = await getGenres(genreId)
        if (res.code === 200 && res.data) {
          this.genreCache[genreId] = res.data.type
          return res.data.type
        }
      } catch (e) {
        console.error(`获取类型 ${genreId} 失败`, e)
      }
      return '未知类型'
    }
  },
  async created() {
    const { type, id } = this.$route.params

    try {
      let res
      if (type === 'director') {
        res = await getDirectors(id)
        this.person.role = '导演'
      } else if (type === 'actor') {
        res = await getActors(id)
        this.person.role = '演员'
      } else {
        throw new Error('未知人员类型')
      }

      if (res.code === 200) {
        this.person = { ...res.data, role: this.person.role }

        let movieIdsRes = null
        if (type === 'director') {
          movieIdsRes = await getMovieIdsByDirector(id)
        } else if (type === 'actor') {
          movieIdsRes = await getMovieIdsByActor(id)
        }

        if (movieIdsRes) {
          if (Array.isArray(movieIdsRes)) {
            this.relatedMovieIds = movieIdsRes
          } else if (movieIdsRes.code === 200 && Array.isArray(movieIdsRes.data)) {
            this.relatedMovieIds = movieIdsRes.data
          } else {
            console.warn('接口返回格式异常:', movieIdsRes)
            this.relatedMovieIds = []
          }
        }

        const movieDetails = []
        for (const mid of this.relatedMovieIds) {
          try {
            const movieRes = await getMovies(mid)
            if (movieRes.code === 200) {
              const movie = movieRes.data
              movie.type = await this.fetchGenreName(movie.genreId)
              movieDetails.push(movie)
            }
          } catch (e) {
            console.warn(`获取电影 ${mid} 失败`, e)
          }
        }
        this.relatedMovies = movieDetails
      } else {
        this.person = { name: '未找到人员信息', resume: '', role: '' }
      }
    } catch (e) {
      console.error('请求错误:', e)
      this.person = { name: '请求错误', resume: e.message, role: '' }
    } finally {
      this.loading = false
    }
  }
}
</script>

<style scoped>
.person-detail {
  max-width: 1000px;
  margin: 40px auto;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  color: #333;
  background: #fff;
  border-radius: 8px;
  padding: 20px 30px;
  box-shadow: 0 2px 8px rgb(0 0 0 / 0.1);
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.header img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  box-shadow: 0 2px 8px rgb(0 0 0 / 0.15);
  margin-right: 25px;
}

.info h1 {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 6px;
}

.role {
  font-size: 16px;
  color: #888;
  font-weight: 500;
}

.resume-section h3,
.related-movies h3 {
  font-size: 20px;
  font-weight: 600;
  border-bottom: 2px solid #eee;
  padding-bottom: 8px;
  margin-bottom: 12px;
}

.resume-section p {
  font-size: 16px;
  line-height: 1.6;
  color: #555;
  white-space: pre-wrap;
}

.empty {
  color: #bbb;
  font-style: italic;
}

.loading {
  max-width: 600px;
  margin: 40px auto;
  text-align: center;
  font-size: 18px;
  color: #666;
}

.related-movies {
  margin-top: 40px;
}
</style>
