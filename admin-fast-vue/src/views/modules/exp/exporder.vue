<template>
  <div v-if="_isMobile()">
    <van-form @submit="onSubmit">
      <van-field name="div" label="订单状态" placeholder="订单状态">
        <template #input>
          <el-select v-model="dataForm.status" placeholder="订单状态" size="mini" clearable>
            <el-option label="未确认" :value="0">未确认</el-option>
            <el-option label="已确认" :value="1">已确认</el-option>
            <el-option label="返单" :value="2">返单</el-option>
            <el-option label="作废" :value="3">作废</el-option>
          </el-select>
        </template>
      </van-field>
      <van-field name="div" label="结算方式" placeholder="结算方式">
        <template #input>
          <el-select v-model="dataForm.settleCode" placeholder="请选择" size="mini" clearable>
            <el-option
                    v-for="item in settleList"
                    :key="item.settleCode"
                    :label="item.settleName"
                    :value="item.settleCode">
            </el-option>
          </el-select>
        </template>
      </van-field>
      <van-field
              readonly
              clickable
              name="calendar"
              :value="dataForm.deliverDate"
              label="发货日期"
              placeholder="点击选择日期"
              @click="showCalendar = true"
      />
      <van-calendar v-model="showCalendar" @confirm="onConfirm" :show-confirm="false" :min-date="minDate" :max-date="maxDate"/>
      <div style="margin: 20px;">
        <van-button round block type="info" size="small" native-type="submit">
          查询
        </van-button>
      </div>
    </van-form>
    <div style="margin: 20px;" v-if="isAuth('exp:exporder:edit')">
      <van-button round block type="primary" size="small" @click="addOrder()">
        录单
      </van-button>
    </div>
  </div>
  <div v-else>
    <exporder-index/>
  </div>
</template>

<script>
  import ExporderIndex from './exporderindex'
  import helper from '@/utils/helper'
  export default {
    data () {
      return {
        showCalendar:false,
        settleList:[],
        dataForm: {
          status: '',
          settleCode:'',
          deliverDate: new Date().format('yyyy-MM-dd')
        },
        minDate: new Date('2020-09-01'),
        maxDate: new Date()
      }
    },
    components: {
      ExporderIndex
    },
    computed: {
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      }
    },
    created () {
      this.getSettleList()
      this.menuActiveName = '订单管理'
    },
    methods: {
      getSettleList () {
        this.$http({
          url: this.$http.adornUrl('/exp/expsettle/all'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.settleList = data.list
          } else {
            this.settleList = []
          }
        })
      },
      onConfirm(date) {
        this.dataForm.deliverDate = date.format('yyyy-MM-dd')
        this.showCalendar = false;
      },
      onSubmit(){
        this.$router.push({path: '/order-index',query: {status: this.dataForm.status, deliverDate:this.dataForm.deliverDate,settleCode:this.dataForm.settleCode}})
      },
      addOrder(){
        this.$router.push({path: '/order-add-update'})
      }
    }
  }
</script>
