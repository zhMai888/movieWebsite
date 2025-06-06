package com.movie.data.service;

import java.util.List;
import com.movie.data.domain.Genres;

/**
 * 类别表Service接口
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public interface IGenresService 
{
    /**
     * 查询类别表
     * 
     * @param id 类别表主键
     * @return 类别表
     */
    public Genres selectGenresById(Long id);

    /**
     * 查询类别表列表
     * 
     * @param genres 类别表
     * @return 类别表集合
     */
    public List<Genres> selectGenresList(Genres genres);

    /**
     * 新增类别表
     * 
     * @param genres 类别表
     * @return 结果
     */
    public int insertGenres(Genres genres);

    /**
     * 修改类别表
     * 
     * @param genres 类别表
     * @return 结果
     */
    public int updateGenres(Genres genres);

    /**
     * 批量删除类别表
     * 
     * @param ids 需要删除的类别表主键集合
     * @return 结果
     */
    public int deleteGenresByIds(Long[] ids);

    /**
     * 删除类别表信息
     * 
     * @param id 类别表主键
     * @return 结果
     */
    public int deleteGenresById(Long id);
}
