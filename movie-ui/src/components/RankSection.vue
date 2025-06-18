<template>
  <section>
    <div class="sticky-header">
      <h3>{{ title }}</h3>
    </div>
    <ul>
      <li
        v-for="movie in movies"
        :key="movie.id"
        class="movie-item"
        @click="$emit('select', movie.id)"
      >
        <img :src="movie.coverurl" alt="封面" loading="lazy" />
        <div class="movie-info">
          <h4>{{ movie.name }}</h4>
          <p>播放量: {{ movie[playKey] }}</p>
        </div>
      </li>
    </ul>
  </section>
</template>

<script>
export default {
  name: 'RankSection',
  props: {
    title: String,
    movies: Array,
    playKey: String
  }
}
</script>

<style scoped>
section {
  flex: 1;
}

.sticky-header {
  position: sticky;
  top: 64px;
  background-color: #1f2937;
  z-index: 100;
  border-bottom: 2px solid #374151;
  padding-bottom: 10px;
  margin-bottom: 15px;
}

h3 {
  font-weight: 600;
  color: #e5e7eb;
  margin: 0;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.movie-item {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
  padding: 8px;
  border-radius: 6px;
  transition: background-color 0.3s, transform 0.2s;
  cursor: pointer;
}

.movie-item:hover {
  transform: scale(1.03);
  background-color: #374151; /* 深色 hover 效果 */
}

.movie-item img {
  width: 50px;
  height: 70px;
  object-fit: cover;
  border-radius: 3px;
  box-shadow: 0 2px 6px rgb(0 0 0 / 0.1);
  margin-right: 12px;
}

.movie-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: #f3f4f6; /* 亮白，标题名 */
}

.movie-info p {
  margin: 0;
  font-size: 13px;
  color: #9ca3af; /* 浅灰，播放量 */
}
</style>
