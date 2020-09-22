<template>
  <div>

    <van-nav-bar v-if="isHome"
            title="首页"
            left-text="退出"
            :right-text="userName"
            @click-left="onQuit"
    />
    <van-nav-bar v-else
            :title="menuActiveName"
            left-text="返回"
            left-arrow
            :right-text="userName"
            @click-left="onClickLeft"
    />
    <keep-alive>
      <router-view />
    </keep-alive>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        keywork:'',
        loading: true,
        isHome:true
      }
    },
    computed: {
      userName: {
        get () { return this.$store.state.user.name }
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
        console.log('退出');
      },
      onClickLeft() {
        this.$router.go(-1)
      }
    }
  }
</script>
