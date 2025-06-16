<template>
  <header class="header">
    <div class="header-container">
      <!-- Logo和品牌名称 -->
      <div class="logo-container">
        <router-link to="/home" class="logo-link">
          <img src="@/assets/logo/logo.png" alt="观影斋" class="logo-img" />
          <span class="brand-name">观影斋</span>
        </router-link>
      </div>

      <!-- 主导航菜单 -->
      <nav class="main-nav">
        <router-link
          to="/category"
          class="nav-link"
          active-class="active-link"
        >电影分类</router-link>
        <router-link
          to="/rankings"
          class="nav-link"
          active-class="active-link"
        >电影排行</router-link>
        <router-link
          to="/actors"
          class="nav-link"
          active-class="active-link"
        >演员列表</router-link>
        <router-link
          to="/directors"
          class="nav-link"
          active-class="active-link"
        >导演列表</router-link>
      </nav>

      <!-- 搜索框 -->
      <div class="search-container" ref="searchContainer">
        <input
          ref="searchInput"
          class="search-input"
          :placeholder="searchPlaceholder"
          v-model="keyword"
          @keyup.enter="handleSearch"
          @focus="showHistory = true"
        />
        <div class="search-icon">
          <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>

        <!-- 搜索历史记录 -->
        <div
          v-if="showHistory"
          class="history-box"
          @mousedown.prevent
        >
          <div class="history-title">搜索历史</div>
          <template v-if="searchHistory.length > 0">
            <div
              v-for="(item, index) in searchHistory"
              :key="index"
              class="history-item"
              @mousedown="selectHistory(item)"
            >
              {{ item.keyword }} ({{ getTypeName(item.type) }})
            </div>
            <div class="clear-history" @mousedown="clearHistory">清除历史记录</div>
          </template>
          <div v-else class="empty-history">
            暂无搜索记录
            <div class="clear-history disabled" @mousedown="showNoHistoryAlert">清除历史记录</div>
          </div>
        </div>
      </div>

      <!-- 用户头像 -->
      <router-link to="/account" class="user-avatar">
        <img :src="userAvatar" alt="用户头像" class="avatar-image" />
      </router-link>
    </div>
  </header>
</template>

<script>
import userAvatar from '@/assets/images/profile.jpg';

// 搜索类型常量
const SEARCH_TYPE = {
  MOVIE: 'movie',
  ACTOR: 'actor',
  DIRECTOR: 'director'
};

export default {
  data() {
    return {
      keyword: "",
      showHistory: false,
      userAvatar: userAvatar,
      clickOutsideListener: null,
      searchHistory: []
    };
  },
  computed: {
    // 根据当前路由动态设置搜索框placeholder
    searchPlaceholder() {
      const route = this.$route.path;
      if (route.includes('/actors')) return '输入演员名称';
      if (route.includes('/directors')) return '输入导演名称';
      return '输入电影名称';
    },
    // 根据当前路由确定搜索类型
    currentSearchType() {
      const route = this.$route.path;
      if (route.includes('/actors')) return SEARCH_TYPE.ACTOR;
      if (route.includes('/directors')) return SEARCH_TYPE.DIRECTOR;
      return SEARCH_TYPE.MOVIE;
    }
  },
  created() {
    this.loadSearchHistory();
  },
  mounted() {
    this.clickOutsideListener = (event) => {
      if (!this.$refs.searchContainer.contains(event.target)) {
        this.showHistory = false;
      }
    };
    document.addEventListener('click', this.clickOutsideListener);
  },
  beforeDestroy() {
    if (this.clickOutsideListener) {
      document.removeEventListener('click', this.clickOutsideListener);
    }
  },
  methods: {
    // 获取类型名称用于显示
    getTypeName(type) {
      const names = {
        [SEARCH_TYPE.MOVIE]: '电影',
        [SEARCH_TYPE.ACTOR]: '演员',
        [SEARCH_TYPE.DIRECTOR]: '导演'
      };
      return names[type] || '';
    },

    handleSearch() {
      if (!this.keyword.trim()) return;

      const searchType = this.currentSearchType;
      const query = {};

      // 根据搜索类型设置不同的查询参数
      switch(searchType) {
        case SEARCH_TYPE.ACTOR:
          query.actor = this.keyword;
          break;
        case SEARCH_TYPE.DIRECTOR:
          query.director = this.keyword;
          break;
        default:
          query.search = this.keyword;
      }

      // 保存搜索记录（包含类型信息）
      this.saveSearchHistory(this.keyword, searchType);

      // 如果已经在目标页面，则只更新查询参数
      if (this.isInTargetPage(searchType)) {
        this.$router.replace({ query });
      } else {
        // 否则跳转到对应类型的默认页面
        this.navigateToSearchPage(searchType, query);
      }

      this.$refs.searchInput.blur();
      this.showHistory = false;
    },

    // 判断是否已经在目标页面
    isInTargetPage(searchType) {
      const currentPath = this.$route.path;
      switch(searchType) {
        case SEARCH_TYPE.ACTOR:
          return currentPath.includes('/actor');
        case SEARCH_TYPE.DIRECTOR:
          return currentPath.includes('/director');
        default:
          return currentPath.includes('/category') || currentPath.includes('/rankings');
      }
    },

    // 导航到对应类型的搜索页面
    navigateToSearchPage(searchType, query) {
      let path = '/category'; // 电影搜索默认到分类页
      if (searchType === SEARCH_TYPE.ACTOR) path = '/actors';
      if (searchType === SEARCH_TYPE.DIRECTOR) path = '/directors';

      this.$router.push({ path, query });
    },

    loadSearchHistory() {
      const history = localStorage.getItem('movieSearchHistory');
      this.searchHistory = history ? JSON.parse(history) : [];
    },

    saveSearchHistory(keyword, type) {
      // 去重（同时比较关键词和类型）
      let history = this.searchHistory.filter(
        item => !(item.keyword === keyword && item.type === type)
      );

      // 添加到开头
      history.unshift({ keyword, type });

      // 保留最多5条
      if (history.length > 5) {
        history = history.slice(0, 5);
      }

      this.searchHistory = history;
      localStorage.setItem('movieSearchHistory', JSON.stringify(history));
    },

    selectHistory(item) {
      this.keyword = item.keyword;
      this.handleSearch();
    },

    clearHistory() {
      this.searchHistory = [];
      localStorage.removeItem('movieSearchHistory');
      this.showHistory = false;
      this.$refs.searchInput.blur();
    },

    showNoHistoryAlert() {
      alert("没有搜索记录");
      this.$refs.searchInput.blur();
    }
  }
};
</script>

<style scoped>
/* 基础样式 */
.header {
  background-color: #1f2937;
  width: 100%;
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-container {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
}

/* Logo样式 */
.logo-container {
  display: flex;
  align-items: center;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-img {
  height: 40px;
  width: 40px;
  margin-right: 0.75rem;
}

.brand-name {
  color: #e5e7eb;
  font-size: 1.25rem;
  font-weight: 600;
}

/* 导航菜单样式 */
.main-nav {
  display: flex;
  gap: 1.5rem;
  margin: 0 auto; /* 使导航菜单居中 */
}

.nav-link {
  color: #9ca3af;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s;
}

.nav-link:hover {
  color: white;
}

/* 搜索框样式 */
.search-container {
  position: relative;
  width: 256px;
  margin-right: 1.5rem; /* 在搜索框和头像之间添加间距 */
}

.search-input {
  background-color: #374151;
  color: white;
  font-size: 0.875rem;
  border-radius: 9999px;
  width: 100%;
  padding: 0.5rem 1rem 0.5rem 2.5rem;
  border: none;
  outline: none;
}

.search-input:focus {
  box-shadow: 0 0 0 2px #3b82f6;
}

.search-icon {
  position: absolute;
  left: 0.75rem;
  top: 0.5rem;
}

.icon {
  width: 1.25rem;
  height: 1.25rem;
  color: #9ca3af;
}

/* 搜索历史框样式 */
.history-box {
  position: absolute;
  z-index: 50;
  background-color: #374151;
  font-size: 0.875rem;
  border-radius: 0.25rem;
  width: 100%;
  margin-top: 0.5rem;
  max-height: 20rem;
  overflow-y: auto;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  color: #e5e7eb;
}

.history-title {
  padding: 0.5rem;
  font-weight: 500;
  border-bottom: 1px solid #4b5563;
}

.history-item {
  padding: 0.5rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.history-item:hover {
  background-color: #4b5563;
}

.clear-history {
  padding: 0.5rem;
  color: #9ca3af;
  text-align: center;
  cursor: pointer;
  border-top: 1px solid #4b5563;
  transition: color 0.2s;
}

.clear-history:hover {
  color: #e5e7eb;
}

.empty-history {
  padding: 0.5rem;
  color: #9ca3af;
  text-align: center;
}

.disabled {
  margin-top: 6px;
  cursor: not-allowed;
  opacity: 0.7;
}

/* 用户头像样式 */
.user-avatar {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-left: auto; /* 将头像推到最右侧 */
}

.avatar-image {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover; /* 确保图片适应容器 */
  border: 2px solid #4b5563; /* 可选：添加边框 */
  transition: transform 0.2s, border-color 0.2s;
}

.avatar-image:hover {
  transform: scale(1.05); /* 悬停时轻微放大 */
  border-color: #6b7280; /* 悬停时改变边框颜色 */
}
</style>
