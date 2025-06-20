<template>
  <div class="person-grid">
    <div class="person-grid-inner">
      <div
        class="person-card-wrapper"
        v-for="person in people"
        :key="person.id"
      >
        <div class="person-card">
          <div class="avatar-container">
            <router-link :to="`/person/${person.type}/${person.id}`" class="avatar-link">
              <img
                class="person-avatar"
                :src="getFullAvatarPath(person.photourl)"
                :alt="person.name"
                loading="lazy"
                @error="handleImageError"
              />
              <div class="avatar-overlay">
                <span class="view-detail">查看详情</span>
              </div>
            </router-link>
          </div>

          <div class="person-info">
            <h3 class="person-name">
              <router-link :to="`/person/${person.type}/${person.id}`">
                {{ person.name }}
              </router-link>
            </h3>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    people: {
      type: Array,
      required: true,
      default: () => []
    }
  },

  data() {
    return {
      defaultAvatar: require('@/assets/images/none.png')
    }
  },
  methods: {
    getFullAvatarPath(filename) {
      if (!filename) {
        return this.defaultAvatar;
      }
      try {
        return require(`@/assets/thumbnails_done/preson_posters/${filename}`);
      } catch (error) {
        console.error('[PeopleGrid] 加载头像失败:', error); // 捕获require错误
        return this.defaultAvatar;
      }
    },
    handleImageError(event) {
      console.warn('[PeopleGrid] 图片加载失败，替换为默认头像'); // 监听网络加载错误
      event.target.src = this.defaultAvatar;
    }
  }
}
</script>

<style scoped>
/* 网格布局系统 */
.person-card-wrapper {
  padding: 1px;
  width: 20%; /* 默认5列 */
}

.person-card {
  height: 100%;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.avatar-container {
  position: relative;
  padding-top: 150%;
  overflow: hidden;
}

.person-avatar {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

/* 卡片主体样式 */
.person-card {
  height: 100%;
  min-height: 320px; /* 固定卡片高度 */
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.avatar-container {
  position: relative;
  padding-top: 133.33%; /* 统一为2:3比例 */
  overflow: hidden;
}

.person-info {
  min-height: 80px; /* 填充空白区域 */
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px;
}


/* 头像区域样式 */
.avatar-container {
  position: relative;
  padding-top: 150%;
  overflow: hidden;
}

.avatar-link {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: block;
}

.person-avatar {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.person-card:hover .person-avatar {
  transform: scale(1.05);
}

/* 头像悬停效果 */
.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 1;
}

.person-card:hover .avatar-overlay {
  opacity: 1;
}

.view-detail {
  color: white;
  font-weight: bold;
  padding: 6px 12px;
  border: 2px solid white;
  border-radius: 20px;
  font-size: 14px;
}

/* 演员信息区域（简化版） */
.person-info {
  padding: 12px;
  text-align: center;
}

.person-name {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.person-name a {
  color: #333;
  text-decoration: none;
}

.person-name a:hover {
  color: #1890ff;
}

/* 响应式设计 */
@media (max-width: 2000px) {
  .person-card-wrapper { width: 100%; }
}
@media (max-width: 1500px) {
  .person-card-wrapper { width: 100%; }
}
@media (max-width: 992px) {
  .person-card-wrapper { width: 90%; }
}
@media (max-width: 768px) {
  .person-card-wrapper { width: 80%; }
}
</style>
