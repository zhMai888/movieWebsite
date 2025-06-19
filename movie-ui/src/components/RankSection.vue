<template>
  <section>
    <div class="sticky-header">
      <h3>{{ title }}</h3>
    </div>
    <div class="card-list">
      <div
        v-for="movie in movies"
        :key="movie.id"
        class="card-item"
        @click="$emit('select', movie.id)"
      >
        <MovieCard :movies="[movie]" />
      </div>
    </div>
  </section>
</template>

<script>
import MovieCard from '@/components/MovieCard.vue'

export default {
  name: 'RankSection',
  components: {
    MovieCard
  },
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
  padding-bottom: 30px;
}

.sticky-header {
  position: sticky;
  top: 80px;
  background-color: #ffffff;
  z-index: 100;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

h3 {
  font-weight: 600;
  color: #1f2937;
  margin: 0;
  font-size: 20px;
}

/* 垂直列表排列容器 */
.card-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card-item {
  width: 100%;
  cursor: pointer;
}
</style>
