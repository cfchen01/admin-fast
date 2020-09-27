<template>
  <div>
    <van-tabs @click="onClick" v-model="curTab">
      <van-tab title="按日查询">
        <van-field
                readonly
                clickable
                name="calendar"
                :value="deliverDate"
                label="选择日期"
                placeholder="点击选择日期"
                @click="showPicker = true"
        />
        <van-calendar v-model="showPicker" @confirm="onConfirm" :show-confirm="false" :min-date="minDate" :max-date="maxDate"/>
        <div v-if="dataForm.status == 1" v-loading="onLoading">
          <van-grid clickable :column-num="2">
            <van-grid-item @click="clickDayOut">
              <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-{{dataForm.totalExpenses}}</span>
              <span slots="text" style="padding: 5px">日支出金额</span>
            </van-grid-item>
            <van-grid-item @click="clickDayIn">
              <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+{{dataForm.totalIncome}}</span>
              <span slots="text" style="padding: 5px">日收入金额</span>
            </van-grid-item>
          </van-grid>
          <van-grid clickable :column-num="1">
            <van-grid-item>
              <span slots="icon" style="font-size: 20px; padding: 5px; color: red" :class="{'cla_green':Number(dataForm.profit)<0}">{{dataForm.profit}}</span>
              <span slots="text" style="padding: 5px">公司日盈利</span>
            </van-grid-item>
          </van-grid>
          <van-dialog v-model="showDay" show-cancel-button>
            <ExpensesdetailCard ref="dayCard"></ExpensesdetailCard>
          </van-dialog>
        </div>
        <div v-else style="margin-top: 60px">
          <van-empty image="error" description="当前时间尚未结算" />
        </div>
      </van-tab>
      <van-tab title="按月查询">
        <van-field
                readonly
                clickable
                name="datetimePicker"
                :value="deliverMonth"
                label="查询月份"
                @click="showMonthPicker = true"
        />
        <van-popup v-model="showMonthPicker" position="bottom">
          <van-datetime-picker
                  type="year-month"
                  title="选择年月"
                  @confirm="onConfirmMonth"
                  :min-date="minDateMonth"
                  :max-date="maxDateMonth"
                  @cancel="showMonthPicker = false"
          />
        </van-popup>
        <div v-if="dataForm.status == 1"  v-loading="onLoading">
          <van-grid clickable :column-num="2">
            <van-grid-item @click="clickMonthOut">
              <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-{{dataForm.totalExpenses}}</span>
              <span slots="text" style="padding: 5px">月支出金额</span>
            </van-grid-item>
            <van-grid-item @click="clickMonthIn">
              <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+{{dataForm.totalIncome}}</span>
              <span slots="text" style="padding: 5px">月收入金额</span>
            </van-grid-item>
          </van-grid>
          <van-grid clickable :column-num="1">
            <van-grid-item>
              <span slots="icon" style="font-size: 20px; padding: 5px; color: red" :class="{'cla_green':Number(dataForm.profit)<0}">{{dataForm.profit}}</span>
              <span slots="text" style="padding: 5px">公司月盈利</span>
            </van-grid-item>
          </van-grid>
          <van-dialog v-model="showMonth" show-cancel-button>
            <ExpensesdetailCard ref="monthCard"></ExpensesdetailCard>
          </van-dialog>
        </div>
        <div v-else style="margin-top: 60px">
          <van-empty image="error" description="当前时间尚未结算" />
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
  import ExpensesdetailCard from './common/expensesdetail-card'
  import helper from '@/utils/helper'
  import { Toast } from 'vant';
  export default {
    data () {
      return {
        curTab:0,
        showPicker:false,
        showMonthPicker:false,
        deliverMonth: new Date().format('yyyy-MM'),
        deliverDate: new Date().format('yyyy-MM-dd'),
        minDate: new Date('2020-09-01'),
        minDateMonth: new Date('2020-09-01'),
        maxDate: new Date(),
        maxDateMonth: new Date(),
        dataForm: {
          id: 0,
          dailyExpenses: '',
          freightIncome: '',
          comAdvance: '',
          totalExpenses: '',
          totalIncome: '',
          profit: '',
          deliverDate: '',
          userId: '',
          status: ''
        },
        showDay:false,
        showMonth:false,
        onLoading:false
      }
    },
    components: {
      ExpensesdetailCard,
      Toast
    },
    computed: {
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      }
    },
    created () {
      this.menuActiveName = '营收报表'
      this.getComSettle()
    },
    methods: {
      getComSettle(){
        this.onLoading = true
        var keyword = this.deliverDate
        if (this.curTab) {
          keyword = this.deliverMonth
        }
        this.$http({
          url: this.$http.adornUrl(`/exp/expcomdaysettle/detail`),
          method: 'post',
          data: this.$http.adornData({'deliverDate':keyword, 'type':this.curTab})
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataForm = data.expComDaySettle
          } else {
            this.dataForm = []
            Toast(data.msg);
          }
          this.onLoading = false
        })
      },
      onClick(name, title){
        this.curTab = name
        this.getComSettle()
        console.log(name, title)
      },
      onConfirm(date) {
        this.deliverDate = date.format('yyyy-MM-dd')
        this.showPicker = false;
        this.getComSettle()
      },
      onConfirmMonth(date) {
        this.deliverMonth = date.format('yyyy-MM')
        this.showMonthPicker = false;
        this.getComSettle()
      },
      clickDayOut(){
        this.showDay = true;
        this.$nextTick(() => {
          this.$refs.dayCard.init(this.deliverDate, 'day')
        })
      },
      clickDayIn(){
        this.$router.push({path: '/order-index',query: {status: '1', deliverDate:this.deliverDate}})
      },
      clickMonthOut(){
        this.showMonth = true;
        this.$nextTick(() => {
          this.$refs.monthCard.init(this.deliverMonth, 'month')
        })
      },
      clickMonthIn(){
        this.$router.push({path: '/order-index',query: {status: '1', deliverDate:this.deliverMonth}})
      }
    }
  }
</script>
<style>
  .cla_red{
    color: red;
  }
  .cla_green{
    color: green !important;
  }
</style>
