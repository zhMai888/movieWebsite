<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主演名字" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入主演名字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['actors:actors:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['actors:actors:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['actors:actors:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['actors:actors:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="actorsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="主演名字" align="center" prop="name" />
      <el-table-column label="简介" align="center" prop="resume" show-overflow-tooltip />
      <el-table-column label="主演照片" align="center" prop="photourl" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['actors:actors:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['actors:actors:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改演员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主演名字" prop="name">
          <el-input v-model="form.name" placeholder="请输入主演名字" />
        </el-form-item>
        <el-form-item label="简介" prop="resume">
          <el-input v-model="form.resume" placeholder="请输入简介" />
        </el-form-item>
        <el-form-item label="主演照片" prop="photourl">
          <el-input v-model="form.photourl" placeholder="请输入主演照片url" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listActors, getActors, delActors, addActors, updateActors } from "@/api/actors/actors"

export default {
  name: "Actors",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 演员表格数据
      actorsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "主演名字不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询演员列表 */
    getList() {
      this.loading = true
      listActors(this.queryParams).then(response => {
        this.actorsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        photourl: null,
        resume: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加演员"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getActors(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改演员"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateActors(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
              // 通知movies的index修改数据
              if (this.$store.state.increase_actors.moviesLoaded) {
                this.$store.commit('increase_actors/SET_INCREASE_ACTORS', 2)
              }
            })
          } else {            
            addActors(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
              // 通知movies的index新增数据
              if (this.$store.state.increase_actors.moviesLoaded) {
                this.$store.commit('increase_actors/SET_INCREASE_ACTORS', 1)
              }
            }
          )
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const rawIds = row.id || this.ids
      const ids = Array.isArray(rawIds) ? rawIds : [rawIds]
      this.$modal.confirm('是否确认删除演员编号为"' + ids + '"的数据项？').then(function() {
        return delActors(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
        // 通知movies的index删除数据
        if (this.$store.state.delete_actors.moviesLoaded) {
          this.$store.commit('delete_actors/SET_DELETED_ACTORS', ids)
        }
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {      
      this.download('actors/actors/export', {
        ...this.queryParams
      }, `actors_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
