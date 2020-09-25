<template>
  <div class="ord-index">
    <div v-if="_isMobile()">
      <van-cell :value="dataForm.deliverDate" />
        <ExporderCard :dataList="dataList" @updateStatus="updateStatus" @addOrUpdateHandle="addOrUpdateHandle" @viewHandle="viewHandle"></ExporderCard>
      <van-empty v-if="dataList.length == 0" description="暂无记录"></van-empty>
      <van-pagination
              v-if="dataList.length > 0"
              style="margin: 10px 0"
              v-model="pageIndex"
              :total-items="totalPage"
              :items-per-page="pageSize"
              force-ellipses
              @change="currentChangeHandle"
      />
      <van-grid v-if="isAuth('exp:exporder:resume') && dataList.length > 0" clickable :column-num="3">
        <van-grid-item>
          <span slots="icon" style="color: green">{{resumeN}}</span>
          <span slots="text" style="font-size: 12px">当天未收费用</span>
        </van-grid-item>
        <van-grid-item>
          <span slots="icon" style="color: red">{{resumeY}}</span>
          <span slots="text" style="font-size: 12px">当天已收费用</span>
        </van-grid-item>
        <van-grid-item>
          <span slots="icon">{{Number(resumeY)+Number(resumeN)}}</span>
          <span slots="text" style="font-size: 12px">当天总费用</span>
        </van-grid-item>
      </van-grid>
    </div>
    <div v-else class="mod-config">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <div class="block">
            <el-date-picker
                    v-model="dataForm.deliverDate"
                    align="right"
                    type="date"
                    placeholder="选择日期"
                    value-format="yyyy-MM-dd"
                    :clearable="false"
                    :default-value="dataForm.deliverDate"
                    :picker-options="pickerOptions">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item>
          <div class="block">
            <el-select v-model="dataForm.status" placeholder="订单状态" clearable>
              <el-option label="未确认" :value="0">未确认</el-option>
              <el-option label="已确认" :value="1">已确认</el-option>
              <el-option label="返单" :value="2">返单</el-option>
              <el-option label="作废" :value="3">作废</el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item>
          <div class="block">
            <el-select v-model="dataForm.deptId" placeholder="网点" clearable>
              <el-option v-for="item in deptList" :label="item.deptName" :value="item.id">{{item.deptName}}</el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">查询</el-button>
          <el-button type="primary" @click="addOrUpdateHandle()" v-if="isAuth('exp:exporder:edit')">新增</el-button>
<!--          <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
        </el-form-item>
      </el-form>
      <el-table
              :data="dataList"
              border
              size="small"
              v-loading="dataListLoading"
              @selection-change="selectionChangeHandle"
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
                prop="ordCode"
                header-align="center"
                align="center"
                label="订单码">
        </el-table-column>
        <el-table-column
                prop="goodsName"
                header-align="center"
                align="center"
                label="货物">
        </el-table-column>
        <el-table-column
                prop="settleName"
                header-align="center"
                align="center"
                label="结算方式">
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
        <el-table-column
                prop="deliverDate"
                header-align="center"
                align="center"
                label="发货日期">
        </el-table-column>
        <el-table-column
                header-align="center"
                align="center"
                width="180"
                label="操作">
          <template slot-scope="scope">
            <el-button v-if="isAuth('exp:exporder:status')" type="text" size="small" :disabled="scope.row.status != 0" @click="updateStatus(scope.row.id, 1)">确认</el-button>
            <el-button v-if="isAuth('exp:exporder:status')" type="text" size="small" :disabled="scope.row.status != 0" @click="updateStatus(scope.row.id, 2)">返单</el-button>
            <el-button v-if="isAuth('exp:exporder:edit')" type="text" size="small" :disabled="scope.row.status != 0" @click="updateStatus(scope.row.id, 3)">作废</el-button>
            <el-button v-if="isAuth('exp:exporder:edit')" type="text" size="small" :disabled="scope.row.status != 0" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button type="text" size="small" @click="viewHandle(scope.row.id)">详情</el-button>
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
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </div>

</template>

<script>
  import AddOrUpdate from './exporder-add-or-update'
  import ExporderCard from './common/exporder-card'
  import helper from '@/utils/helper'
  import { Dialog } from 'vant'
  import { Toast } from 'vant'
  export default {
    data () {
      return {
        dataForm: {
          status: '',
          settleCode:'',
          deliverDate: '',
          deptId: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        pickerOptions: {
          disabledDate(time)
          {
            return time.getTime() > Date.now();
          }
        },
        deptList:[],
        resumeY:0,
        resumeN:0
      }
    },
    components: {
      AddOrUpdate,
      Toast,
      Dialog,
      ExporderCard
    },
    activated () {
      this.dataForm.status = this.$route.query.status || ''
      this.dataForm.settleCode = this.$route.query.settleCode || ''
      this.dataForm.deliverDate = this.$route.query.deliverDate || new Date().format('yyyy-MM-dd')
      this.getDataList()
      this.getDeptList()
      this.getResume()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/exp/exporder/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'status': this.dataForm.status,
            'deptId': this.dataForm.deptId,
            'settleCode': this.dataForm.settleCode,
            'deliverDate': this.dataForm.deliverDate
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
      getResume () {
        this.$http({
          url: this.$http.adornUrl('/exp/exporder/resume'),
          method: 'get',
          params: this.$http.adornParams({'deliverDate': this.dataForm.deliverDate})
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.resumeY = data.value1
            this.resumeN = data.value2
          }
        })
      },
      updateStatus(id, status){
        let msg = ''
        if (status == 1) {
          msg = '确认'
        } else if (status == 2) {
          msg = '返单'
        } else if (status == 3) {
          msg = '作废'
        }
        Dialog.confirm({
          title: '系统提示',
          message: `确定对订单[${id}]进行[${msg}]操作?`,
        })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl('/exp/exporder/status'),
            method: 'post',
            params: this.$http.adornParams({
              'id': id,
              'status': status
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.getDataList();
              Toast('修改成功')
            } else {
              Toast(data.msg)
            }
          })
        })
        .catch(() => {
          // on cancel
        });
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
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      viewHandle (id) {
        this.$router.push({path: '/order-add-update', query:{isView:'1',id:id}})
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.$router.push({path: '/order-add-update', query:{id:id}})
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/exp/exporder/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
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
<style lang="scss">
  .ord-index{
    .van-grid-item__content{
      padding: 4px;
    }
    .delete-button {
      height: 100% !important;
    }
    .van-divider{
      margin: 0 !important;
    }
  }

</style>
