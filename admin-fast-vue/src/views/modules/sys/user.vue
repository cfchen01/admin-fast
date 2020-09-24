<template>
  <div>
    <div v-if="showMain" >
      <div v-if="_isMobile()">
        <van-search
                v-model="dataForm.userName"
                show-action
                placeholder="请输入搜索关键词"
                @search="getDataList()"
        >
          <template #action>
            <van-button type="primary" @click="getDataList()" size="small">查询</van-button>
            <van-button type="info" @click="addOrUpdateHandle()" size="small" style="margin-left: 10px">新增</van-button>
          </template>
        </van-search>
        <van-cell v-for="item in dataList" @click="clickItem(item)">
          <!-- 使用 title 插槽来自定义标题 -->
          <template #title>
            <span style="margin-right: 20px">{{item.realname}}</span>
          </template>
          <template #extra>
            <span style="margin-right: 20px">{{item.username}}</span>
            <van-tag v-if="item.status == 0" type="danger">禁用</van-tag>
            <van-tag v-else type="success">正常</van-tag>
          </template>
        </van-cell>
        <van-action-sheet
                v-model="isShow"
                :title="userItem.username"
                :actions="actions"
                :closeable="false"
                close-on-click-action
                @closed="userItem = {}"
                @select="onSelect"
                cancel-text="取消"
        />
        <van-pagination
                style="margin-top: 10px"
                v-model="pageIndex"
                :total-items="totalPage"
                :items-per-page="pageSize"
                force-ellipses
                @change="currentChangeHandle"
        />
      </div>
      <div v-else class="mod-user">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <el-form-item>
            <el-input v-model="dataForm.userName" placeholder="用户名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">查询</el-button>
            <el-button v-if="isAuth('sys:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('sys:user:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
          </el-form-item>
        </el-form>
        <el-table
                :data="dataList"
                border
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandle"
                style="width: 100%;">
          <el-table-column
                  type="selection"
                  header-align="center"
                  align="center"
                  width="50">
          </el-table-column>
          <el-table-column
                  prop="userId"
                  header-align="center"
                  align="center"
                  width="80"
                  label="ID">
          </el-table-column>
          <el-table-column
                  prop="username"
                  header-align="center"
                  align="center"
                  label="用户名">
          </el-table-column>
          <el-table-column
                  prop="realname"
                  header-align="center"
                  align="center"
                  label="真实姓名">
          </el-table-column>
          <el-table-column
                  prop="email"
                  header-align="center"
                  align="center"
                  label="邮箱">
          </el-table-column>
          <el-table-column
                  prop="mobile"
                  header-align="center"
                  align="center"
                  label="手机号">
          </el-table-column>
          <el-table-column
                  prop="status"
                  header-align="center"
                  align="center"
                  label="状态">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
              <el-tag v-else size="small">正常</el-tag>
            </template>
          </el-table-column>
          <el-table-column
                  prop="createTime"
                  header-align="center"
                  align="center"
                  width="180"
                  label="创建时间">
          </el-table-column>
          <el-table-column
                  header-align="center"
                  align="center"
                  width="150"
                  label="操作">
            <template slot-scope="scope">
              <el-button v-if="isAuth('sys:user:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.userId)">修改</el-button>
              <el-button v-if="isAuth('sys:user:delete')" type="text" size="small" @click="deleteHandle(scope.row.userId)">删除</el-button>
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
  </div>
</template>

<script>
  import { Dialog } from 'vant'
  export default {
    data () {
      return {
        dataForm: {
          userName: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        isShow:false,
        actions: [
          { name: '修改', color: '#3d3eee' },
          { name: '删除', color: '#ee0a24' },
        ],
        userItem:{},
        showMain:true
      }
    },
    components: {
      Dialog
    },
    computed: {
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      }
    },
    activated () {
      this.getDataList()
      this.isShow = false
      this.menuActiveName = '用户管理'
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/user/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'username': this.dataForm.userName
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
      addOrUpdateHandle (id) {
        this.$router.push({path: '/user-add-update',query: {id: id}})
      },
      // 删除
      deleteHandle (id) {
        var userIds = id ? [id] : this.dataListSelections.map(item => {
          return item.userId
        })
        this.$confirm(`确定对[id=${userIds.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteActoion(userIds)
        }).catch(() => {})
      },
      deleteActoion(userIds){
        this.$http({
          url: this.$http.adornUrl('/sys/user/delete'),
          method: 'post',
          data: this.$http.adornData(userIds, false)
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
      },
      clickItem(item){
        this.isShow = true
        this.userItem = item
      },
      onSelect(action, index){
        if (this.userItem.userId == null) {
          return;
        }
        if (index === 0) {
          this.addOrUpdateHandle(this.userItem.userId)
        } else if(index == 1) {
          var userIds = [this.userItem.userId]
          Dialog.confirm({
            title: '系统提示',
            message: `确定对[${this.userItem.realname}]删除操作?`,
          })
          .then(() => {
            this.deleteActoion(userIds)
          })
          .catch(() => {
            // on cancel
          });

        }
      }
    }
  }
</script>
