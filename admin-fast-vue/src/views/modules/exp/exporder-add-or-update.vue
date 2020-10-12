<template>
  <div>
    <el-page-header v-if="!_isMobile()" @back="onClickLeft" :content="menuActiveName"></el-page-header>
    <van-form v-if="dataForm" validate-first @submit="dataFormSubmit">
      <van-field v-if="!isView" readonly :value="realName" label="录单人"></van-field>
      <van-field v-else readonly :value="dataForm.realname" label="录单人"></van-field>
      <van-cell-group title="托运方">
        <van-field v-model="dataForm.shipper" label="托运方" placeholder="托运方（选填）" :maxlength="50" :readonly="isView"/>
        <van-field v-model="dataForm.shipperTel" label="电话" type="digit" placeholder="电话（选填）" :maxlength="11" :readonly="isView"/>
      </van-cell-group>
      <van-cell-group title="收件方">
        <van-field v-model="dataForm.receiver" label="收件人" placeholder="收件人（选填）" :maxlength="50" :readonly="isView"/>
        <van-field v-model="dataForm.receiverTel" label="电话" type="digit" required placeholder="电话（必填）" :rules="[{ required: true, message: '请填写收货人电话' }]" :maxlength="11" :readonly="isView"/>
      </van-cell-group>
      <van-field readonly name="calendar" :value="dataForm.deliverDate" required label="发货日期" placeholder="点击选择日期" @click="selectDate"/>
      <van-calendar v-model="showCalendar" @confirm="onConfirm" :show-confirm="false" :min-date="minDate" :disabled="isView"/>
      <van-field v-model="dataForm.ordCode" label="订单码" placeholder="订单码（选填）" :maxlength="11" :readonly="isView"/>
      <van-cell-group title="货物信息">
        <van-field name="div"  label="发货地址" placeholder="发货地点" required :rules="[{ validator: deptValidator, message: '请选择发货地址' }]">
          <template #input>
            <el-select v-model="dataForm.deptId" placeholder="请选择发货地点" size="mini" :disabled="isView">
              <el-option
                      v-for="item in orderVo.deptList"
                      :key="item.id"
                      :label="item.deptName"
                      :value="item.id">
              </el-option>
            </el-select>
          </template>
        </van-field>
        <van-field name="div"  label="货物名称" placeholder="货物名称" required :rules="[{ validator: goodsValidator, message: '请选择货物名称' }]">
          <template #input>
            <el-select v-model="dataForm.goodsId" placeholder="请选择货物名称" size="mini" :disabled="isView">
              <el-option
                      v-for="item in orderVo.goodsList"
                      :key="item.id"
                      :label="item.goodsName"
                      :value="item.id">
              </el-option>
            </el-select>
          </template>
        </van-field>
        <van-field name="div"  label="包装" placeholder="包装" required :rules="[{ validator: packingValidator, message: '请选择包装' }]">
          <template #input>
            <el-select v-model="dataForm.packingId" placeholder="请选择包装" size="mini" :disabled="isView">
              <el-option
                      v-for="item in orderVo.packingList"
                      :key="item.id"
                      :label="item.packingName"
                      :value="item.id">
              </el-option>
            </el-select>
          </template>
        </van-field>
      </van-cell-group>
      <van-field required v-model="dataForm.ordNum" label="件数" placeholder="件数（必填）" type="digit" :rules="[{ required: true, message: '请填写件数' }]" :readonly="isView" :maxlength="8"/>
      <van-field v-model="dataForm.freight" label="运费" placeholder="运费（必填）" type="digit" :rules="[{ required: true, message: '请填写运费' }]" :readonly="isView" :maxlength="8"/>
      <van-field name="div"  label="结算方式" placeholder="结算方式" required :rules="[{ validator: settleValidator, message: '请选择结算方式' }]">
        <template #input>
          <el-select v-model="dataForm.settleCode" placeholder="请选择结算方式" size="mini" :disabled="isView" @change="onChange">
            <el-option
                    v-for="item in orderVo.settleList"
                    :key="item.settleCode"
                    :label="item.settleName"
                    :value="item.settleCode">
            </el-option>
          </el-select>
        </template>
      </van-field>
      <van-field v-model="dataForm.advance" label="垫付" type="digit" placeholder="垫付（选填）" :maxlength="8" :readonly="isView" :disabled="dataForm.settleCode != 'TF'"/>
      <van-field v-model="dataForm.delivery" label="送货费" type="digit" placeholder="送货费（选填）" :maxlength="8" :readonly="isView"/>
      <van-field readonly label="总费用" :value="totalMoney"/>
      <van-cell-group title="备注信息">
        <van-field v-model="dataForm.remark" label="备注" placeholder="备注（选填）" type="textarea" :maxlength="255" :readonly="isView"/>
        <van-field name="uploader" label="图片" >
          <template v-if="uploader.length == 0 && isView" #input>
            <span>无</span>
          </template>
          <template v-else #input>
            <van-uploader v-model="uploader" :max-count="3"
                          :after-read="uploadImg"
                          :before-delete="beforeDelete"
                          :max-size="2 * 1024 * 1024"
                          @oversize="onOversize"
                          :deletable="!isView" :show-upload="!isView"/>
          </template>
        </van-field>
      </van-cell-group>
      <div style="margin: 16px;" v-if="!isView">
        <van-button round block type="info" native-type="submit" size="small">
          提交
        </van-button>
      </div>
    </van-form>
    <div style="margin-top: 60px" v-else>
      <van-empty image="error" description="订单不存在" />
    </div>
  </div>
</template>

<script>
  import helper from '@/utils/helper'
  import Vue from 'vue'
  import { Toast } from 'vant';
  import axios from 'axios'
  export default {
    data () {
      return {
        visible: false,
        dataForm: {},
        orderVo:{
          settleList:[],
          packingList:[],
          goodsList:[],
          deptList:[]
        },
        showCalendar:false,
        isView:true,
        minDate: new Date(),
        uploader: [],
      }
    },
    components: {
      Toast
    },
    computed: {
      realName: {
        get () { return this.$store.state.user.nick },
        set (val) { this.$store.commit('user/updateNick', val) }
      },
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      },
      totalMoney:{
        get () {
          var value = 0;
          if (this.dataForm.delivery) {
            value = Number(value) + Number(this.dataForm.delivery)
          }
          if (this.dataForm.advance) {
            value = Number(value) + Number(this.dataForm.advance)
          }
          if (this.dataForm.freight) {
            value = Number(value) + Number(this.dataForm.freight)
          }
          return value;
        }
      }
    },
    activated(){
      this.initForm();
      this.isView = Boolean(this.$route.query.isView)
      this.init(this.$route.query.id);
      this.getOrderVo();
    },
    methods: {
      initForm(){
        this.dataForm = {
          id: 0,
          deptId: '',
          ordCode: '',
          goodsId: '',
          ordNum: '',
          packingId: '',
          freight: 0,
          advance: 0,
          settleCode: '',
          delivery: 0,
          remark: '',
          userId: '',
          shipper: '',
          shipperTel: '',
          receiver: '',
          receiverTel: '',
          status: '',
          deliverDate: new Date().format('yyyy-MM-dd'),
          fileList:[]
        }
        this.uploader = []
      },
      init (id) {
        this.dataForm.id = id || 0
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/exp/exporder/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.expOrder
              if (this.dataForm) {
                this.converImage()
              }
            }
          })
        }
      },
      getOrderVo(){
        this.$http({
          url: this.$http.adornUrl(`/exp/exporder/orderVo`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.orderVo = data.orderObjVo
          }
        })
      },
      deptValidator(value){
        if (this.dataForm.deptId) {
          return true
        }
        return false
      },
      goodsValidator(value){
        if (this.dataForm.goodsId) {
          return true
        }
        return false
      },
      packingValidator(value){
        if (this.dataForm.packingId) {
          return true
        }
        return false
      },
      settleValidator(value){
        if (this.dataForm.settleCode) {
          return true
        }
        return false
      },
      // 表单提交
      dataFormSubmit () {
        this.$http({
          url: this.$http.adornUrl(`/exp/exporder/${!this.dataForm.id ? 'save' : 'update'}`),
          method: 'post',
          data: this.$http.adornData(this.dataForm)
        }).then(({data}) => {
          if (data && data.code === 0) {
            Toast('操作成功');
            this.$router.go(-1)
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      selectDate(){
        if (!this.isView) {
          this.showCalendar = true
        }
      },
      onChange(val){
        if (val != 'TF') {
          this.dataForm.advance = 0
        }
      },
      onConfirm(date) {
        this.dataForm.deliverDate = date.format('yyyy-MM-dd')
        this.showCalendar = false;
      },
      doBack(){
        this.$router.go(-1)
      },
      onOversize(file) {
        console.log(file);
        Toast('文件大小不能超过 2M');
      },
      beforeDelete(file, detail){
        this.dataForm.fileList.splice(detail.index ,1)
        console.log(file, detail)
        console.log(this.dataForm.fileList)
        return true
      },
      uploadImg(file){
        console.log('file=',file)
        var configs = {
          headers:{'Content-Type':'multipart/form-data'}
        };
        let formData = new FormData();
        formData.append("file",file.file);
        this.$http({
          url: this.$http.adornUrl(`/exp/expfile/upload`),
          method: 'post',
          config:configs,
          data: formData
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataForm.fileList.push(data.expFile)
            this.converImage();
            Toast('上传成功');
          } else {
            this.$message.error(data.msg)
          }
        })
        // let formData = new FormData();
        // formData.append("file",file.file);
        //
        // const config = {
        //   headers: { "Content-Type":"multipart/form-data" }
        // };
        //
        // axios
        //   .post(this.$http.adornUrl(`/exp/expfile/upload`),formData,config)
        //   .then(function (response) {
        //     console.log(response);
        //   })
        //   .catch(function (error) {
        //     console.log(error);
        //   });
      },
      converImage(){
        console.log('this.dataForm.fileList=',this.dataForm.fileList)
        let $this = this
        this.uploader = []
        if (this.dataForm.fileList && this.dataForm.fileList.length > 0) {
          this.dataForm.fileList.forEach(function (item) {
            $this.uploader.push({url: process.env.VUE_APP_PROXY_TARGET + item.url})
          })
        }
        console.log('this.uploader=',this.uploader)
      },
      onClickLeft() {
        this.$router.go(-1)
      },
    }
  }
</script>
