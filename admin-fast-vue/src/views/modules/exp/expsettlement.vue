<template>
    <div>
        <van-tabs @click="handleClick">
            <van-tab title="日查询">
                <div style="text-align: center;">
                    <el-date-picker
                            v-model="deliverDate"
                            type="date"
                            :clearable="false"
                            placeholder="选择日期"
                            value-format="yyyy-MM-dd"
                            @change="getComSettle"
                            :picker-options="pickerOptions">
                    </el-date-picker>
                </div>

                <!--            <el-row style="margin: 20px 10px">-->
                <!--                <el-col :span="3">-->
                <!--                    <div style="text-align: center; border-radius: 8px; border: 1px solid  #cccccc; padding: 16px 8px; cursor: pointer">-->
                <!--                        <div style="padding: 4px; color: green;font-size: 18px">-1234</div>-->
                <!--                        <div>支出金额</div>-->
                <!--                    </div>-->
                <!--                </el-col>-->
                <!--                <el-col :span="3">-->
                <!--                    <el-input-number v-model="dailyExpenses" :controls="false" :maxlength="8"></el-input-number>-->
                <!--                </el-col>-->
                <!--            </el-row>-->

            </van-tab>
            <van-tab title="月查询">
                <div style="text-align: center">
                    <el-date-picker
                            v-model="deliverMonth"
                            type="month"
                            :clearable="false"
                            value-format="yyyy-MM"
                            @change="getComSettle"
                            :picker-options="pickerOptions"
                            placeholder="选择月">
                    </el-date-picker>
                </div>
            </van-tab>
        </van-tabs>
        <div v-if="curTab == 0 || dataForm.status == 1"  v-loading="onLoading">
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">

<!--                    <el-popover-->
<!--                            placement="right"-->
<!--                            width="620"-->
<!--                            border-->
<!--                            trigger="click">-->
<!--                        <el-table :data="gridData">-->
<!--                            <el-table-column width="90" property="name" label="金额">-->
<!--                                <template slot-scope="scope">-->
<!--                                    <span>￥{{scope.row.name}}</span>-->
<!--                                </template>-->
<!--                            </el-table-column>-->
<!--                            <el-table-column width="150" property="date" label="时间"></el-table-column>-->
<!--                            <el-table-column width="350" property="address" label="描述"></el-table-column>-->
<!--                        </el-table>-->
                        <van-grid clickable :border="false" :column-num="1">
                            <van-grid-item  @click="clickExpenses">
                                <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-{{dataForm.dailyExpenses}}</span>
                                <span slots="text" style="padding: 5px">支出金额</span>
                            </van-grid-item>
                        </van-grid>
<!--                    </el-popover>-->
                </el-col>
                <el-col :span="18" v-if="curTab == 0 && dataForm.status == 0">
                    <el-form :inline="true" :model="formInline" class="demo-form-inline" style="margin-top: 30px">
                        <el-form-item label="支出金额">
                            <el-input-number v-model="formInline.money" :controls="false" :maxlength="8" style="width: 80px"></el-input-number>
                        </el-form-item>
                        <el-form-item label="支出描述">
                            <el-input v-model="formInline.expDesc" placeholder="支出描述" style="width: 300px"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitExpenses">确定</el-button>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">
                    <van-grid clickable :border="false" :column-num="1">
                        <van-grid-item @click="showExpOdrList('freight')">
                            <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+{{dataForm.freightIncome}}</span>
                            <span slots="text" style="padding: 5px">公司运费收入</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid clickable :border="false" :column-num="1">
                        <van-grid-item @click="showExpOdrList('advance')">
                            <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-{{dataForm.comAdvance}}</span>
                            <span slots="text" style="padding: 5px">公司垫费</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid clickable :border="false" :column-num="1">
                        <van-grid-item @click="showExpOdrList('delivery')">
                            <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+{{dataForm.comDelivery}}</span>
                            <span slots="text" style="padding: 5px">网点送货费</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
            </el-row>
            <div v-if="deptFreihtList.length > 0">
                <el-row><span style="margin-left: 20px; color: #cccccc">网点运费收入</span></el-row>
                <el-row style="margin: 10px 0;" :gutter="20">
                    <el-col :span="24">
                        <van-grid :clickable="curTab == 0" :border="false" :column-num="8">
                            <van-grid-item v-for="item in deptFreihtList">
                                <span slots="icon" style="font-size: 18px; padding: 5px; color: red" v-if="curTab == 0">+{{item.income}}</span>
                                <span slots="icon" style="font-size: 18px; padding: 5px; color: red" v-else-if="item.status == 1">+{{item.income}}</span>
                                <span slots="icon" style="font-size: 18px;padding: 5px; color: blue" v-else>0</span>
                                <span slots="text" style="padding: 5px; text-align: center">
                                    <div>{{item.deptName}}运费收入</div>
                                    <div v-if="item.status == 1" style="color: darkgray">已结算</div>
                                    <div v-else style="color: blue">未结算</div>
                                </span>
                            </van-grid-item>
                        </van-grid>
                    </el-col>
                </el-row>
            </div>
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px; color: green">-{{dataForm.totalExpenses}}</span>
                            <span slots="text" style="padding: 5px">公司支出</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px; color: red">+{{dataForm.totalIncome}}</span>
                            <span slots="text" style="padding: 5px">公司收入</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px; color: red" :class="{'cla_green':Number(dataForm.profit)<0}">{{dataForm.profit}}</span>
                            <span slots="text" style="padding: 5px">公司盈利</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
            </el-row>
            <el-row style="margin: 20px;text-align: center" v-if="dataForm.status == 0">
                <el-button type="danger" :disabled="!dataForm.canSubmit" @click="dataFormSubmit">结算提交</el-button>
            </el-row>
            <el-row  style="margin: 20px;text-align: center" v-if="curTab == 0 && dataForm.status == 1">
                <el-button type="success" disabled @click="dataFormSubmit">账单已结算</el-button>
            </el-row>
        </div>
        <div style="margin-top: 60px" v-else>
            <van-empty image="error" description="当前时间尚未结算" />
        </div>
        <van-dialog v-model="showDialog" show-cancel-button>
            <ExpensesdetailCard ref="monthCard"></ExpensesdetailCard>
        </van-dialog>
        <ExporderList v-if="exporderListShow" ref="exporderList"></ExporderList>
    </div>

</template>

<script>
    import helper from '@/utils/helper'
    import ExpensesdetailCard from './common/expensesdetail-card'
    import ExporderList from './common/exporder-list'
    import { Toast } from 'vant';
    export default {
        data () {
            return {
                deliverDate: '',
                deliverMonth: '',
                dataForm: {
                    id: 0,
                    dailyExpenses: '',
                    freightIncome: '',
                    comAdvance: '',
                    comDelivery: '',
                    totalExpenses: '',
                    totalIncome: '',
                    profit: '',
                    deliverDate: '',
                    userId: '',
                    status: '',
                    canSubmit:false
                },
                deptFreihtList:[],
                formInline:{
                    money:0,
                    expDesc:''
                },
                dailyExpenses:0,
                curTab:0,
                pickerOptions: {
                    disabledDate(time) {
                        if (time.getTime() > Date.now()) {
                            return  true
                        }
                        if (time.getTime() < new Date('2020-09-01').getTime()) {
                            return  true
                        }
                        return false
                    }
                },
                showDialog:false,
                onLoading:false,
                exporderListShow:false
            }
        },
        components: {
            Toast,
            ExpensesdetailCard,
            ExporderList
        },
        created(){
            this.deliverDate = new Date().format('yyyy-MM-dd');
            this.deliverMonth = new Date().format('yyyy-MM');
            this.getComSettle();
        },
        methods: {
            getComSettle(){
                this.onLoading = true
                var keyword = this.deliverDate
                if (this.curTab) {
                    keyword = this.deliverMonth
                }
                this.$http({
                    url: this.$http.adornUrl(`/exp/expcomdaysettle/settle`),
                    method: 'post',
                    data: this.$http.adornData({'deliverDate':keyword, 'type':this.curTab})
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.dataForm = data.expComDaySettle
                    } else {
                        this.dataForm = []
                        Toast(data.msg);
                    }
                })
                this.getDeptSettle();
            },
            getDeptSettle(){
                var keyword = this.deliverDate
                if (this.curTab) {
                    keyword = this.deliverMonth
                }
                this.$http({
                    url: this.$http.adornUrl(`/exp/expdepdaysettle/settle/list`),
                    method: 'post',
                    data: this.$http.adornData({'deliverDate':keyword, 'type':this.curTab})
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.deptFreihtList = data.list
                    } else {
                        this.deptFreihtList = []
                        Toast(data.msg);
                    }
                    this.onLoading = false
                })
            },
            // 表单提交
            dataFormSubmit () {
                this.$confirm(`请确认是否提交，提交后不能再修改`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http({
                        url: this.$http.adornUrl(`/exp/expcomdaysettle/update/settle`),
                        method: 'post',
                        data: this.$http.adornData({
                            'deliverDate': this.deliverDate
                        })
                    }).then(({data}) => {
                        if (data && data.code === 0) {
                            this.getComSettle();
                            this.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 1500,
                                onClose: () => {
                                }
                            })
                        } else {
                            this.$message.error(data.msg)
                        }
                    })
                }).catch(() => {})

            },
            handleClick(name, title) {
                this.curTab = name
                this.getComSettle()
            },
            submitExpenses(){
                if (this.formInline.money === 0) {
                    this.$alert('请输入金额', '系统提示', {
                    });
                    return;
                }
                this.$http({
                    url: this.$http.adornUrl(`/exp/expcomdaysettle/update/expenses`),
                    method: 'post',
                    params: this.$http.adornParams({
                        'deliverDate': this.deliverDate,
                        'money': this.formInline.money,
                        'expDesc': this.formInline.expDesc,
                    })
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.getComSettle();
                        this.formInline.money = 0
                        this.formInline.expDesc = ''
                        this.$message({
                            message: '操作成功',
                            type: 'success',
                            duration: 1500,
                            onClose: () => {
                            }
                        })
                    } else {
                        this.$message.error(data.msg)
                    }
                })
            },
            clickExpenses(){
                this.showDialog = true
                let keyword = this.deliverMonth
                let type = 'month'
                if (this.curTab == 0) {
                    keyword = this.deliverDate
                    type = 'day'
                }
                this.$nextTick(() => {
                    this.$refs.monthCard.init(keyword, type)
                })
            },
            showExpOdrList(type){
                let value = this.deliverDate
                if (this.curTab == 1) {
                    value = this.deliverMonth
                }
                this.exporderListShow = true;
                this.$nextTick(() => {
                    this.$refs.exporderList.init(value, type)
                })
            }
        }
    }
</script>
<style>
    .cla_green{
        color: green !important;
    }
</style>
