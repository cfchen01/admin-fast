<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="公司月结表id" prop="expComDayId">
      <el-input v-model="dataForm.expComDayId" placeholder="公司月结表id"></el-input>
    </el-form-item>
    <el-form-item label="网点id" prop="deptId">
      <el-input v-model="dataForm.deptId" placeholder="网点id"></el-input>
    </el-form-item>
    <el-form-item label="已付订单总费用" prop="paidMoney">
      <el-input v-model="dataForm.paidMoney" placeholder="已付订单总费用"></el-input>
    </el-form-item>
    <el-form-item label="已付订单已收费用" prop="paidMoneyIn">
      <el-input v-model="dataForm.paidMoneyIn" placeholder="已付订单已收费用"></el-input>
    </el-form-item>
    <el-form-item label="提货订单总费用" prop="arrivalMoney">
      <el-input v-model="dataForm.arrivalMoney" placeholder="提货订单总费用"></el-input>
    </el-form-item>
    <el-form-item label="提货订单已收费用" prop="arrivalMoneyIn">
      <el-input v-model="dataForm.arrivalMoneyIn" placeholder="提货订单已收费用"></el-input>
    </el-form-item>
    <el-form-item label="公司已收费用" prop="comMoneyIn">
      <el-input v-model="dataForm.comMoneyIn" placeholder="公司已收费用"></el-input>
    </el-form-item>
    <el-form-item label="发货日期(结算日期）" prop="deliverDate">
      <el-input v-model="dataForm.deliverDate" placeholder="发货日期(结算日期）"></el-input>
    </el-form-item>
    <el-form-item label="月结订单总费用" prop="monthMoney">
      <el-input v-model="dataForm.monthMoney" placeholder="月结订单总费用"></el-input>
    </el-form-item>
    <el-form-item label="月结订单已收费用" prop="monthMoneyIn">
      <el-input v-model="dataForm.monthMoneyIn" placeholder="月结订单已收费用"></el-input>
    </el-form-item>
    <el-form-item label="网点收入(运费)" prop="income">
      <el-input v-model="dataForm.income" placeholder="网点收入(运费)"></el-input>
    </el-form-item>
    <el-form-item label="结算人" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="结算人"></el-input>
    </el-form-item>
    <el-form-item label="结算状态--0、未结算，1、已结算" prop="status">
      <el-input v-model="dataForm.status" placeholder="结算状态--0、未结算，1、已结算"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
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
          status: ''
        },
        dataRule: {
          expComDayId: [
            { required: true, message: '公司月结表id不能为空', trigger: 'blur' }
          ],
          deptId: [
            { required: true, message: '网点id不能为空', trigger: 'blur' }
          ],
          paidMoney: [
            { required: true, message: '已付订单总费用不能为空', trigger: 'blur' }
          ],
          paidMoneyIn: [
            { required: true, message: '已付订单已收费用不能为空', trigger: 'blur' }
          ],
          arrivalMoney: [
            { required: true, message: '提货订单总费用不能为空', trigger: 'blur' }
          ],
          arrivalMoneyIn: [
            { required: true, message: '提货订单已收费用不能为空', trigger: 'blur' }
          ],
          comMoneyIn: [
            { required: true, message: '公司已收费用不能为空', trigger: 'blur' }
          ],
          deliverDate: [
            { required: true, message: '发货日期(结算日期）不能为空', trigger: 'blur' }
          ],
          monthMoney: [
            { required: true, message: '月结订单总费用不能为空', trigger: 'blur' }
          ],
          monthMoneyIn: [
            { required: true, message: '月结订单已收费用不能为空', trigger: 'blur' }
          ],
          income: [
            { required: true, message: '网点收入(运费)不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '结算人不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '结算状态--0、未结算，1、已结算不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/exp/expdepdaysettle/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
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
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/exp/expdepdaysettle/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'expComDayId': this.dataForm.expComDayId,
                'deptId': this.dataForm.deptId,
                'paidMoney': this.dataForm.paidMoney,
                'paidMoneyIn': this.dataForm.paidMoneyIn,
                'arrivalMoney': this.dataForm.arrivalMoney,
                'arrivalMoneyIn': this.dataForm.arrivalMoneyIn,
                'comMoneyIn': this.dataForm.comMoneyIn,
                'deliverDate': this.dataForm.deliverDate,
                'monthMoney': this.dataForm.monthMoney,
                'monthMoneyIn': this.dataForm.monthMoneyIn,
                'income': this.dataForm.income,
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
      }
    }
  }
</script>
