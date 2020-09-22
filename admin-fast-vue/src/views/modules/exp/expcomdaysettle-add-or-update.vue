<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="日常支出" prop="dailyExpenses">
      <el-input v-model="dataForm.dailyExpenses" placeholder="日常支出"></el-input>
    </el-form-item>
    <el-form-item label="运费收入" prop="freightIncome">
      <el-input v-model="dataForm.freightIncome" placeholder="运费收入"></el-input>
    </el-form-item>
    <el-form-item label="公司垫付" prop="comAdvance">
      <el-input v-model="dataForm.comAdvance" placeholder="公司垫付"></el-input>
    </el-form-item>
    <el-form-item label="总支出" prop="totalExpenses">
      <el-input v-model="dataForm.totalExpenses" placeholder="总支出"></el-input>
    </el-form-item>
    <el-form-item label="总收入" prop="totalIncome">
      <el-input v-model="dataForm.totalIncome" placeholder="总收入"></el-input>
    </el-form-item>
    <el-form-item label="盈利" prop="profit">
      <el-input v-model="dataForm.profit" placeholder="盈利"></el-input>
    </el-form-item>
    <el-form-item label="结算日期" prop="deliverDate">
      <el-input v-model="dataForm.deliverDate" placeholder="结算日期"></el-input>
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
        dataRule: {
          dailyExpenses: [
            { required: true, message: '日常支出不能为空', trigger: 'blur' }
          ],
          freightIncome: [
            { required: true, message: '运费收入不能为空', trigger: 'blur' }
          ],
          comAdvance: [
            { required: true, message: '公司垫付不能为空', trigger: 'blur' }
          ],
          totalExpenses: [
            { required: true, message: '总支出不能为空', trigger: 'blur' }
          ],
          totalIncome: [
            { required: true, message: '总收入不能为空', trigger: 'blur' }
          ],
          profit: [
            { required: true, message: '盈利不能为空', trigger: 'blur' }
          ],
          deliverDate: [
            { required: true, message: '结算日期不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/exp/expcomdaysettle/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.dailyExpenses = data.expComDaySettle.dailyExpenses
                this.dataForm.freightIncome = data.expComDaySettle.freightIncome
                this.dataForm.comAdvance = data.expComDaySettle.comAdvance
                this.dataForm.totalExpenses = data.expComDaySettle.totalExpenses
                this.dataForm.totalIncome = data.expComDaySettle.totalIncome
                this.dataForm.profit = data.expComDaySettle.profit
                this.dataForm.deliverDate = data.expComDaySettle.deliverDate
                this.dataForm.userId = data.expComDaySettle.userId
                this.dataForm.status = data.expComDaySettle.status
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
      }
    }
  }
</script>
