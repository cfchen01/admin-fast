<template>
  <div class="ord-index">
    <div v-if="_isMobile()">
      <van-cell :value="dataForm.phone" />
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
    </div>
    <div v-else class="mod-config">
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
                prop="realname"
                header-align="center"
                align="center"
                label="录单员">
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
                prop="receiverTel"
                header-align="center"
                align="center"
                label="收件人手机">
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
    </div>
  </div>

</template>

<script>
  import ExporderCard from './common/exporder-card'
  import helper from '@/utils/helper'
  import { Dialog } from 'vant'
  import { Toast } from 'vant'
  export default {
    data () {
      return {
        dataForm: {
          phone:''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
      }
    },
    components: {
      Toast,
      Dialog,
      ExporderCard
    },
    activated () {
      if (this.$route.query.phone != null) {
        this.dataForm.phone = this.$route.query.phone
      }
      this.getDataList()
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
            'phone': this.dataForm.phone
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
      showStatus(status){
        if (status == 0) {
          return '未确认'
        }
        if (status == 1) {
          return '已确认'
        }
        if (status == 2) {
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
