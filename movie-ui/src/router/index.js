import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

// 公共路由
export const constantRoutes = [
  // 默认首页重定向到 /user-login
  {
    path: '/',
    redirect: '/user-login',
    hidden: true
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/home',
    component: () => import('@/views/home'),
    hidden: true
  },
  {
    path: '/movies/:id',
    name: 'MovieDetail',
    component: () => import('@/views/MovieDetail.vue'),
    hidden: true
  },
  {
    path: '/person/:type/:id',
    name: 'PersonDetail',
    component: () => import('@/views/PersonDetail.vue'),
    hidden: true
  },
  {
    path: '/ranking',
    component: () => import('@/views/Ranking.vue'),
    hidden: true
  },
  {
    path: '/user-login',
    component: () => import('@/views/user-login.vue'),
    hidden: true
  },
  {
    path: '/user-register',
    component: () => import('@/views/user-register.vue'),
    hidden: true
  },
  {
    path: '/play/:id',
    name: 'MoviePlayer',
    component: () => import('@/views/MoviePlayer.vue'),
    hidden: true
  },
  {
    path: '/category',
    component: () => import('@/views/Category'),
    hidden: true
  },
  {
    path: '/actors',
    component: () => import('@/views/actors'),
    hidden: true
  },
  {
    path: '/directors',
    component: () => import('@/views/directors'),
    hidden: true
  },
  {
    path: '/myself',
    component: () => import('@/views/myself.vue'),
    hidden: true
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/index',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/system/myself.vue-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:myself.vue:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/myself.vue' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'myself.vue/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' }
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/monitor/job/log'),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/monitor/job' }
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/tool/gen/editTable'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
      }
    ]
  }
]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push
let routerReplace = Router.prototype.replace
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
Router.prototype.replace = function replace(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
