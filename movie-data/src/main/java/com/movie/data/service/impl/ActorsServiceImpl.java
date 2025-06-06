package com.movie.data.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movie.data.mapper.ActorsMapper;
import com.movie.data.domain.Actors;
import com.movie.data.service.IActorsService;

/**
 * 演员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
@Service
public class ActorsServiceImpl implements IActorsService 
{
    @Autowired
    private ActorsMapper actorsMapper;

    /**
     * 查询演员
     * 
     * @param id 演员主键
     * @return 演员
     */
    @Override
    public Actors selectActorsById(Long id)
    {
        return actorsMapper.selectActorsById(id);
    }

    /**
     * 查询演员列表
     * 
     * @param actors 演员
     * @return 演员
     */
    @Override
    public List<Actors> selectActorsList(Actors actors)
    {
        return actorsMapper.selectActorsList(actors);
    }

    /**
     * 新增演员
     * 
     * @param actors 演员
     * @return 结果
     */
    @Override
    public int insertActors(Actors actors)
    {
        return actorsMapper.insertActors(actors);
    }

    /**
     * 修改演员
     * 
     * @param actors 演员
     * @return 结果
     */
    @Override
    public int updateActors(Actors actors)
    {
        return actorsMapper.updateActors(actors);
    }

    /**
     * 批量删除演员
     * 
     * @param ids 需要删除的演员主键
     * @return 结果
     */
    @Override
    public int deleteActorsByIds(Long[] ids)
    {
        // 先删除关联表
        actorsMapper.deleteMovie_ActorByActorIds(ids);
        return actorsMapper.deleteActorsByIds(ids);
    }

    /**
     * 删除演员信息
     * 
     * @param id 演员主键
     * @return 结果
     */
    @Override
    public int deleteActorsById(Long id)
    {
        // 先删除关联表
        actorsMapper.deleteMovie_ActorByActorIds(new Long[]{id});
        return actorsMapper.deleteActorsById(id);
    }
}
