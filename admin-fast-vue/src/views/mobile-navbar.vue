<template>
  <div>

    <van-nav-bar v-if="isHome"
            title="首页"
            left-text="退出"
            :right-text="realName"
            @click-left="onQuit"
           @click-right="onClickRight"
    />
    <van-nav-bar v-else
            :title="menuActiveName"
            left-text="返回"
            left-arrow
            @click-left="onClickLeft"
    />
    <keep-alive>
      <router-view />
    </keep-alive>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </div>
</template>

<script>
  import { clearLoginInfo } from '@/utils'
  import UpdatePassword from './main-navbar-update-password'
  import { Dialog } from 'vant'
  export default {
    data () {
      return {
        keywork:'',
        loading: true,
        isHome:true,
        updatePassowrdVisible:false
      }
    },
    components: {
      UpdatePassword
    },
    computed: {
      userName: {
        get () { return this.$store.state.user.name }
      },
      realName: {
        get () { return this.$store.state.user.nick },
        set (val) { this.$store.commit('user/updateNick', val) }
      },
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      }
    },
    created(){
      this.checkRoute();
    },
    watch:{
      $route: function (to, from) {
        this.checkRoute();
      }
    },
    methods: {
      checkRoute(){
        if (this.$route.path == '/home')  {
          this.isHome = true
        } else {
          this.isHome = false
        }
      },
      onQuit() {
        Dialog.confirm({
          title: '系统提示',
          message: `确定退出登录?`,
        })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/logout'),
            method: 'post',
            data: this.$http.adornData()
          }).then(({data}) => {
            if (data && data.code === 0) {
              clearLoginInfo()
              this.$router.push({ name: 'login' })
            }
          })
        })
        .catch(() => {
          // on cancel
        });
      },
      onClickLeft() {
        this.$router.go(-1)
      },
      onClickRight(){
        console.log('========')
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      }
    }
  }
</script>
