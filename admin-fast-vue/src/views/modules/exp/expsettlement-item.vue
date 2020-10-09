<template>
    <div>
        <div>
            <el-form :inline="true">
                <el-form-item>
                    <el-button @click="goBack()" type="warning">返回</el-button>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="deptId" placeholder="网点" @change="getDeptSettle">
                        <el-option v-for="item in deptList" :label="item.deptName" :value="item.id">{{item.deptName}}</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <div class="block">
                        <el-date-picker
                                v-model="deliverDate"
                                align="right"
                                type="date"
                                placeholder="选择日期"
                                value-format="yyyy-MM-dd"
                                :clearable="false"
                                @change="getDeptSettle"
                                :default-value="deliverDate"
                                :picker-options="pickerOptions">
                        </el-date-picker>
                    </div>
                </el-form-item>
            </el-form>
        </div>
        <div v-if="!dataForm.isNull">
            <el-divider content-position="left">已付订单</el-divider>
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.paidMoney}}</span>
                            <span slots="text" style="padding: 5px">已付订单总费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.paidMoneyIn}}</span>
                            <span slots="text" style="padding: 5px">已付订单已收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
            </el-row>
            <el-row>
                <el-table
                        :data="dataList"
                        border
                        size="mini"
                        style="width: 90%;">
                    <el-table-column
                            prop="realname"
                            header-align="center"
                            align="center"
                            label="录单员">
                    </el-table-column>
                    <el-table-column
                            prop="moneyAll"
                            header-align="center"
                            align="center"
                            label="应交费用">
                    </el-table-column>
                    <el-table-column
                            prop="moneyIn"
                            header-align="center"
                            align="center"
                            label="已交费用">
                    </el-table-column>
                    <el-table-column
                            prop="moneyAdd"
                            header-align="center"
                            align="center"
                            label="新增费用">
                        <template slot-scope="scope">
                            <el-input-number size="mini" v-model="scope.row.moneyAdd" :controls="false" :maxlength="8" style="width: 80px"></el-input-number>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="status"
                            header-align="center"
                            align="center"
                            label="操作">
                        <template slot-scope="scope">
                            <el-button type="text" size="mini" @click="checkUserSubmit(scope.row)">增加</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-row>
            <el-divider content-position="left">提付订单</el-divider>
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.arrivalMoney}}</span>
                            <span slots="text" style="padding: 5px">提付订单总费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.arrivalMoneyIn}}</span>
                            <span slots="text" style="padding: 5px">提付订单已收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{Number(dataForm.arrivalMoney)-Number(dataForm.arrivalMoneyIn)}}</span>
                            <span slots="text" style="padding: 5px">提付订单未收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
            </el-row>
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.comMoneyIn}}</span>
                            <span slots="text" style="padding: 5px">公司已收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{Number(dataForm.arrivalMoneyIn)-Number(dataForm.comMoneyIn)}}</span>
                            <span slots="text" style="padding: 5px">公司未收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="14" v-if="dataForm.status == 0">
                    <el-form :inline="true" :model="formInline" class="demo-form-inline" style="margin-top: 30px">
                        <el-form-item label="收到金额">
                            <el-input-number v-model="formInline.money" :controls="false" :maxlength="8" style="width: 80px"></el-input-number>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" :disabled="formInline.money == 0" @click="checkComSubmit">确定</el-button>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
            <el-divider content-position="left">月结订单</el-divider>
            <el-row style="margin: 10px 0;" :gutter="20">
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.monthMoney}}</span>
                            <span slots="text" style="padding: 5px">月结订单总费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{dataForm.arrivalMoneyIn}}</span>
                            <span slots="text" style="padding: 5px">月结订单已收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
                <el-col :span="4" style="text-align: center">
                    <van-grid :border="false" :column-num="1">
                        <van-grid-item>
                            <span slots="icon" style="font-size: 18px; padding: 5px;">￥{{Number(dataForm.arrivalMoney)-Number(dataForm.arrivalMoneyIn)}}</span>
                            <span slots="text" style="padding: 5px">月结订单未收费用</span>
                        </van-grid-item>
                    </van-grid>
                </el-col>
            </el-row>
            <el-row style="margin: 20px;text-align: center" v-if="dataForm.status == 0">
                <el-button type="danger" :disabled="!dataForm.canSubmit" @click="dataFormSubmit">结算提交</el-button>
            </el-row>
            <el-row  style="margin: 20px;text-align: center" v-else>
                <el-button type="success" disabled @click="dataFormSubmit">账单已结算</el-button>
            </el-row>
        </div>
        <div style="margin-top: 60px" v-else>
            <van-empty image="error" description="当前无订单" />
        </div>
    </div>
</template>

<script>
    import helper from '@/utils/helper'
    export default {
        data () {
            return {
                dataForm: {
                    id: 0,
                    expComDayId: '',
                    deptId: '',
                    paidMoney: '',
                    paidMoneyIn: '',
                    arrivalMoney: '',
                    arrivalMoneyIn: '',
                    comMoneyIn: '',
                    deliverDate: '',
                    monthMoney: '',
                    monthMoneyIn: '',
                    income: '',
                    userId: '',
                    status: '',
                    canSubmit:false,
                    isNull:false
                },
                deliverDate: new Date().format('yyyy-MM-dd'),
                deptId:'',
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
                deptList:[],
                formInline:{
                    money:0,
                    expDesc:''
                },
                dataList:[]
            }
        },
        created(){
            this.getDeptList();
        },
        methods: {
            init (deliverDate, deptId) {
                this.deliverDate = deliverDate
                this.deptId = deptId
                this.getDeptSettle();

            },
            getDeptSettle(){
                this.$http({
                    url: this.$http.adornUrl(`/exp/expdepdaysettle/settle`),
                    method: 'post',
                    data: this.$http.adornData({'deptId':this.deptId,'deliverDate':this.deliverDate})
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.dataForm.id = data.expDepDaySettle.id
                        this.dataForm.expComDayId = data.expDepDaySettle.expComDayId
                        this.dataForm.deptId = data.expDepDaySettle.deptId
                        this.dataForm.paidMoney = data.expDepDaySettle.paidMoney
                        this.dataForm.paidMoneyIn = data.expDepDaySettle.paidMoneyIn
                        this.dataForm.arrivalMoney = data.expDepDaySettle.arrivalMoney
                        this.dataForm.arrivalMoneyIn = data.expDepDaySettle.arrivalMoneyIn
                        this.dataForm.comMoneyIn = data.expDepDaySettle.comMoneyIn
                        this.dataForm.deliverDate = data.expDepDaySettle.deliverDate
                        this.dataForm.monthMoney = data.expDepDaySettle.monthMoney
                        this.dataForm.monthMoneyIn = data.expDepDaySettle.monthMoneyIn
                        this.dataForm.income = data.expDepDaySettle.income
                        this.dataForm.userId = data.expDepDaySettle.userId
                        this.dataForm.status = data.expDepDaySettle.status
                        this.dataForm.canSubmit = data.expDepDaySettle.canSubmit
                        this.dataForm.isNull = data.expDepDaySettle.isNull

                        this.getUserMoneyList(this.dataForm.id);
                    }
                })
            },
            getDeptList () {
                this.$http({
                    url: this.$http.adornUrl('/exp/expdepartment/all'),
                    method: 'get',
                    params: this.$http.adornParams({})
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.deptList = data.list
                    } else {
                        this.deptList = []
                    }
                })
            },
            getUserMoneyList (daySettleId) {
                this.$http({
                    url: this.$http.adornUrl('/exp/expusermoney/all/' + daySettleId),
                    method: 'get',
                    params: this.$http.adornParams({})
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.dataList = data.list
                    } else {
                        this.dataList = []
                    }
                })
            },
            checkComSubmit(){
                if (Number(this.formInline.money) + Number(this.dataForm.comMoneyIn) > Number(this.dataForm.arrivalMoneyIn)) {
                    this.$confirm(`已交费用应收费用，是否继续`, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.submitComIn();
                    }).catch(() => {
                    })
                } else {
                    this.submitComIn();
                }
            },
            submitComIn(){

                this.$http({
                    url: this.$http.adornUrl(`/exp/expdepdaysettle/update/comIn`),
                    method: 'post',
                    params: this.$http.adornParams({
                        'deliverDate': this.deliverDate,
                        'money': this.formInline.money,
                        'expDesc': this.formInline.expDesc,
                        'deptId': this.deptId,
                    })
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.getDeptSettle();
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
            checkUserSubmit(row){
                if (row.moneyAdd === 0) {
                    this.$alert('请输入金额', '系统提示', {
                    });
                    return;
                }
                if (Number(row.moneyAdd) + Number(row.moneyIn) > Number(row.moneyAll)) {
                    this.$confirm(`已交费用大于总费用，是否继续`, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.submitUserMoney(row);
                    }).catch(() => {
                    })
                } else {
                    this.submitUserMoney(row);
                }
            },
            submitUserMoney(row){
                this.$http({
                    url: this.$http.adornUrl(`/exp/expusermoney/update/money`),
                    method: 'post',
                    data: this.$http.adornData(row)
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.getDeptSettle();
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
            // 表单提交
            dataFormSubmit () {
                this.$confirm(`请确认是否提交，提交后不能再修改`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http({
                        url: this.$http.adornUrl(`/exp/expdepdaysettle/update/settle/${this.dataForm.id}`),
                        method: 'post',
                        data: this.$http.adornData({})
                    }).then(({data}) => {
                        if (data && data.code === 0) {
                            this.getDeptSettle();
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
            goBack(){
                this.$emit('closeItem')
            }
        }
    }
</script>
