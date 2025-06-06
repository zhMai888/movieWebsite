package com.movie.data.mapper;

import java.util.List;
import com.movie.data.domain.Areas;

/**
 * 地区Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public interface AreasMapper 
{
    /**
     * 查询地区
     * 
     * @param id 地区主键
     * @return 地区
     */
    public Areas selectAreasById(Long id);

    /**
     * 查询地区列表
     * 
     * @param areas 地区
     * @return 地区集合
     */
    public List<Areas> selectAreasList(Areas areas);

    /**
     * 新增地区
     * 
     * @param areas 地区
     * @return 结果
     */
    public int insertAreas(Areas areas);

    /**
     * 修改地区
     * 
     * @param areas 地区
     * @return 结果
     */
    public int updateAreas(Areas areas);

    /**
     * 删除地区
     * 
     * @param id 地区主键
     * @return 结果
     */
    public int deleteAreasById(Long id);

    /**
     * 批量删除地区
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAreasByIds(Long[] ids);
}
