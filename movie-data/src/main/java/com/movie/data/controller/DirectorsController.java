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
import com.movie.data.domain.Directors;
import com.movie.data.service.IDirectorsService;
import com.movie.common.utils.poi.ExcelUtil;
import com.movie.common.core.page.TableDataInfo;

/**
 * 导演Controller
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/directors/directors")
public class DirectorsController extends BaseController
{
    @Autowired
    private IDirectorsService directorsService;

    /**
     * 查询导演列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Directors directors)
    {
        startPage();
        List<Directors> list = directorsService.selectDirectorsList(directors);
        return getDataTable(list);
    }

    /**
     * 导出导演列表
     */
    @Log(title = "导演", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Directors directors)
    {
        List<Directors> list = directorsService.selectDirectorsList(directors);
        ExcelUtil<Directors> util = new ExcelUtil<Directors>(Directors.class);
        util.exportExcel(response, list, "导演数据");
    }

    /**
     * 获取导演详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(directorsService.selectDirectorsById(id));
    }

    /**
     * 新增导演
     */
    @Log(title = "导演", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Directors directors)
    {
        return toAjax(directorsService.insertDirectors(directors));
    }

    /**
     * 修改导演
     */
    @Log(title = "导演", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Directors directors)
    {
        return toAjax(directorsService.updateDirectors(directors));
    }

    /**
     * 删除导演
     */
    @Log(title = "导演", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(directorsService.deleteDirectorsByIds(ids));
    }
}
