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
import com.movie.data.domain.Areas;
import com.movie.data.service.IAreasService;
import com.movie.common.utils.poi.ExcelUtil;
import com.movie.common.core.page.TableDataInfo;

/**
 * 地区Controller
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/areas/areas")
public class AreasController extends BaseController
{
    @Autowired
    private IAreasService areasService;

    /**
     * 查询地区列表
     */
    @PreAuthorize("@ss.hasPermi('areas:areas:list')")
    @GetMapping("/list")
    public TableDataInfo list(Areas areas)
    {
        startPage();
        List<Areas> list = areasService.selectAreasList(areas);
        return getDataTable(list);
    }

    /**
     * 导出地区列表
     */
    @PreAuthorize("@ss.hasPermi('areas:areas:export')")
    @Log(title = "地区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Areas areas)
    {
        List<Areas> list = areasService.selectAreasList(areas);
        ExcelUtil<Areas> util = new ExcelUtil<Areas>(Areas.class);
        util.exportExcel(response, list, "地区数据");
    }

    /**
     * 获取地区详细信息
     */
    @PreAuthorize("@ss.hasPermi('areas:areas:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(areasService.selectAreasById(id));
    }

    /**
     * 新增地区
     */
    @PreAuthorize("@ss.hasPermi('areas:areas:add')")
    @Log(title = "地区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Areas areas)
    {
        return toAjax(areasService.insertAreas(areas));
    }

    /**
     * 修改地区
     */
    @PreAuthorize("@ss.hasPermi('areas:areas:edit')")
    @Log(title = "地区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Areas areas)
    {
        return toAjax(areasService.updateAreas(areas));
    }

    /**
     * 删除地区
     */
    @PreAuthorize("@ss.hasPermi('areas:areas:remove')")
    @Log(title = "地区", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(areasService.deleteAreasByIds(ids));
    }
}
