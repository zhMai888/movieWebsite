<template>
  <div class="person-detail" v-if="!loading">
    <div class="header">
      <img :src="person.photourl || defaultAvatar" alt="头像" />
      <div class="info">
        <h1>{{ person.name || '无姓名' }}</h1>
        <p class="role">{{ person.role || '' }}</p>
      </div>
    </div>

    <section class="resume-section">
      <h3>简介</h3>
      <p v-if="person.resume && person.resume.trim() !== ''">{{ person.resume }}</p>
      <p v-else class="empty">暂无简介</p>
    </section>

    <!-- 这里可以扩展更多信息展示 -->
  </div>

  <div v-else class="loading">加载中...</div>
</template>

<script>
import { getDirectors } from '@/api/directors/directors'
import { getActors } from '@/api/actors/actors'

export default {
  data() {
    return {
      person: {},
      loading: true,
      defaultAvatar: 'https://via.placeholder.com/150?text=无图'
    }
  },
  async created() {
    const {type, id} = this.$route.params

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
        this.person = {...res.data, role: this.person.role}
      } else {
        this.person = {name: '未找到人员信息', resume: '', role: ''}
      }
    } catch (e) {
      console.error(e)
      this.person = {name: '请求错误', resume: e.message, role: ''}
    } finally {
      this.loading = false
    }
  }
}
</script>

<style scoped>
.person-detail {
  max-width: 600px;
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

.resume-section h3 {
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
</style>
