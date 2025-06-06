package com.movie.data.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movie.data.mapper.AreasMapper;
import com.movie.data.domain.Areas;
import com.movie.data.service.IAreasService;

/**
 * 地区Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@Service
public class AreasServiceImpl implements IAreasService 
{
    @Autowired
    private AreasMapper areasMapper;

    /**
     * 查询地区
     * 
     * @param id 地区主键
     * @return 地区
     */
    @Override
    public Areas selectAreasById(Long id)
    {
        return areasMapper.selectAreasById(id);
    }

    /**
     * 查询地区列表
     * 
     * @param areas 地区
     * @return 地区
     */
    @Override
    public List<Areas> selectAreasList(Areas areas)
    {
        return areasMapper.selectAreasList(areas);
    }

    /**
     * 新增地区
     * 
     * @param areas 地区
     * @return 结果
     */
    @Override
    public int insertAreas(Areas areas)
    {
        return areasMapper.insertAreas(areas);
    }

    /**
     * 修改地区
     * 
     * @param areas 地区
     * @return 结果
     */
    @Override
    public int updateAreas(Areas areas)
    {
        return areasMapper.updateAreas(areas);
    }

    /**
     * 批量删除地区
     * 
     * @param ids 需要删除的地区主键
     * @return 结果
     */
    @Override
    public int deleteAreasByIds(Long[] ids)
    {
        return areasMapper.deleteAreasByIds(ids);
    }

    /**
     * 删除地区信息
     * 
     * @param id 地区主键
     * @return 结果
     */
    @Override
    public int deleteAreasById(Long id)
    {
        return areasMapper.deleteAreasById(id);
    }
}
