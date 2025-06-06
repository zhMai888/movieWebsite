package com.movie.data.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movie.data.mapper.DirectorsMapper;
import com.movie.data.domain.Directors;
import com.movie.data.service.IDirectorsService;

/**
 * 导演Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@Service
public class DirectorsServiceImpl implements IDirectorsService 
{
    @Autowired
    private DirectorsMapper directorsMapper;

    /**
     * 查询导演
     * 
     * @param id 导演主键
     * @return 导演
     */
    @Override
    public Directors selectDirectorsById(Long id)
    {
        return directorsMapper.selectDirectorsById(id);
    }

    /**
     * 查询导演列表
     * 
     * @param directors 导演
     * @return 导演
     */
    @Override
    public List<Directors> selectDirectorsList(Directors directors)
    {
        return directorsMapper.selectDirectorsList(directors);
    }

    /**
     * 新增导演
     * 
     * @param directors 导演
     * @return 结果
     */
    @Override
    public int insertDirectors(Directors directors)
    {
        return directorsMapper.insertDirectors(directors);
    }

    /**
     * 修改导演
     * 
     * @param directors 导演
     * @return 结果
     */
    @Override
    public int updateDirectors(Directors directors)
    {
        return directorsMapper.updateDirectors(directors);
    }

    /**
     * 批量删除导演
     * 
     * @param ids 需要删除的导演主键
     * @return 结果
     */
    @Override
    public int deleteDirectorsByIds(Long[] ids)
    {
        // 先删除关联表
        directorsMapper.deleteMovie_DirectorByDirectorIds(ids);
        return directorsMapper.deleteDirectorsByIds(ids);
    }

    /**
     * 删除导演信息
     * 
     * @param id 导演主键
     * @return 结果
     */
    @Override
    public int deleteDirectorsById(Long id)
    {
        // 删除关联表
        directorsMapper.deleteMovie_DirectorByDirectorIds(new Long[]{id});
        return directorsMapper.deleteDirectorsById(id);
    }
}
