package com.movie.data.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movie.data.mapper.GenresMapper;
import com.movie.data.domain.Genres;
import com.movie.data.service.IGenresService;

/**
 * 类别表Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@Service
public class GenresServiceImpl implements IGenresService 
{
    @Autowired
    private GenresMapper genresMapper;

    /**
     * 查询类别表
     * 
     * @param id 类别表主键
     * @return 类别表
     */
    @Override
    public Genres selectGenresById(Long id)
    {
        return genresMapper.selectGenresById(id);
    }

    /**
     * 查询类别表列表
     * 
     * @param genres 类别表
     * @return 类别表
     */
    @Override
    public List<Genres> selectGenresList(Genres genres)
    {
        return genresMapper.selectGenresList(genres);
    }

    /**
     * 新增类别表
     * 
     * @param genres 类别表
     * @return 结果
     */
    @Override
    public int insertGenres(Genres genres)
    {
        return genresMapper.insertGenres(genres);
    }

    /**
     * 修改类别表
     * 
     * @param genres 类别表
     * @return 结果
     */
    @Override
    public int updateGenres(Genres genres)
    {
        return genresMapper.updateGenres(genres);
    }

    /**
     * 批量删除类别表
     * 
     * @param ids 需要删除的类别表主键
     * @return 结果
     */
    @Override
    public int deleteGenresByIds(Long[] ids)
    {
        return genresMapper.deleteGenresByIds(ids);
    }

    /**
     * 删除类别表信息
     * 
     * @param id 类别表主键
     * @return 结果
     */
    @Override
    public int deleteGenresById(Long id)
    {
        return genresMapper.deleteGenresById(id);
    }
}
