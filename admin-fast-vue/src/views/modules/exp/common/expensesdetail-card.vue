<template>
  <div style="max-height: 50vh; overflow-y: auto">
    <div v-if="type == 'day'" style="margin-top: 46px">
      <van-nav-bar v-if="oldType == 'month'" :title="keyword + ' 支出明细'" fixed left-text="返回" left-arrow @click-left="onClickLeft"/>
      <van-nav-bar v-else :title="keyword + ' 支出明细'" fixed />
        <van-cell v-for="item in dataList" :title="'￥'+ item.money">
          <template #extra>
            <span style="color: #999999">{{item.createTime}}</span>
          </template>
          <template #label>
            <span style="color: #999999">描述：{{item.expDesc}}</span>
          </template>
        </van-cell>
        <van-empty v-if="dataList.length == 0" description="暂无记录" />
    </div>
    <div v-else style="margin-top: 46px">
      <van-nav-bar :title="keyword + ' 支出明细'" fixed/>
        <van-cell v-for="item in dataList" :title="item.money" is-link :value="item.createDate" @click="clickLink(item)" />
        <van-empty v-if="dataList.length == 0" description="暂无记录" />
    </div>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        dataList: [],
        dataListLoading: false,
        oldKey:'',
        oldType:'',
        keyword: '',
        type: ''
      }
    },
    methods: {
      init(keyword, type){
        this.keyword = keyword;
        this.type = type;
        this.getDataList();
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/exp/expexpensesdetail/all'),
          method: 'get',
          params: this.$http.adornParams({
            'type': this.type,
            'keyword': this.keyword
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.list
          } else {
            this.dataList = []
          }
          this.dataListLoading = false
        })
      },
      onClickLeft(){
        this.type = this.oldType
        this.keyword = this.oldKey
        this.getDataList();
      },
      clickLink(item){
        this.type = 'day'
        this.oldType = 'month';
        this.oldKey = this.keyword;
        this.keyword = item.createDate;
        this.getDataList();
      }
    }
  }
</script>
<style>
  .van-empty{padding: 0}
</style>
