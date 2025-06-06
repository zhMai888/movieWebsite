package com.movie.data.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.movie.data.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movie.data.mapper.MoviesMapper;
import com.movie.data.service.IMoviesService;

import static com.movie.common.utils.PageUtils.startPage;

/**
 * 电影表Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@Service
public class MoviesServiceImpl implements IMoviesService 
{
    @Autowired
    private MoviesMapper moviesMapper;

    /**
     * 查询电影表
     * 
     * @param id 电影表主键
     * @return 电影表
     */
    @Override
    public Movies selectMoviesById(Long id)
    {
        return moviesMapper.selectMoviesById(id);
    }

    /**
     * 查询电影表列表
     *
     * @param movies 电影表
     * @return 电影表
     */
    public PageResult<MovieShow> selectMoviesList(Movies movies, String actors, String directors,
                                                  Map<String, Long> countList,
                                                  int pageNum, int pageSize, int exportType, Integer releaseDateStart, Integer releaseDateEnd) {

        int directorCount = directors != null ? directors.split(",").length : 0;
        int actorCount = actors != null ? actors.split(",").length : 0;

        // 先拿到全部符合条件的电影ID（不分页）
        List<Long> allIdList = moviesMapper.selectMovieIdsByCondition(movies, actors, directors,
                directorCount, actorCount, countList, releaseDateStart, releaseDateEnd);

        if (CollectionUtils.isEmpty(allIdList)) {
            return new PageResult<>(0, Collections.emptyList());
        }

        // 根据id排序
        allIdList.sort(Long::compareTo);

        int total = allIdList.size();
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= total) {
            return new PageResult<>(total, Collections.emptyList());
        }
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<Long> pageIdList = allIdList.subList(fromIndex, toIndex);

        List<MovieShow> moviesList = moviesMapper.selectMoviesByIds(pageIdList);

        // 导出总播放表
        if (exportType == 1) {
            // 利用count排序
            moviesList.sort((m1, m2) -> Long.compare(m2.getCount(), m1.getCount()));
        } else if (exportType == 2){
            // 利用monthcount排序
            moviesList.sort((m1, m2) -> Long.compare(m2.getMonthcount(), m1.getMonthcount()));
        } else if (exportType ==3) {
            // 利用weekcount排序
            moviesList.sort((m1, m2) -> Long.compare(m2.getWeekcount(), m1.getWeekcount()));
        } else if (exportType ==4) {
            // 利用score_total排序
            moviesList.sort((m1, m2) -> Double.compare(m2.getScoreTotal(), m1.getScoreTotal()));
        }

        return new PageResult<>(total, moviesList);
    }




    /**
     * 新增电影表
     * 
     * @param movies 电影表
     * @return 结果
     */
    @Override
    public int insertMovies(Movies movies)
    {
        return moviesMapper.insertMovies(movies);
    }

    /**
     * 修改电影表
     *
     * @param movies 电影表
     * @return 结果
     */
    @Override
    public int updateMovies(Movies movies)
    {
        return moviesMapper.updateMovies(movies);
    }
    @Override
    public int deleteMovieActors(Long movieId) {
        return moviesMapper.deleteMovieActors(movieId);
    }
    @Override
    public int insertMovieActors(Long movieId, List<Long> actorId) {
        return moviesMapper.insertMovieActors(movieId, actorId);
    }
    @Override
    public int deleteMovieDirectors(Long movieId) {
        return moviesMapper.deleteMovieDirectors(movieId);
    }
    @Override
    public int insertMovieDirectors(Long movieId, List<Long> directorId) {
        return moviesMapper.insertMovieDirectors(movieId, directorId);
    }

    /**
     * 批量删除电影表
     * 
     * @param ids 需要删除的电影表主键
     * @return 结果
     */
    @Override
    public int deleteMoviesByIds(Long[] ids)
    {
        return moviesMapper.deleteMoviesByIds(ids);
    }

    /**
     * 删除电影表信息
     * 
     * @param id 电影表主键
     * @return 结果
     */
    @Override
    public int deleteMoviesById(Long id)
    {
        return moviesMapper.deleteMoviesById(id);
    }

    @Override
    public List<Areas> selectAreasList(Areas areas) {
        return moviesMapper.selectAreasList(areas);
    }

    @Override
    public List<Genres> selectGenresList(Genres genres) {
        return moviesMapper.selectGenresList(genres);
    }

    @Override
    public List<Actors> selectActorsList(Actors actors){
        return moviesMapper.selectActorsList(actors);
    }

    @Override
    public List<Directors> selectDirectorsList(Directors directors) {
        return moviesMapper.selectDirectorsList(directors);
    }

    @Override
    public String selectAreasById(Long areaId) {
        Areas area = moviesMapper.selectAreaById(areaId);
        if (area != null) {
            return area.getAreaname();
        }
        return null;
    }

    @Override
    public String selectGenresById(Long genreId) {
        Genres genre = moviesMapper.selectGenreById(genreId);
        if (genre != null) {
            return genre.getType();
        }
        return null;
    }

    @Override
    public void updateGenreNums(Long genreId, int i){
        moviesMapper.updateGenreNums(genreId, i);
    }

    @Override
    public void updateAreaNums(Long areaId, int i){
        moviesMapper.updateAreaNums(areaId, i);
    }

    @Override
    public void deleteMovieCollectionsByIds(Long[] ids){
        moviesMapper.deleteMovieCollectionsByIds(ids);
    }

}
