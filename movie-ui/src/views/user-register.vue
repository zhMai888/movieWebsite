<template>
  <div class="register-page">
    <div class="register-container">
      <!-- 左侧预留图片区域 -->
      <div class="register-image">
        <!-- 这里放图片或者背景图 -->
        <img src="" alt="注册展示图" v-if="false" />
      </div>

      <!-- 右侧注册表单 -->
      <div class="register-form">
        <h2>注册观影斋</h2>
        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <label for="username">用户名</label>
            <input
              id="username"
              v-model.trim="username"
              type="text"
              placeholder="请输入用户名"
              required
              autocomplete="username"
            />
          </div>

          <div class="form-group">
            <label for="account">手机号或邮箱</label>
            <input
              id="account"
              v-model.trim="account"
              type="text"
              placeholder="请输入手机号或邮箱"
              required
              autocomplete="email"
            />
          </div>

          <div class="form-group">
            <label for="password">密码</label>
            <input
              id="password"
              v-model="password"
              type="password"
              placeholder="请输入密码"
              required
              autocomplete="new-password"
            />
          </div>

          <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input
              id="confirmPassword"
              v-model="confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              required
              autocomplete="new-password"
            />
          </div>

          <button type="submit" class="btn-register">注册</button>
        </form>

        <div class="login-link">
          已有账号？
          <router-link to="/user-login">登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserRegister",
  data() {
    return {
      username: "",
      account: "",
      password: "",
      confirmPassword: "",
    };
  },
  methods: {
    handleRegister() {
      if (!this.username || !this.account || !this.password || !this.confirmPassword) {
        alert("请填写完整信息");
        return;
      }
      if (this.password !== this.confirmPassword) {
        alert("两次密码输入不一致");
        return;
      }
      // 这里可以加手机号或邮箱格式校验
      alert(`注册信息：
用户名：${this.username}
账号：${this.account}
密码：${this.password}`);
    },
  },
};
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #1f2937;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

.register-container {
  display: flex;
  background: #374151;
  box-shadow: 0 2px 12px rgb(0 0 0 / 0.2);
  border-radius: 8px;
  width: 900px;
  max-width: 100%;
  overflow: hidden;
}

.register-image {
  flex: 1;
  background: #4b5563;
}

.register-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.register-form {
  flex: 1;
  padding: 60px 60px 40px;
  box-sizing: border-box;
}

.register-form h2 {
  margin-bottom: 40px;
  font-weight: 600;
  font-size: 28px;
  color: #f9fafb;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #d1d5db;
  font-size: 14px;
}

.form-group input {
  width: 100%;
  height: 40px;
  padding: 0 12px;
  border: 1px solid #9ca3af;
  border-radius: 4px;
  font-size: 15px;
  box-sizing: border-box;
  background-color: #2d3748;
  color: #f9fafb;
  transition: border-color 0.3s;
}

.form-group input::placeholder {
  color: #cbd5e1;
}

.form-group input:focus {
  border-color: #3b82f6;
  outline: none;
  background-color: #1f2937;
}

.btn-register {
  width: 100%;
  height: 42px;
  background-color: #3b82f6;
  border: none;
  border-radius: 4px;
  color: #ffffff;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-register:hover {
  background-color: #2563eb;
}

.login-link {
  margin-top: 20px;
  font-size: 14px;
  color: #d1d5db;
  text-align: center;
}

.login-link a {
  color: #3b82f6;
  font-weight: 700;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
