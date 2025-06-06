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
import com.movie.data.domain.Actors;
import com.movie.data.service.IActorsService;
import com.movie.common.utils.poi.ExcelUtil;
import com.movie.common.core.page.TableDataInfo;

/**
 * 演员Controller
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/actors/actors")
public class ActorsController extends BaseController
{
    @Autowired
    private IActorsService actorsService;

    /**
     * 查询演员列表
     */
    @PreAuthorize("@ss.hasPermi('actors:actors:list')")
    @GetMapping("/list")
    public TableDataInfo list(Actors actors)
    {
        startPage();
        List<Actors> list = actorsService.selectActorsList(actors);
        return getDataTable(list);
    }

    /**
     * 导出演员列表
     */
    @PreAuthorize("@ss.hasPermi('actors:actors:export')")
    @Log(title = "演员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Actors actors)
    {
        List<Actors> list = actorsService.selectActorsList(actors);
        ExcelUtil<Actors> util = new ExcelUtil<Actors>(Actors.class);
        util.exportExcel(response, list, "演员数据");
    }

    /**
     * 获取演员详细信息
     */
    @PreAuthorize("@ss.hasPermi('actors:actors:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(actorsService.selectActorsById(id));
    }

    /**
     * 新增演员
     */
    @PreAuthorize("@ss.hasPermi('actors:actors:add')")
    @Log(title = "演员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Actors actors)
    {
        return toAjax(actorsService.insertActors(actors));
    }

    /**
     * 修改演员
     */
    @PreAuthorize("@ss.hasPermi('actors:actors:edit')")
    @Log(title = "演员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Actors actors)
    {
        return toAjax(actorsService.updateActors(actors));
    }

    /**
     * 删除演员
     */
    @PreAuthorize("@ss.hasPermi('actors:actors:remove')")
    @Log(title = "演员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(actorsService.deleteActorsByIds(ids));
    }
}
