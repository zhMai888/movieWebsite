package com.movie.data.service.impl;

import com.movie.data.domain.Movies;
import com.movie.data.mapper.CollectionsMapper;
import com.movie.data.service.ICollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionsServiceImpl implements ICollectionsService {

    @Autowired
    private CollectionsMapper collectionsMapper;

    @Override
    public List<Movies> getCollectionsById(Long id){
        return collectionsMapper.selectById(id);
    }

    @Override
    public void addCollections(Long userId, Long movieId){
        collectionsMapper.addById(userId, movieId);
    }

    @Override
    public void deleteCollections(Long userId, Long movieId){
        collectionsMapper.deleteById(userId, movieId);
    }
}
