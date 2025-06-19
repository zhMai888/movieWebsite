package com.movie.data.controller;

import com.movie.common.core.controller.BaseController;
import com.movie.common.core.domain.AjaxResult;
import com.movie.data.service.IMadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mad/mad")
class MadController extends BaseController
{
    @Autowired
    private IMadService madService;

    @GetMapping("/mtoa")
    public List<Long> mtoa(Long movie_id) {
        return madService.mtoa(movie_id);
    }

    @GetMapping("/mtod")
    public List<Long> mtod(Long movie_id) {
        return madService.mtod(movie_id);
    }

    @GetMapping("/atom")
    public List<Long> atom(Long actor_id) {
        return madService.atom(actor_id);
    }

    @GetMapping("/dtom")
    public List<Long> dtom(Long director_id) {
        return madService.dtom(director_id);
    }

}
