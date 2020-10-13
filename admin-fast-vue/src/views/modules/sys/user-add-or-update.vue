<template>
  <div>
    <div v-if="_isMobile()">
      <van-form validate-first @submit="doSubmit">
        <van-field required v-model="dataForm.userName" label="用户名" :readonly="dataForm.id != 0" name="validatorName" placeholder="用户名" :rules="[{ required: true, message: '请填写用户名' }]" />
        <van-field v-model="dataForm.password" label="密码" name="validator" type="password" placeholder="密码" :rules="[{ validator: passwordValidator, message: '请填写密码' }]" />
        <van-field v-model="dataForm.comfirmPassword" label="确认密码" name="asyncValidator" type="password" placeholder="确认密码" :rules="[{ validator: asyncValidator, message: '两次输入密码不一致' }]"/>
        <van-field v-model="dataForm.realname" label="真实姓名" placeholder="真实姓名" :rules="[{ required: true, message: '请填写真实姓名' }]"/>
        <van-field v-model="dataForm.email" label="邮箱" placeholder="邮箱"/>
        <van-field v-model="dataForm.mobile" label="手机号" placeholder="手机号"/>
        <van-field name="div"  label="角色" placeholder="角色">
          <template #input>
            <el-select v-model="dataForm.roleId" placeholder="请选择" size="mini" @change="onChange" :disabled="dataForm.id != 0">
              <el-option
                      v-for="item in roleList"
                      :key="item.roleId"
                      :label="item.roleName"
                      :value="item.roleId">
              </el-option>
            </el-select>
          </template>
        </van-field>
        <van-field name="div"  label="网点" placeholder="网点" v-if="dataForm.roleId == 3">
          <template #input>
            <el-select v-model="dataForm.deptId" placeholder="请选择" size="mini" :disabled="dataForm.id != 0">
              <el-option
                      v-for="item in deptList"
                      :key="item.id"
                      :label="item.deptName"
                      :value="item.id">
              </el-option>
            </el-select>
          </template>
        </van-field>
        <van-field name="radio" label="状态">
          <template #input>
            <van-radio-group v-model="dataForm.status" direction="horizontal">
              <van-radio :name="0">禁用</van-radio>
              <van-radio :name="1">启用</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <div style="margin: 16px;">
          <van-button round block type="info" native-type="submit" size="small">
            提交
          </van-button>
        </div>
      </van-form>
    </div>
    <div v-else>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="dataForm.userName" :readonly="this.dataForm.id != 0" placeholder="登录帐号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="comfirmPassword" :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.comfirmPassword" type="password" placeholder="确认密码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realname" >
          <el-input v-model="dataForm.realname" placeholder="真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="dataForm.mobile" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item label="角色" size="mini" prop="roleIdList">
          <el-radio-group v-model="dataForm.roleId" @change="onChange" :disabled="this.dataForm.id != 0">
            <el-radio :label="item.roleId" v-for="item in roleList">{{ item.roleName }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="网点" size="mini" prop="deptList" v-if="dataForm.roleId == 3">
          <el-radio-group v-model="dataForm.deptId" :disabled="this.dataForm.id != 0">
            <el-radio :label="item.id" v-for="item in deptList">{{ item.deptName }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" size="mini" prop="status">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item size="mini" style="text-align: center">
          <el-button @click="doBack()">取消</el-button>
          <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
        </el-form-item>
      </el-form>
    </div>

  </div>

</template>

<script>
  import { Toast } from 'vant';
  export default {
    data () {
      var validatePassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('密码不能为空'))
        } else {
          callback()
        }
      }
      var validateComfirmPassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('确认密码不能为空'))
        } else if (this.dataForm.password !== value) {
          callback(new Error('确认密码与密码输入不一致'))
        } else {
          callback()
        }
      }
      return {
        roleList: [],
        deptList: [],
        dataForm: {
          id: 0,
          userName: '',
          password: '',
          comfirmPassword: '',
          realname: '',
          salt: '',
          email: '',
          mobile: '',
          roleId: '',
          deptId:'',
          status: 1
        },
        dataRule: {
          userName: [
            { required: true, message: '用户名不能为空', trigger: 'blur' }
          ],
          password: [
            { validator: validatePassword, trigger: 'blur' }
          ],
          comfirmPassword: [
            { validator: validateComfirmPassword, trigger: 'blur' }
          ],
            realname: [
                { required: true, message: '真实姓名不能为空', trigger: 'blur' }
            ],
        },
        userRole:'',
        showPicker:false,
        columns: ['杭州', '宁波', '温州', '嘉兴', '湖州'],
      }
    },
    components: {
      Toast
    },
    activated(){
      // if (this._isMobile()) {
      this.initForm();
      this.getDeptList();
      this.init(this.$route.query.id);
      // }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.$http({
          url: this.$http.adornUrl('/sys/role/select'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.roleList = data && data.code === 0 ? data.list : []
        }).then(() => {
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/sys/user/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.userName = data.user.username
                this.dataForm.realname = data.user.realname
                this.dataForm.salt = data.user.salt
                this.dataForm.email = data.user.email
                this.dataForm.mobile = data.user.mobile
                this.dataForm.roleId = data.user.roleId
                this.dataForm.deptId = data.user.deptId
                this.dataForm.status = data.user.status
              }
            })
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
      initForm(){
        this.dataForm = {
          id: 0,
          userName: '',
          password: '',
          comfirmPassword: '',
          realname: '',
          salt: '',
          email: '',
          mobile: '',
          roleId: '',
          deptId: '',
          status: 1
        }
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.doSubmit();
          }
        })
      },
      // 表单提交
      doSubmit () {
        this.$http({
          url: this.$http.adornUrl(`/sys/user/${!this.dataForm.id ? 'save' : 'update'}`),
          method: 'post',
          data: this.$http.adornData({
            'userId': this.dataForm.id || undefined,
            'username': this.dataForm.userName,
            'realname': this.dataForm.realname,
            'password': this.dataForm.password,
            'salt': this.dataForm.salt,
            'email': this.dataForm.email,
            'mobile': this.dataForm.mobile,
            'status': this.dataForm.status,
            'deptId': this.dataForm.deptId,
            'roleId': this.dataForm.roleId
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
            this.doBack();
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      doBack(){
        this.$router.go(-1)
      },
      passwordValidator(value){
        if (!this.dataForm.id && !/\S/.test(value)) {
          return false
        } else {
          return true
        }
      },
      asyncValidator(value){
        if (!this.dataForm.id && !/\S/.test(value)) {
          return false
        } else if (this.dataForm.password !== value) {
          return false
        } else {
          return true
        }
      },
      onChange(val){
        if (val != 3) {
          this.dataForm.deptId = ''
        }
      }
    }
  }
</script>
