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
      <van-search
              v-model="phone"
              shape="round"
              background="#17B3A3"
              placeholder="请输入手机号号"
              @search="onSubmit"
      />
      <van-cell v-for="item in menuList" :title="item.name" is-link @click="clickLink(item)" />
    </div>
    <div v-else>
      <el-input placeholder="请输入订单号" v-model="ordCode">
        <el-button slot="append" @click="onSearch" icon="el-icon-search"></el-button>
      </el-input>
      <el-input placeholder="请输入手机号" v-model="phone" style="margin-top: 10px">
        <el-button slot="append" @click="onSubmit" icon="el-icon-search"></el-button>
      </el-input>
    </div>
  </div>
</template>

<script>
  export default {
    data(){
      return{
        ordCode:'',
        phone:''
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
        this.$router.push({path: '/order-add-update',query: {isView:'true',id:this.ordCode}})
      },
      onSubmit(){
        this.$router.push({path: '/order-phone',query: {phone: this.phone}})
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

