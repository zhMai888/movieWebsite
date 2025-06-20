package com.movie.data.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.movie.common.utils.bean.BeanUtils;
import com.movie.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.movie.common.annotation.Log;
import com.movie.common.core.controller.BaseController;
import com.movie.common.core.domain.AjaxResult;
import com.movie.common.enums.BusinessType;
import com.movie.data.service.IMoviesService;
import com.movie.common.utils.poi.ExcelUtil;
import com.movie.common.core.page.TableDataInfo;

import static com.movie.common.core.domain.AjaxResult.success;

/**
 * 电影表Controller
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/movies/movies")
public class MoviesController extends BaseController
{
    @Autowired
    private IMoviesService moviesService;

    /**
     * 查询电影表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Movies movies, String actors, String directors,
                              Long beginCount, Long endCount,
                              Long beginWeekCount, Long endWeekCount,
                              Long beginMonthCount, Long endMonthCount,
                              int pageNum, int pageSize, Integer releaseDateStart, Integer releaseDateEnd) {
        System.out.println(movies.getReleaseDate());
        System.out.println("开始时间：" + releaseDateStart);
        System.out.println("结束时间：" + releaseDateEnd);
        Map<String, Long> countList = new HashMap<>();
        countList.put("beginCount", beginCount);
        countList.put("endCount", endCount);
        countList.put("beginWeekCount", beginWeekCount);
        countList.put("endWeekCount", endWeekCount);
        countList.put("beginMonthCount", beginMonthCount);
        countList.put("endMonthCount", endMonthCount);

        PageResult<MovieShow> pageResult = moviesService.selectMoviesList(movies, actors, directors, countList, pageNum, pageSize, 0, releaseDateStart, releaseDateEnd);

        // 这里自己创建 TableDataInfo，传入 total 和 rows
        TableDataInfo dataTable = new TableDataInfo();
        dataTable.setTotal(pageResult.getTotal());
        dataTable.setRows(pageResult.getRows());
        dataTable.setCode(0);       // 通常0表示成功
        dataTable.setMsg("查询成功");

        return dataTable;
    }


    /**
     * 导出电影表列表
     */
    @PreAuthorize("@ss.hasPermi('movies:movies:export')")
    @Log(title = "电影表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Movies movies, String actors, String directors, Integer exportType)
    {
        Map<String, Long> countList = new HashMap<>();
        PageResult<MovieShow> list_or = moviesService.selectMoviesList(movies, actors, directors, countList,1, Integer.MAX_VALUE, exportType, null, null);
        List<MovieExportVO> exportList = new ArrayList<>();

        List<MovieShow> list = list_or.getRows();

        for (MovieShow movie : list) {
            MovieExportVO vo = new MovieExportVO();
            BeanUtils.copyProperties(movie, vo);

            // 查出电影地区和类型
            if (movie.getAreaId() != null) {
                String area = moviesService.selectAreasById(movie.getAreaId());
                vo.setAreaName(area);
            }

            if(movie.getGenreId() != null) {
                String genre = moviesService.selectGenresById(movie.getGenreId());
                vo.setGenreName(genre);
            }

            // 拼接导演名字，用“、”连接
            if (movie.getDirectorList() != null) {
                String directorNames = movie.getDirectorList().stream()
                        .map(Directors::getName)
                        .collect(Collectors.joining("、"));
                vo.setDirectors(directorNames);
            }

            // 拼接演员名字，用“、”连接
            if (movie.getActorList() != null) {
                String actorNames = movie.getActorList().stream()
                        .map(Actors::getName)
                        .collect(Collectors.joining("、"));
                vo.setActors(actorNames);
            }

            exportList.add(vo);
        }

        // 导出Excel
        ExcelUtil<MovieExportVO> util = new ExcelUtil<>(MovieExportVO.class);
        util.exportExcel(response, exportList, "电影表数据");
    }

    /**
     * 获取电影表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(moviesService.selectMoviesById(id));
    }

    /**
     * 新增电影表
     */
    @PreAuthorize("@ss.hasPermi('movies:movies:add')")
    @Log(title = "电影表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MovieDTO moviesDTO) {
        Movies movies = moviesDTO.getMovies();
        List<Long> actors = moviesDTO.getActors();
        List<Long> directors = moviesDTO.getDirectors();

        // 新增电影信息
        moviesService.insertMovies(movies);
        Long movieId = movies.getId();

        // 2. 插入演员
        if (actors != null && !actors.isEmpty()) {
            moviesService.insertMovieActors(movieId, actors);
        }

        // 插入导演
        if (directors != null && !directors.isEmpty()) {
            moviesService.insertMovieDirectors(movieId, directors);
        }

        // 修改类型数量
        if (movies.getGenreId() != null) {
            moviesService.updateGenreNums(movies.getGenreId(), 1);
        }

        // 修改地区数量
        if (movies.getAreaId() != null) {
            moviesService.updateAreaNums(movies.getAreaId(), 1);
        }

        return toAjax(1);
    }

    /**
     * 修改电影表
     */
    @PreAuthorize("@ss.hasPermi('movies:movies:edit')")
    @Log(title = "电影表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MovieDTO moviesDTO) {
        Movies movies = moviesDTO.getMovies();
        List<Long> actors = moviesDTO.getActors();
        List<Long> directors = moviesDTO.getDirectors();

        // 修改类型
        Movies movie = moviesService.selectMoviesById(movies.getId());
        if (movies.getGenreId() != null && !Objects.equals(movies.getGenreId(), movie.getGenreId())) {
            // 类型更改了，需要修改数量
            moviesService.updateGenreNums(movie.getGenreId(), -1);
            moviesService.updateGenreNums(movies.getGenreId(), 1);
        }

        // 修改地区
        if (movies.getAreaId() != null && !Objects.equals(movies.getAreaId(), movie.getAreaId())) {
            // 地区更改了，需要修改数量
            moviesService.updateAreaNums(movie.getAreaId(), -1);
            moviesService.updateAreaNums(movies.getAreaId(), 1);
        }

        // 更新电影信息
        int rows = moviesService.updateMovies(movies);
        Long movieId = movies.getId();

        // 删除旧的关联
        moviesService.deleteMovieActors(movieId);
        moviesService.deleteMovieDirectors(movieId);

        // 插入新的演员
        if (actors != null && !actors.isEmpty()) {
            moviesService.insertMovieActors(movieId, actors);
        }

        // 插入新的导演
        if (directors != null && !directors.isEmpty()) {
            moviesService.insertMovieDirectors(movieId, directors);
        }


        return toAjax(rows);
    }


    /**
     * 删除电影表
     */
    @PreAuthorize("@ss.hasPermi('movies:movies:remove')")
    @Log(title = "电影表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        for (Long id : ids) {
            moviesService.deleteMovieActors(id);
            moviesService.deleteMovieDirectors(id);
            // 修改地区数量
            Movies movie = moviesService.selectMoviesById(id);
            if (movie.getAreaId() != null) {
                moviesService.updateAreaNums(movie.getAreaId(), -1);
            }
            // 修改类型数量
            if (movie.getGenreId() != null) {
                moviesService.updateGenreNums(movie.getGenreId(), -1);
            }

        }
        // 删除电影收藏
        moviesService.deleteMovieCollectionsByIds(ids);

        // 删除电影信息
        moviesService.deleteMoviesByIds(ids);
        return toAjax(ids.length);
    }

    /**
     * 搜索地区列表
     */
    @GetMapping("/searchAreas")
    public AjaxResult searchAreas() {
        List<Areas> areasList = moviesService.selectAreasList(new Areas());
        return success(areasList);
    }

    /**
     * 搜索电影类型列表
     */
    @GetMapping("/searchGenres")
    public AjaxResult searchGenres() {
        List<Genres> genresList = moviesService.selectGenresList(new Genres());
        return success(genresList);
    }

    /**
     * 搜索主演列表
     */
    @GetMapping("/listActors")
    public AjaxResult listActors() {
        List<Actors> actorsList = moviesService.selectActorsList(new Actors());
        return success(actorsList);
    }

    /**
     * 搜索导演列表
     */
    @GetMapping("/listDirectors")
    public AjaxResult listDirectors() {
        List<Directors> directorsList = moviesService.selectDirectorsList(new Directors());
        return success(directorsList);
    }
}














