package com.movie.data.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.movie.common.annotation.Log;
import com.movie.common.core.controller.BaseController;
import com.movie.common.core.domain.AjaxResult;
import com.movie.common.enums.BusinessType;
import com.movie.data.domain.Genres;
import com.movie.data.service.IGenresService;
import com.movie.common.utils.poi.ExcelUtil;
import com.movie.common.core.page.TableDataInfo;

/**
 * 类别表Controller
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/genres/genres")
public class GenresController extends BaseController
{
    @Autowired
    private IGenresService genresService;

    /**
     * 查询类别表列表
     */
    @PreAuthorize("@ss.hasPermi('genres:genres:list')")
    @GetMapping("/list")
    public TableDataInfo list(Genres genres)
    {
        startPage();
        List<Genres> list = genresService.selectGenresList(genres);
        return getDataTable(list);
    }

    /**
     * 导出类别表列表
     */
    @PreAuthorize("@ss.hasPermi('genres:genres:export')")
    @Log(title = "类别表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Genres genres)
    {
        List<Genres> list = genresService.selectGenresList(genres);
        ExcelUtil<Genres> util = new ExcelUtil<Genres>(Genres.class);
        util.exportExcel(response, list, "类别表数据");
    }

    /**
     * 获取类别表详细信息
     */
    @PreAuthorize("@ss.hasPermi('genres:genres:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(genresService.selectGenresById(id));
    }

    /**
     * 新增类别表
     */
    @PreAuthorize("@ss.hasPermi('genres:genres:add')")
    @Log(title = "类别表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Genres genres)
    {
        return toAjax(genresService.insertGenres(genres));
    }

    /**
     * 修改类别表
     */
    @PreAuthorize("@ss.hasPermi('genres:genres:edit')")
    @Log(title = "类别表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Genres genres)
    {
        return toAjax(genresService.updateGenres(genres));
    }

    /**
     * 删除类别表
     */
    @PreAuthorize("@ss.hasPermi('genres:genres:remove')")
    @Log(title = "类别表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(genresService.deleteGenresByIds(ids));
    }
}
