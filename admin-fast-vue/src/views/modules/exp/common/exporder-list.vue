<template>
  <el-dialog
          :title="'订单列表 时间：' + this.dataForm.deliverDate"
          width="50%"
          :visible.sync="visible">
    <el-form :inline="true" @keyup.enter.native="getDataList()">
      <el-form-item>
        <div class="block">
          <el-select v-model="dataForm.deptId" placeholder="网点" clearable>
            <el-option v-for="item in deptList" :label="item.deptName" :value="item.id">{{item.deptName}}</el-option>
          </el-select>
        </div>
      </el-form-item>
      <el-form-item>
        <div class="block">
          <el-select v-model="dataForm.userId" placeholder="录单员" clearable>
            <el-option v-for="item in userList" :label="item.realname || item.username" :value="item.userId">{{item.realname || item.username}}</el-option>
          </el-select>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
            :data="dataList"
            border
            size="small"
            v-loading="dataListLoading"
            style="width: 100%;">
      <el-table-column
              prop="id"
              header-align="center"
              align="center"
              label="订单号">
      </el-table-column>
      <el-table-column
              prop="deptName"
              header-align="center"
              align="center"
              label="网点">
      </el-table-column>
      <el-table-column
              prop="realname"
              header-align="center"
              align="center"
              label="录单员">
      </el-table-column>
      <el-table-column
              prop="deliverDate"
              header-align="center"
              align="center"
              label="时间">
      </el-table-column>
      <el-table-column
              prop="settleName"
              header-align="center"
              align="center"
              label="结算方式">
      </el-table-column>
      <el-table-column
              v-if="dataForm.moneyType == 'freight'"
              prop="freight"
              header-align="center"
              align="center"
              label="运费">
      </el-table-column>
      <el-table-column
              v-if="dataForm.moneyType == 'advance'"
              prop="advance"
              header-align="center"
              align="center"

              label="垫费">
      </el-table-column>
      <el-table-column
              v-if="dataForm.moneyType == 'delivery'"
              prop="delivery"
              header-align="center"
              align="center"
              label="送货费">
      </el-table-column>
      <el-table-column
              prop="status"
              header-align="center"
              align="center"
              label="订单状态">
        <template slot-scope="scope">
          <span>{{showStatus(scope.row.status)}}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="pageIndex"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :total="totalPage"
            layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        dataForm: {
          deliverDate: '',
          moneyType: '',
          deptId: '',
          userId:''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        deptList:[],
        userList:[],
        visible:false
      }
    },
    created(){
        this.getDeptList();
        this.getUserList();
    },
    methods: {
      init(value, type){
        this.visible = true
        this.dataForm.deliverDate = value
        this.dataForm.moneyType = type
        this.dataForm.userId = ''
        this.dataForm.deptId = ''
        this.pageIndex = 1;
        this.pageSize = 10;
        this.totalPage = 0;
        this.getDataList();
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/exp/exporder/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'deptId': this.dataForm.deptId,
            'deliverDate': this.dataForm.deliverDate,
            'userId': this.dataForm.userId,
            'moneyType': this.dataForm.moneyType
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
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
      getUserList () {
        this.$http({
          url: this.$http.adornUrl('/sys/user/all'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.userList = data.list
          } else {
            this.userList = []
          }
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      showStatus(status){
        if (status == 0) {
          return '未确认'
        }
        if (status == 1) {
          return '已确认'
        }
        if (status == 0) {
          return '返单'
        }
        if (status == 3) {
          return '作废'
        }
        return ''
      }
    }
  }
</script>
