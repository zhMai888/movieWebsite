<template>
  <div class="login-page">
    <div class="login-container">
      <!-- 左侧预留图片区域 -->
      <div class="login-image">
        <img src="" alt="登录展示图" v-if="false" />
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form">
        <h2>登录观影斋</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="account">账号</label>
            <input
              id="account"
              v-model="account"
              type="text"
              placeholder="请输入账号"
              required
              autocomplete="username"
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
              autocomplete="current-password"
            />
          </div>

          <div class="form-group captcha-group">
            <label for="captcha">验证码</label>
            <div class="captcha-wrapper">
              <input
                id="captcha"
                v-model="captcha"
                type="text"
                placeholder="请输入验证码"
                required
              />
              <img
                :src="codeUrl"
                alt="验证码"
                class="captcha-img"
                @click="getCode"
                title="点击刷新验证码"
              />
            </div>
          </div>

          <button type="submit" class="btn-login" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <div class="register-link">
          还没有账号？
          <router-link to="/user-register">注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"; // 你的验证码接口
import { userlogin,checkCaptcha } from "@/api/users/users"; // 登录接口

export default {
  name: "UserLogin",
  data() {
    return {
      account: "",
      password: "",
      captcha: "",
      codeUrl: "",
      uuid: "",
      loading: false,
    };
  },
  created() {
    this.getCode();
  },
  methods: {
    getCode() {
      getCodeImg()
        .then((res) => {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.uuid = res.uuid;
        })
        .catch(() => {
          this.codeUrl = "";
          this.uuid = "";
        });
    },
    handleLogin() {
      this.getCode()
      if (!this.account || !this.password || !this.captcha) {
        alert("账号、密码和验证码不能为空");
        this.captcha = ""
        return;
      }
      const toCaptcha = {
        code: this.captcha,
        uuid: this.uuid,
      };
      this.loading = true;
      checkCaptcha(toCaptcha)
        .then((res) => {
          if(res===0) {
            alert("验证码错误请重新输入")
            this.captcha = ""
          }else if(res===2){
            alert("验证码已过期请重新输入")
            this.captcha = ""
          }else{
            const toLogin = {
              username: this.account,
              password: this.password
            }
            userlogin(toLogin)
              .then((res) => {
                if(res == null || res === ""){
                  alert("账号或密码错误，请重新输入")
                  this.password = ""
                  this.captcha = ""
                }else{
                  sessionStorage.setItem("userInfo", JSON.stringify(res));
                  location.replace("/home");
                }
              })
              .catch((err) => {
                alert("登录失败：" + (err.response?.data?.msg || err.message));
              })
          }
        })
        .catch((err) => {
          alert("登录失败：" + (err.response?.data?.msg || err.message));
          this.account = ""
          this.password = ""
          this.captcha = ""
        })
        .finally(() =>{
          this.loading = false;
        })
    },
  },
};
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #1f2937;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.login-container {
  display: flex;
  background: #374151;
  box-shadow: 0 2px 12px rgb(0 0 0 / 0.2);
  border-radius: 8px;
  width: 900px;
  max-width: 100%;
  overflow: hidden;
}

.login-image {
  flex: 1;
  background: #4b5563;
}

.login-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.login-form {
  flex: 1;
  padding: 60px 60px 40px;
  box-sizing: border-box;
}

.login-form h2 {
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

.btn-login {
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

.btn-login:hover:not(:disabled) {
  background-color: #2563eb;
}

.btn-login:disabled {
  cursor: not-allowed;
  background-color: #93c5fd;
}

.register-link {
  margin-top: 20px;
  font-size: 14px;
  color: #d1d5db;
  text-align: center;
}

.register-link a {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 700;
}

.register-link a:hover {
  text-decoration: underline;
}

.captcha-group .captcha-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.captcha-img {
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #9ca3af;
}
</style>
