<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="用户id" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="用户id"></el-input>
    </el-form-item>
    <el-form-item label="网点id" prop="deptId">
      <el-input v-model="dataForm.deptId" placeholder="网点id"></el-input>
    </el-form-item>
    <el-form-item label="日结id" prop="daySettleId">
      <el-input v-model="dataForm.daySettleId" placeholder="日结id"></el-input>
    </el-form-item>
    <el-form-item label="业务员应交总费用" prop="moneyAll">
      <el-input v-model="dataForm.moneyAll" placeholder="业务员应交总费用"></el-input>
    </el-form-item>
    <el-form-item label="业务员已提交费用" prop="moneyIn">
      <el-input v-model="dataForm.moneyIn" placeholder="业务员已提交费用"></el-input>
    </el-form-item>
    <el-form-item label="结算日期" prop="deliverDate">
      <el-input v-model="dataForm.deliverDate" placeholder="结算日期"></el-input>
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
          userId: '',
          deptId: '',
          daySettleId: '',
          moneyAll: '',
          moneyIn: '',
          deliverDate: ''
        },
        dataRule: {
          userId: [
            { required: true, message: '用户id不能为空', trigger: 'blur' }
          ],
          deptId: [
            { required: true, message: '网点id不能为空', trigger: 'blur' }
          ],
          daySettleId: [
            { required: true, message: '日结id不能为空', trigger: 'blur' }
          ],
          moneyAll: [
            { required: true, message: '业务员应交总费用不能为空', trigger: 'blur' }
          ],
          moneyIn: [
            { required: true, message: '业务员已提交费用不能为空', trigger: 'blur' }
          ],
          deliverDate: [
            { required: true, message: '结算日期不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/exp/expusermoney/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.userId = data.expUserMoney.userId
                this.dataForm.deptId = data.expUserMoney.deptId
                this.dataForm.daySettleId = data.expUserMoney.daySettleId
                this.dataForm.moneyAll = data.expUserMoney.moneyAll
                this.dataForm.moneyIn = data.expUserMoney.moneyIn
                this.dataForm.deliverDate = data.expUserMoney.deliverDate
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
              url: this.$http.adornUrl(`/exp/expusermoney/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'userId': this.dataForm.userId,
                'deptId': this.dataForm.deptId,
                'daySettleId': this.dataForm.daySettleId,
                'moneyAll': this.dataForm.moneyAll,
                'moneyIn': this.dataForm.moneyIn,
                'deliverDate': this.dataForm.deliverDate
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
