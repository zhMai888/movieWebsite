package com.movie.data.service;

import java.util.List;
import java.util.Map;

import com.movie.data.domain.*;

/**
 * 电影表Service接口
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public interface IMoviesService 
{
    /**
     * 查询电影表
     * 
     * @param id 电影表主键
     * @return 电影表
     */
    public Movies selectMoviesById(Long id);

    /**
     * 查询电影表列表
     *
     * @param movies 电影表
     * @return 电影表集合
     */
    public PageResult<MovieShow> selectMoviesList(Movies movies, String actors, String directors, Map<String, Long> countList,
                                                  int pageNum, int pageSize, int exportType, Integer releaseDateStart, Integer releaseDateEnd);

    /**
     * 新增电影表
     * 
     * @param movies 电影表
     * @return 结果
     */
    public int insertMovies(Movies movies);

    /**
     * 修改电影表
     * 
     * @param movies 电影表
     * @return 结果
     */
    public int updateMovies(Movies movies);
    public int deleteMovieActors(Long movieId);
    public int insertMovieActors(Long movieId, List<Long> actorId);
    public int deleteMovieDirectors(Long movieId);
    public int insertMovieDirectors(Long movieId, List<Long> directorId);

    /**
     * 批量删除电影表
     * 
     * @param ids 需要删除的电影表主键集合
     * @return 结果
     */
    public int deleteMoviesByIds(Long[] ids);

    /**
     * 删除电影表信息
     * 
     * @param id 电影表主键
     * @return 结果
     */
    public int deleteMoviesById(Long id);

    List<Areas> selectAreasList(Areas areas);

    List<Genres> selectGenresList(Genres genres);

    List<Actors> selectActorsList(Actors actors);

    List<Directors> selectDirectorsList(Directors directors);

    String selectAreasById(Long areaId);

    String selectGenresById(Long genreId);

    void updateGenreNums(Long genreId, int i);

    void updateAreaNums(Long areaId, int i);

    public void deleteMovieCollectionsByIds(Long[] ids);
}
