package com.movie.data.mapper;

import java.util.List;
import java.util.Map;

import com.movie.data.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 电影表Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public interface MoviesMapper 
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
    List<Long> selectMovieIdsByCondition(@Param("movies") Movies movies,
                                         @Param("actors") String actors,
                                         @Param("directors") String directors,
                                         @Param("directorCount") Integer directorCount,
                                         @Param("actorCount") Integer actorCount,
                                         @Param("countList") Map<String, Long> countList,
                                         @Param("releaseDateStart") Integer releaseDateStart,
                                         @Param("releaseDateEnd") Integer releaseDateEnd);

    List<MovieShow> selectMoviesByIds(@Param("idList") List<Long> idList);



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
    int updateMovies(Movies movies);
    int deleteMovieActors(@Param("movieId") Long movieId);
    int deleteMovieDirectors(@Param("movieId") Long movieId);

    // 批量插入版本，注意加 @Param("list")
    int insertMovieActors(@Param("movieId") Long movieId, @Param("list") List<Long> actorIds);
    int insertMovieDirectors(@Param("movieId") Long movieId, @Param("list") List<Long> directorIds);


    /**
     * 删除电影表
     * 
     * @param id 电影表主键
     * @return 结果
     */
    public int deleteMoviesById(Long id);

    /**
     * 批量删除电影表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMoviesByIds(Long[] ids);

    List<Areas> selectAreasList(Areas areas);

    List<Genres> selectGenresList(Genres genres);

    List<Actors> selectActorsList(Actors actors);

    List<Directors> selectDirectorsList(Directors directors);

    Areas selectAreaById(Long movieId);

    Genres selectGenreById(Long genreId);

    void updateGenreNums(@Param("id") Long genreId, @Param("nums") int i);

    void updateAreaNums(@Param("id") Long areaId, @Param("novienum") int i);

    void resetWeekCount();

    void resetMonthCount();

    void deleteMovieCollectionsByIds(Long[] ids);
}
