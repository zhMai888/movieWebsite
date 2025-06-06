package com.movie.data.mapper;

import java.util.List;
import com.movie.data.domain.Directors;

/**
 * 导演Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public interface DirectorsMapper 
{
    /**
     * 查询导演
     * 
     * @param id 导演主键
     * @return 导演
     */
    public Directors selectDirectorsById(Long id);

    /**
     * 查询导演列表
     * 
     * @param directors 导演
     * @return 导演集合
     */
    public List<Directors> selectDirectorsList(Directors directors);

    /**
     * 新增导演
     * 
     * @param directors 导演
     * @return 结果
     */
    public int insertDirectors(Directors directors);

    /**
     * 修改导演
     * 
     * @param directors 导演
     * @return 结果
     */
    public int updateDirectors(Directors directors);

    /**
     * 删除导演
     * 
     * @param id 导演主键
     * @return 结果
     */
    public int deleteDirectorsById(Long id);

    /**
     * 批量删除导演
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDirectorsByIds(Long[] ids);

    void deleteMovie_DirectorByDirectorIds(Long[] ids);
}
