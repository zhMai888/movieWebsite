<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="电影名字" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入电影名字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上映年份" prop="releaseYear">
        <el-select v-model="queryParams.releaseDate" placeholder="请选择年份" clearable>
          <el-option label="未知" :value="-1" />
          <el-option
            v-for="year in yearOptions"
            :key="year"
            :label="year"
            :value="year"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="主演" prop="actors">
        <el-select
          v-model="formTop.actors"
          filterable
          multiple
          placeholder="输入搜索主演"
        >
          <el-option
            v-for="item in Actors"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="导演" prop="directors">
        <el-select
          v-model="formTop.directors"
          filterable
          multiple
          placeholder="输入搜索导演"
        >
          <el-option
            v-for="item in Directors"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="电影地区" prop="areaId">
        <el-select v-model="queryParams.areaId" placeholder="请选择电影地区" clearable>
          <el-option
            v-for="area in areas"
            :key="area.value"
            :label="area.label"
            :value="area.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="电影类型" prop="genreId">
        <el-select v-model="queryParams.genreId" placeholder="请选择电影类型" clearable>
          <el-option
            v-for="genre in genres"
            :key="genre.value"
            :label="genre.label"
            :value="genre.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="观看次数" prop="count">
        <el-input
          v-model="top.count"
          placeholder="逗号隔开范围"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本周观看次数" prop="weekcount">
        <el-input
          v-model="top.weekcount"
          placeholder="逗号隔开范围"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月观看次数" prop="monthcount">
        <el-input
          v-model="top.monthcount"
          placeholder="逗号隔开范围"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="vip" prop="see">
        <el-select v-model="queryParams.see" placeholder="请选择vip" clearable>
          <el-option
            v-for="dict in dict.type.is_vip"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['movies:movies:add']"
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
          v-hasPermi="['movies:movies:edit']"
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
          v-hasPermi="['movies:movies:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="exportDialogVisible = true"
          v-hasPermi="['movies:movies:export']"
        >导出</el-button>
        <el-dialog
          title="选择导出类型"
          :visible.sync="exportDialogVisible"
          width="30%"
          class="export-dialog"
        >
          <el-radio-group v-model="exportType">
            <p class="radio-item"><el-radio label="movie"><span style="font-size: 20px;">电影表单</span></el-radio></p>
            <p class="radio-item"><el-radio label="play"><span style="font-size: 20px;">电影播放总表单</span></el-radio></p>
            <p class="radio-item"><el-radio label="month"><span style="font-size: 20px;">电影月播放表单</span></el-radio></p>
            <p class="radio-item"><el-radio label="week"><span style="font-size: 20px;">电影周播放表单</span></el-radio></p>
            <p class="radio-item"><el-radio label="score"><span style="font-size: 20px;">电影评分表单</span></el-radio></p>
          </el-radio-group>

          <span slot="footer" class="dialog-footer">
            <el-button @click="exportDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleExport">确认导出</el-button>
          </span>
        </el-dialog>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="moviesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="电影名字" align="center" prop="name" />
      <el-table-column label="上映年份" align="center" prop="releaseYear">
        <template slot-scope="scope">
          {{ scope.row.releaseDate || '未知' }}
        </template>
      </el-table-column>
      <el-table-column label="主演" align="center" prop="actors" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-for="(actor, index) in scope.row.actorList" :key="index">
            {{ actor.name }}<span v-if="index < scope.row.actorList.length - 1">, </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="导演" align="center" prop="directors" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-for="(director, index) in scope.row.directorList" :key="index">
            {{ director.name }}<span v-if="index < scope.row.directorList.length - 1">, </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="电影地区" align="center" prop="areaId" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ getAreaLabel(scope.row.areaId) }}
        </template>
      </el-table-column>

      <el-table-column label="电影类型" align="center" prop="genreId">
        <template slot-scope="scope">
          {{ getGenreLabel(scope.row.genreId) }}
        </template>
      </el-table-column>
      <el-table-column label="观看次数" align="center" prop="count" show-overflow-tooltip />
      <el-table-column label="本周观看次数" align="center" prop="weekcount" show-overflow-tooltip />
      <el-table-column label="月观看次数" align="center" prop="monthcount" show-overflow-tooltip />
      <el-table-column label="vip" align="center" prop="see">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.is_vip" :value="scope.row.see"/>
        </template>
      </el-table-column>
      <el-table-column label="电影描述" align="center" prop="description" show-overflow-tooltip />
      <el-table-column label="评分" align="center" prop="scoreTotal" />
      <el-table-column label="评分人数" align="center" prop="scoreCount" show-overflow-tooltip />
      <el-table-column label="封面url" align="center" prop="coverurl" show-overflow-tooltip />
      <el-table-column label="资源地址" align="center" prop="movieurl" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['movies:movies:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['movies:movies:remove']"
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

    <!-- 添加或修改电影表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="电影名字" prop="name">
          <el-input v-model="form.name" placeholder="请输入电影名字" />
        </el-form-item>
        <el-form-item label="上映年份" prop="releaseYear">
          <el-select v-model="form.releaseDate" placeholder="请选择年份">
            <el-option label="未知" :value=null />
            <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year"
              :value="year"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="主演" prop="actors">
          <el-select
            v-model="form.actors"
            filterable
            multiple
            placeholder="输入搜索主演"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="item in Actors"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="导演" prop="directors">
          <el-select
            v-model="form.directors"
            filterable
            multiple
            placeholder="输入搜索导演"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="item in Directors"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="电影地区" prop="areaId">
          <el-select v-model="form.areaId" placeholder="请选择电影地区">
            <el-option
              v-for="area in areas"
              :key="area.value"
              :label="area.label"
              :value="area.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="电影类型" prop="genreId">
          <el-select v-model="form.genreId" placeholder="请选择电影类型">
            <el-option
              v-for="genre in genres"
              :key="genre.value"
              :label="genre.label"
              :value="genre.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="观看次数" prop="count">
          <el-input v-model="form.count" placeholder="请输入观看次数" />
        </el-form-item>
        <el-form-item label="本周观看次数" prop="weekcount">
          <el-input v-model="form.weekcount" placeholder="请输入本周观看次数" />
        </el-form-item>
        <el-form-item label="月观看次数" prop="monthcount">
          <el-input v-model="form.monthcount" placeholder="请输入月观看次数" />
        </el-form-item>
        <el-form-item label="vip" prop="see">
          <el-select v-model="form.see" placeholder="请选择vip">
            <el-option
              v-for="dict in dict.type.is_vip"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电影描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="评分" prop="scoreTotal">
          <el-input v-model="form.scoreTotal" placeholder="请输入评分" />
        </el-form-item>
        <el-form-item label="评分人数" prop="scoreCount">
          <el-input v-model="form.scoreCount" placeholder="请输入评分人数" />
        </el-form-item>
        <el-form-item label="封面url" prop="coverurl">
          <el-input v-model="form.coverurl" placeholder="请输入封面url" />
        </el-form-item>
        <el-form-item label="资源地址" prop="movieurl">
          <el-input v-model="form.movieurl" placeholder="请输入资源地址" />
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
import { listMovies, getMovies, delMovies, addMovies, updateMovies, searchAreas, searchGenres, listActors, listDirectors } from "@/api/movies/movies"

export default {
  name: "Movies",
  dicts: ['is_vip'],
  data() {
    return {
      exportDialogVisible: false,
      exportType: 'movie',
      yearOptions: [],
      // 地区字典
      areas: [],
      // 类型字典
      genres: [],
      // 演员表
      Actors: [],
      // 导演表
      Directors: [],
      formTop: {
        actors: [],
        directors: [],
      },
      top: {
        count: null,
        weekcount: null,
        monthcount: null,
      },
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
      // 电影表表格数据
      moviesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        areaId: null,
        genreId: null,
        count: null,
        weekcount: null,
        monthcount: null,
        see: null,
        releaseDate: null,
      },
      // 表单参数
      form: {
        actors: [],
        directors: [],
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "电影名字不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getYear();
    this.getAreas();
    this.getGenres();
    this.getActors();
    this.getDirectors();
    this.getList();
  },
  mounted() {
    this.$store.commit('delete_actors/SET_MOVIES_LOADED', true);
    this.$store.commit('delete_directors/SET_MOVIES_LOADED', true);
    this.$store.commit('increase_actors/SET_MOVIES_LOADED', true);
    this.$store.commit('increase_directors/SET_MOVIES_LOADED', true);
    this.$watch(
      () => this.$store.state.delete_actors.dataUpdatedFlag,
      (newVal) => {
        if (newVal) {
          const deletedIds = this.$store.state.delete_actors.lastDeletedActorIds
          this.changeActorList(deletedIds)
          this.$store.commit('delete_actors/CLEAR_DATA_UPDATED_FLAG')
        }
      }
    ),
    this.$watch(
      () => this.$store.state.delete_directors.dataUpdatedFlag,
      (newVal) => {
        if (newVal) {
          const deletedIds = this.$store.state.delete_directors.lastDeletedDirectorIds
          this.Directors = this.Directors.filter(director => !deletedIds.includes(director.value))
          // 刷新表格
          this.moviesList.forEach(movie => {
            movie.directorList = movie.directorList.filter(director => !deletedIds.includes(director.id))
          })
          
          this.$store.commit('delete_directors/CLEAR_DATA_UPDATED_FLAG')
        }
      }
    ),
    this.$watch(
      () => this.$store.state.increase_actors.dataUpdatedFlag,
      (newVal) => {
        if (newVal) {
          this.getActors()
          if (this.$store.state.increase_actors.IncreaseIds == 2){
            this.getList();
          }
          this.$store.commit('increase_actors/CLEAR_DATA_UPDATED_FLAG')
        }
      }
    ),
    this.$watch(
      () => this.$store.state.increase_directors.dataUpdatedFlag,
      (newVal) => {
        if (newVal) {
          this.getDirectors()
          if (this.$store.state.increase_directors.IncreaseIds == 2){
            this.getList();
          }
          this.$store.commit('increase_directors/CLEAR_DATA_UPDATED_FLAG')
        }
      }
    )
  },
  methods: {
    changeActorList(deletedIds){   
      // 删除已删除的演员
      this.Actors = this.Actors.filter(actor => !deletedIds.includes(actor.value))
      // 刷新表格
      this.movieList.forEach(movie => {
        movie.actorList = movie.actorList.filter(actor => !deletedIds.includes(actor.id))
      })
    },
    getYear(){
      const currentYear = new Date().getFullYear();
      for (let y = currentYear; y >= 1990; y--) {
        this.yearOptions.push(y);
      }
    },
    // 获取地区字典
    getAreas() {
      searchAreas().then(response => {
        this.areas = response.data.map(area => {
          return { value: area.id, label: area.areaname }
        })
      })
    },
    // 获取类型字典
    getGenres() {
      searchGenres().then(response => {
        this.genres = response.data.map(genre => {
          return { value: genre.id, label: genre.type }
        })
      })
    },
    getAreaLabel(id) {
      const item = this.areas.find(area => area.value === id);
      return item ? item.label : id;
    },
    getGenreLabel(id) {
      const item = this.genres.find(genre => genre.value === id);
      return item ? item.label : id;
    },
    getActors() {
      listActors().then(response => {
        this.Actors = response.data.map(actor => {
          return { value: actor.id, label: actor.name }
        })
      })
    },
    getDirectors() {
      listDirectors().then(response => {
        this.Directors = response.data.map(director => {
          return { value: director.id, label: director.name }
        })
      })
    },
    /** 查询电影表列表 */
    getList() {
      this.loading = true
      // 结合主演和导演的查询条件
      if (this.formTop.actors){
        this.queryParams.actors = this.formTop.actors.join(',')
      } else {
        this.queryParams.actors = null
      }
      if (this.formTop.directors){
        this.queryParams.directors = this.formTop.directors.join(',')
      } else {
        this.queryParams.directors = null
      }
      if(this.top.count) {
        const countRange = this.top.count.split(',')
        this.queryParams.beginCount = countRange[0] ? parseInt(countRange[0]) : null
        this.queryParams.endCount = countRange[1] ? parseInt(countRange[1]) : null
        if(this.queryParams.beginCount > this.queryParams.endCount) {
          this.$modal.msgError("观看次数范围开始值不能大于结束值")
          this.loading = false
          return
        }
      } else {
        this.queryParams.beginCount = null
        this.queryParams.endCount = null
      }
      if(this.top.weekcount) {
        const weekCountRange = this.top.weekcount.split(',')
        this.queryParams.beginWeekCount = weekCountRange[0] ? parseInt(weekCountRange[0]) : null
        this.queryParams.endWeekCount = weekCountRange[1] ? parseInt(weekCountRange[1]) : null
        if(this.queryParams.beginWeekCount > this.queryParams.endWeekCount) {
          this.$modal.msgError("本周观看次数范围开始值不能大于结束值")
          this.loading = false
          return
        }
      } else {
        this.queryParams.beginWeekCount = null
        this.queryParams.endWeekCount = null
      }
      if(this.top.monthcount) {
        const monthCountRange = this.top.monthcount.split(',')
        this.queryParams.beginMonthCount = monthCountRange[0] ? parseInt(monthCountRange[0]) : null
        this.queryParams.endMonthCount = monthCountRange[1] ? parseInt(monthCountRange[1]) : null
        if(this.queryParams.beginMonthCount > this.queryParams.endMonthCount) {
          this.$modal.msgError("月观看次数范围开始值不能大于结束值")
          this.loading = false
          return
        }
      } else {
        this.queryParams.beginMonthCount = null
        this.queryParams.endMonthCount = null
      }

      this.queryParams.releaseDateStart = null
      this.queryParams.releaseDateEnd = null
      console.log(this.queryParams);
      
      listMovies(this.queryParams).then(response => {


        // console.log(response);
        
        this.moviesList = response.rows
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
        areaId: null,
        genreId: null,
        count: null,
        weekcount: null,
        monthcount: null,
        see: null,
        description: null,
        scoreTotal: null,
        scoreCount: null,
        coverurl: null,
        movieurl: null,
        actors: [],
        directors: [],
        releaseDate: null,
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
      this.formTop = {
        actors: [],
        directors: []
      }
      this.top = {
        count: null,
        weekcount: null,
        monthcount: null,
      }
      this.queryParams.releaseDate = null
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
      this.title = "添加电影表"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMovies(id).then(response => {
        this.form = response.data
        this.form.actors = this.moviesList.find(item => item.id === id).actorList.map(actor => actor.id)
        this.form.directors = this.moviesList.find(item => item.id === id).directorList.map(director => director.id)
        this.open = true
        this.title = "修改电影表"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const { actors, directors, ...movies } = this.form;
          const payload = {
            movies,
            actors: actors || [],
            directors: directors || []
          };

          // console.log(payload);
          
          if (this.form.id != null) {
            updateMovies(payload).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMovies(payload).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除电影表编号为"' + ids + '"的数据项？').then(function() {
        return delMovies(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.exportDialogVisible = false;
      switch (this.exportType) {
        case 'movie':
          this.exportType = 0
          break;
        case 'play':
          this.exportType = 1
          break;
        case 'month':
          this.exportType = 2
          break;
        case 'week':
          this.exportType = 3
          break;
        case 'score':
          this.exportType = 4
          break;
      }
      this.queryParams.exportType = this.exportType
      this.download('movies/movies/export', {
        ...this.queryParams
      }, `movies_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style>
.export-dialog .el-dialog__wrapper {
  border-radius: 12px !important;
}

.export-dialog .el-dialog {
  border-radius: 12px !important;
  overflow: hidden;
}
.radio-item {
  margin: 20px 0;         
}
</style>
