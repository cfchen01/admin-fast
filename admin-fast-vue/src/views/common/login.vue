<template>
  <div>
    <van-nav-bar
            title="登 录"
            style="margin-bottom: 20px;"
    />
    <van-form @submit="dataFormSubmit">
      <van-field
              v-model="dataForm.userName"
              name="用户名"
              label="用户名"
              placeholder="用户名"
              :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
              v-model="dataForm.password"
              type="password"
              name="密码"
              label="密码"
              placeholder="密码"
              :rules="[{ required: true, message: '请填写密码' }]"
      />
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit" size="small">
          登 录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
    export default {
        data () {
            return {
                dataForm: {
                    userName: '',
                    password: ''
                }
            }
        },
        methods: {
            // 提交表单
            dataFormSubmit () {
                this.$http({
                    url: this.$http.adornUrl('/sys/login'),
                    method: 'post',
                    data: this.$http.adornData({
                        'username': this.dataForm.userName,
                        'password': this.dataForm.password
                    })
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.$cookie.set('token', data.token)
                        this.$router.replace({ name: 'home' })
                        // this.$message.success("登录成功")
                    } else {
                        // this.getCaptcha()
                        this.$message.error(data.msg)
                    }
                })
            },
        }
    }
</script>
<style>
  .van-nav-bar__title{
    color: white !important;
  }
  .van-nav-bar__text{
    color: white !important;
  }
  .van-nav-bar{
    background-color: #17B3A3 !important;
  }
  .van-nav-bar .van-icon{
    color: white !important;
  }
</style>

