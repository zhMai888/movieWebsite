package com.movie.data.service.impl;

import com.movie.data.mapper.MadMapper;
import com.movie.data.service.IMadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MadServiceImpl implements IMadService {
    @Autowired
    private MadMapper madMapper;

    @Override
    public List<Long> mtoa(Long movieId){
        return madMapper.mtoa(movieId);
    }

    @Override
    public List<Long> mtod(Long movieId){
        return madMapper.mtod(movieId);
    }

    @Override
    public List<Long> atom(Long actorId){
        return madMapper.atom(actorId);
    }

    @Override
    public List<Long> dtom(Long directorId){
        return madMapper.dtom(directorId);
    }

}

