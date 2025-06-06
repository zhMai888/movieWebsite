package com.movie.data.mapper;

import java.util.List;
import com.movie.data.domain.Actors;

/**
 * 演员Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public interface ActorsMapper 
{
    /**
     * 查询演员
     * 
     * @param id 演员主键
     * @return 演员
     */
    public Actors selectActorsById(Long id);

    /**
     * 查询演员列表
     * 
     * @param actors 演员
     * @return 演员集合
     */
    public List<Actors> selectActorsList(Actors actors);

    /**
     * 新增演员
     * 
     * @param actors 演员
     * @return 结果
     */
    public int insertActors(Actors actors);

    /**
     * 修改演员
     * 
     * @param actors 演员
     * @return 结果
     */
    public int updateActors(Actors actors);

    /**
     * 删除演员
     * 
     * @param id 演员主键
     * @return 结果
     */
    public int deleteActorsById(Long id);

    /**
     * 批量删除演员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteActorsByIds(Long[] ids);

    void deleteMovie_ActorByActorIds(Long[] ids);
}
