<template>
  <div class="login-wrapper">
    <div class="login-container">
      <h1 class="title">로그인</h1>
      <p class="subtitle">계정에 로그인하세요</p>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            type="email"
            v-model="email"
            placeholder="name@example.com"
            required
            autocomplete="email"
          />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            id="password"
            type="password"
            v-model="password"
            required
            autocomplete="current-password"
          />
        </div>

        <button type="submit" class="submit-button">로그인</button>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      </form>

      <div class="divider">또는 다음으로 계속</div>

      <button class="google-login" @click="handleSocialLogin('google')">Google로 로그인</button>
      <button class="github-login" @click="handleSocialLogin('github')">GitHub로 로그인</button>

      <div class="footer">
        계정이 없으신가요?
        <a href="/register" class="register-link">회원가입</a>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api/axios'
import router from '@/router'

export default {
  data() {
    return {
      email: '',
      password: '',
      errorMessage: ''
    }
  },
  methods: {
    async handleLogin() {
      try {
        const response = await api.post('/auth/login', {
          email: this.email,
          password: this.password
        })

        const { accessToken } = response.data
        localStorage.setItem('access_token', accessToken)

        // 로그인 성공 → 대시보드 이동
        await router.push('/dashboard')
      } catch (error) {
        this.errorMessage = '로그인 실패: 이메일 또는 비밀번호가 올바르지 않습니다.'
        console.error('로그인 오류:', error)
      }
    },
    handleSocialLogin(provider) {
      const BACKEND_BASE_URL = import.meta.env.VITE_BACKEND_BASE_URL;
      window.location.href = `${BACKEND_BASE_URL}/api/oauth2/authorization/${provider}`;
    }


  }
}
</script>

<style scoped>
.login-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
}

.login-container {
  width: 100%;
  max-width: 360px;
  padding: 40px 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
  color: #111;
}

.subtitle {
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 16px;
  text-align: left;
}

label {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
  display: block;
}

input {
  width: 100%;
  padding: 10px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
  outline: none;
  box-sizing: border-box;
  background-color: #fff;
}

input:focus {
  border-color: #333;
}

.submit-button {
  width: 100%;
  padding: 12px;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 8px;
}

.submit-button:hover {
  background-color: #222;
}

.divider {
  margin: 24px 0 16px;
  color: #999;
  font-size: 13px;
}

.google-login {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  background-color: #fff;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  color: #444;
  transition: background-color 0.2s;
}

.google-login:hover {
  background-color: #f5f5f5;
}

.footer {
  margin-top: 24px;
  font-size: 13px;
  color: #555;
}

.register-link {
  margin-left: 4px;
  color: #2ecc71;
  font-weight: 600;
  text-decoration: none;
}

.register-link:hover {
  text-decoration: underline;
}

.error-message {
  margin-top: 12px;
  font-size: 13px;
  color: red;
}

.github-login {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  background-color: #fff;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  color: #444;
  margin-top: 8px;
  transition: background-color 0.2s;
}

.github-login:hover {
  background-color: #f5f5f5;
}

</style>
