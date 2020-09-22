<template>
  <div class="mod-home">
    <div v-if="_isMobile()">
      <van-search
              v-model="ordCode"
              shape="round"
              background="#17B3A3"
              placeholder="请输入订单号"
              @search="onSearch"
      />
      <van-cell v-for="item in menuList" :title="item.name" is-link @click="clickLink(item)" />
    </div>
    <el-input v-else placeholder="请输入订单号" v-model="ordCode">
      <el-button slot="append" @click="onSearch" icon="el-icon-search"></el-button>
    </el-input>

  </div>
</template>

<script>
  export default {
    data(){
      return{
        ordCode:''
      }
    },
    computed: {
      userName: {
        get () { return this.$store.state.user.name }
      },
      menuList: {
        get () { return this.$store.state.common.menuList },
        set (val) { this.$store.commit('common/updateMenuList', val) }
      },
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      }
    },
    created(){
      this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
    },
    methods:{
      onSearch(){
        this.$http({
          url: this.$http.adornUrl('/sys/menu/select'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
        })
      },
      clickLink(item){
        if (item == null || item.url == null) {
          return '/home'
        }
        this.menuActiveName = item.name
        console.log('this.menuActiveName=',this.menuActiveName)
        this.$router.push(item.url.replace('/', '-'))
      }
    }
  }
</script>

<style>
  .mod-home {
    line-height: 1.5;
  }
</style>

