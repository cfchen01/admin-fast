<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="网点id" prop="deptId">
      <el-input v-model="dataForm.deptId" placeholder="网点id"></el-input>
    </el-form-item>
    <el-form-item label="订单码" prop="ordCode">
      <el-input v-model="dataForm.ordCode" placeholder="订单码"></el-input>
    </el-form-item>
    <el-form-item label="货物id" prop="goodsId">
      <el-input v-model="dataForm.goodsId" placeholder="货物id"></el-input>
    </el-form-item>
    <el-form-item label="货物件数" prop="ordNum">
      <el-input v-model="dataForm.ordNum" placeholder="货物件数"></el-input>
    </el-form-item>
    <el-form-item label="包装id" prop="packingId">
      <el-input v-model="dataForm.packingId" placeholder="包装id"></el-input>
    </el-form-item>
    <el-form-item label="运费" prop="freight">
      <el-input v-model="dataForm.freight" placeholder="运费"></el-input>
    </el-form-item>
    <el-form-item label="垫付费" prop="advance">
      <el-input v-model="dataForm.advance" placeholder="垫付费"></el-input>
    </el-form-item>
    <el-form-item label="已收垫付费" prop="advanceIn">
      <el-input v-model="dataForm.advanceIn" placeholder="已收垫付费"></el-input>
    </el-form-item>
    <el-form-item label="结算方式id" prop="settleId">
      <el-input v-model="dataForm.settleId" placeholder="结算方式id"></el-input>
    </el-form-item>
    <el-form-item label="送货费" prop="delivery">
      <el-input v-model="dataForm.delivery" placeholder="送货费"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
    </el-form-item>
    <el-form-item label="开单员" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="开单员"></el-input>
    </el-form-item>
    <el-form-item label="托运方" prop="shipper">
      <el-input v-model="dataForm.shipper" placeholder="托运方"></el-input>
    </el-form-item>
    <el-form-item label="托运方电话" prop="shipperTel">
      <el-input v-model="dataForm.shipperTel" placeholder="托运方电话"></el-input>
    </el-form-item>
    <el-form-item label="收货方" prop="receiver">
      <el-input v-model="dataForm.receiver" placeholder="收货方"></el-input>
    </el-form-item>
    <el-form-item label="收货方电话" prop="receiverTel">
      <el-input v-model="dataForm.receiverTel" placeholder="收货方电话"></el-input>
    </el-form-item>
    <el-form-item label="订单状态--0、未确认，1、已确认，2、返单" prop="status">
      <el-input v-model="dataForm.status" placeholder="订单状态--0、未确认，1、已确认，2、返单"></el-input>
    </el-form-item>
    <el-form-item label="发货日期" prop="deliverDate">
      <el-input v-model="dataForm.deliverDate" placeholder="发货日期"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
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
          deptId: '',
          ordCode: '',
          goodsId: '',
          ordNum: '',
          packingId: '',
          freight: '',
          advance: '',
          advanceIn: '',
          settleId: '',
          delivery: '',
          remark: '',
          userId: '',
          shipper: '',
          shipperTel: '',
          receiver: '',
          receiverTel: '',
          status: '',
          deliverDate: '',
          createTime: ''
        },
        dataRule: {
          deptId: [
            { required: true, message: '网点id不能为空', trigger: 'blur' }
          ],
          ordCode: [
            { required: true, message: '订单码不能为空', trigger: 'blur' }
          ],
          goodsId: [
            { required: true, message: '货物id不能为空', trigger: 'blur' }
          ],
          ordNum: [
            { required: true, message: '货物件数不能为空', trigger: 'blur' }
          ],
          packingId: [
            { required: true, message: '包装id不能为空', trigger: 'blur' }
          ],
          freight: [
            { required: true, message: '运费不能为空', trigger: 'blur' }
          ],
          advance: [
            { required: true, message: '垫付费不能为空', trigger: 'blur' }
          ],
          advanceIn: [
            { required: true, message: '已收垫付费不能为空', trigger: 'blur' }
          ],
          settleId: [
            { required: true, message: '结算方式id不能为空', trigger: 'blur' }
          ],
          delivery: [
            { required: true, message: '送货费不能为空', trigger: 'blur' }
          ],
          remark: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '开单员不能为空', trigger: 'blur' }
          ],
          shipper: [
            { required: true, message: '托运方不能为空', trigger: 'blur' }
          ],
          shipperTel: [
            { required: true, message: '托运方电话不能为空', trigger: 'blur' }
          ],
          receiver: [
            { required: true, message: '收货方不能为空', trigger: 'blur' }
          ],
          receiverTel: [
            { required: true, message: '收货方电话不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '订单状态--0、未确认，1、已确认，2、返单不能为空', trigger: 'blur' }
          ],
          deliverDate: [
            { required: true, message: '发货日期不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/exp/exporder/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.deptId = data.expOrder.deptId
                this.dataForm.ordCode = data.expOrder.ordCode
                this.dataForm.goodsId = data.expOrder.goodsId
                this.dataForm.ordNum = data.expOrder.ordNum
                this.dataForm.packingId = data.expOrder.packingId
                this.dataForm.freight = data.expOrder.freight
                this.dataForm.advance = data.expOrder.advance
                this.dataForm.advanceIn = data.expOrder.advanceIn
                this.dataForm.settleId = data.expOrder.settleId
                this.dataForm.delivery = data.expOrder.delivery
                this.dataForm.remark = data.expOrder.remark
                this.dataForm.userId = data.expOrder.userId
                this.dataForm.shipper = data.expOrder.shipper
                this.dataForm.shipperTel = data.expOrder.shipperTel
                this.dataForm.receiver = data.expOrder.receiver
                this.dataForm.receiverTel = data.expOrder.receiverTel
                this.dataForm.status = data.expOrder.status
                this.dataForm.deliverDate = data.expOrder.deliverDate
                this.dataForm.createTime = data.expOrder.createTime
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
              url: this.$http.adornUrl(`/exp/exporder/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'deptId': this.dataForm.deptId,
                'ordCode': this.dataForm.ordCode,
                'goodsId': this.dataForm.goodsId,
                'ordNum': this.dataForm.ordNum,
                'packingId': this.dataForm.packingId,
                'freight': this.dataForm.freight,
                'advance': this.dataForm.advance,
                'advanceIn': this.dataForm.advanceIn,
                'settleId': this.dataForm.settleId,
                'delivery': this.dataForm.delivery,
                'remark': this.dataForm.remark,
                'userId': this.dataForm.userId,
                'shipper': this.dataForm.shipper,
                'shipperTel': this.dataForm.shipperTel,
                'receiver': this.dataForm.receiver,
                'receiverTel': this.dataForm.receiverTel,
                'status': this.dataForm.status,
                'deliverDate': this.dataForm.deliverDate,
                'createTime': this.dataForm.createTime
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
