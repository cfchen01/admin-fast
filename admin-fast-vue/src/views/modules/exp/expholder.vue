<template>
  <div>
    <van-tabs @click="onClick" v-model="curTab">
      <van-tab title="按日查询">
        <van-field
                readonly
                clickable
                name="datetimePicker"
                :value="deliverDate"
                label="查询日期"
                @click="showPicker = true"
        />
        <van-popup v-model="showPicker" position="bottom">
          <van-datetime-picker
                  type="date"
                  title="选择日期"
                  @confirm="onConfirm"
                  :min-date="minDate"
                  :max-date="maxDate"
                  @cancel="showPicker = false"
          />
        </van-popup>
        <van-grid clickable :column-num="2">
          <van-grid-item text="路由跳转" to="/" >
            <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-22334</span>
            <span slots="text" style="padding: 5px">支出金额</span>
          </van-grid-item>
          <van-grid-item text="URL 跳转" url="/vant/mobile.html" >
            <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+7885</span>
            <span slots="text" style="padding: 5px">收入金额</span>
          </van-grid-item>
        </van-grid>
        <van-grid clickable :column-num="1">
          <van-grid-item text="路由跳转" to="/" >
            <span slots="icon" style="font-size: 20px; padding: 5px; color: green">-22334</span>
            <span slots="text" style="padding: 5px">公司盈利</span>
          </van-grid-item>
        </van-grid>
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
        <van-grid clickable :column-num="2">
          <van-grid-item text="路由跳转" to="/" >
            <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-87985</span>
            <span slots="text" style="padding: 5px">公司总支出</span>
          </van-grid-item>
          <van-grid-item text="URL 跳转" url="/vant/mobile.html" >
            <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+77888</span>
            <span slots="text" style="padding: 5px">公司总收入</span>
          </van-grid-item>
        </van-grid>
        <van-grid clickable :column-num="1">
          <van-grid-item text="路由跳转" to="/" >
            <span slots="icon" style="font-size: 20px; padding: 5px; color: green">-1123132</span>
            <span slots="text" style="padding: 5px">公司盈利</span>
          </van-grid-item>
        </van-grid>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
  import ExporderIndex from './exporderindex'
  import helper from '@/utils/helper'
  export default {
    data () {
      return {
        curTab:0,
        showPicker:false,
        showMonthPicker:false,
        deliverMonth: new Date().format('yyyy-MM'),
        deliverDate: new Date().format('yyyy-MM-dd'),
        minDate: new Date('2019-09-01'),
        minDateMonth: new Date('2019-09-01'),
        maxDate: new Date(),
        maxDateMonth: new Date()
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
      this.menuActiveName = '财务报表'
    },
    methods: {
      onClick(name, title){
        this.curTab = name
        console.log(name, title)
      },
      onConfirm(date) {
        this.deliverDate = date.format('yyyy-MM-dd')
        this.showPicker = false;
      },
      onConfirmMonth(date) {
        this.deliverMonth = date.format('yyyy-MM')
        this.showMonthPicker = false;
      },
    }
  }
</script>
