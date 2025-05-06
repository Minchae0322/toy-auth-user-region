<template>
  <div class="loading">로그인 처리 중입니다...</div>
</template>

<script>
import router from '@/router'

export default {
  name: 'OAuth2Callback',
  mounted() {
    const query = new URLSearchParams(window.location.search)
    const token = query.get('token')

    if (token) {
      localStorage.setItem('access_token', token)
      router.replace('/dashboard') // ✅ 로그인 성공 → 대시보드로 이동
    } else {
      console.error('❌ 토큰 없음: 로그인 실패')
      router.replace('/login')
    }
  }
}
</script>

<style scoped>
.loading {
  text-align: center;
  font-size: 16px;
  margin-top: 50px;
}
</style>
