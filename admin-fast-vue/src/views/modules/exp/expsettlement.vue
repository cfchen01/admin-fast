<template>
    <el-tabs v-model="curTab" type="card" @tab-click="handleClick">
        <el-tab-pane label="日查询" name="first">
            <el-date-picker
                    v-model="deliverDate"
                    align="right"
                    type="date"
                    :clearable="false"
                    placeholder="选择日期"
                    :picker-options="pickerOptions">
            </el-date-picker>
        </el-tab-pane>
        <el-tab-pane label="月查询" name="second">
            <el-date-picker
                    v-model="deliverMonth"
                    type="month"
                    :clearable="false"
                    placeholder="选择月">
            </el-date-picker>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
    import helper from '@/utils/helper'
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
                    totalExpenses: '',
                    totalIncome: '',
                    profit: '',
                    deliverDate: '',
                    userId: '',
                    status: ''
                },
                curTab:'first',
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                    }
                }
            }
        },
        created(){
            this.deliverDate = new Date().format('yyyy-MM-dd');
            this.deliverMonth = new Date().format('yyyy-MM');
            this.getDetail();
        },
        methods: {
            getDetail () {
                this.$http({
                    url: this.$http.adornUrl(`/exp/expcomdaysettle/info/${this.dataForm.id}`),
                    method: 'get',
                    params: this.$http.adornParams()
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.dataForm = data.expComDaySettle
                    }
                })
            },
            // 表单提交
            dataFormSubmit () {
                this.$refs['dataForm'].validate((valid) => {
                    if (valid) {
                        this.$http({
                            url: this.$http.adornUrl(`/exp/expcomdaysettle/${!this.dataForm.id ? 'save' : 'update'}`),
                            method: 'post',
                            data: this.$http.adornData({
                                'id': this.dataForm.id || undefined,
                                'dailyExpenses': this.dataForm.dailyExpenses,
                                'freightIncome': this.dataForm.freightIncome,
                                'comAdvance': this.dataForm.comAdvance,
                                'totalExpenses': this.dataForm.totalExpenses,
                                'totalIncome': this.dataForm.totalIncome,
                                'profit': this.dataForm.profit,
                                'deliverDate': this.dataForm.deliverDate,
                                'userId': this.dataForm.userId,
                                'status': this.dataForm.status
                            })
                        }).then(({data}) => {
                            if (data && data.code === 0) {
                                this.$message({
                                    message: '操作成功',
                                    type: 'success',
                                    duration: 1500,
                                    onClose: () => {
                                        this.visible = false
                                        this.$emit('refreshDataList')
                                    }
                                })
                            } else {
                                this.$message.error(data.msg)
                            }
                        })
                    }
                })
            },
            handleClick(tab, event) {
                this.curTab = tab.name
                console.log('this.curTab',this.curTab);
            }
        }
    }
</script>
