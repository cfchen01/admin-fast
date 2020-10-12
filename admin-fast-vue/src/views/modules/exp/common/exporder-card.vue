<template>
  <div>
    <van-swipe-cell v-for="item in dataList">
      <van-card
              :num="item.ordNum"
              :price="Number(item.advance)+Number(item.freight)+Number(item.delivery)"
              :desc="'备注：'+item.remark"
              @click="viewHandle(item.id)"
      >
        <template #title>
          <div>
            <span>订单号：{{item.id}}</span>
            <span style="float: right">{{item.receiver + '/' + item.receiverTel}}</span>
          </div>
        </template>
        <template #num>
          <div>
            <span>{{item.goodsName}} x{{item.ordNum}}</span>
          </div>
        </template>
        <template #price>
          <div>
            <span>总费用￥{{Number(item.advance)+Number(item.freight)+Number(item.delivery)}} 其中运费￥{{Number(item.delivery)}}</span>
          </div>
        </template>
        <template #tags>
          <div>
              <span>
                <span style="margin-right: 5px">{{item.deliverDate}}</span>
                <van-tag plain v-if="item.status == 0" type="warning">未确认</van-tag>
                <van-tag plain v-else-if="item.status == 1" type="success">已确认</van-tag>
                <van-tag plain v-else-if="item.status == 2" type="danger">返单</van-tag>
                <van-tag plain v-else="item.status == 3" type="danger">作废</van-tag>
              </span>
            <span style="float: right" v-if="isAuth('exp:exporder:status')">
                <van-button size="mini" type="danger" :disabled="item.status != 0" @click="updateStatus(item.id, 2)">返单</van-button>
                <van-button size="mini" type="info" :disabled="item.status != 0" @click="updateStatus(item.id, 1)">确认</van-button>
              </span>
          </div>
        </template>
        <!--          <template #footer>-->
        <!--            <van-button size="mini" type="danger">返单</van-button>-->
        <!--            <van-button size="mini" type="info">确认</van-button>-->
        <!--          </template>-->
      </van-card>
      <template #right v-if="isAuth('exp:exporder:edit')">
        <van-button square text="修改" type="info" class="delete-button" :disabled="item.status != 0" @click="addOrUpdateHandle(item.id)"/>
        <van-button square text="作废" type="warning" class="delete-button" :disabled="item.status != 0" @click="updateStatus(item.id, 3)"/>
      </template>
      <van-divider />
    </van-swipe-cell>
  </div>
</template>

<script>
  export default {
    data () {
      return {
      }
    },
    props:{
      dataList:Array
    },
    methods: {
      updateStatus(id, status){
        this.$emit('updateStatus', id, status)
      },
      addOrUpdateHandle(id){
        this.$emit('addOrUpdateHandle', id)
      }
      ,viewHandle(id){
        this.$emit('viewHandle', id)
      }
    }
  }
</script>
<style>
  .van-card__content{
    min-height:0!important;
  }
</style>
