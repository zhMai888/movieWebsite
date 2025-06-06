package com.movie.data.controller;

import com.movie.common.core.controller.BaseController;
import com.movie.common.core.domain.AjaxResult;
import com.movie.common.core.page.TableDataInfo;
import com.movie.data.domain.Movies;
import com.movie.data.service.ICollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/collections/collections")
public class CollectionsController extends BaseController {
    @Autowired
    private ICollectionsService collectionsService;

    /**
     * 获取收藏列表
     */
    @GetMapping("/get")
    public TableDataInfo getCollectionsId(Long id) {
        startPage();
        List<Movies> collections = collectionsService.getCollectionsById(id);
        return getDataTable(collections);
    }

    /**
     * 增加收藏
     */
    @GetMapping("/add")
    public AjaxResult addCollections(Long user_id, Long movie_id) {
        collectionsService.addCollections(user_id, movie_id);
        return AjaxResult.success("收藏成功");
    }

    /**
     * 删除收藏
     */
    @GetMapping("/delete")
    public AjaxResult deleteCollections(Long user_id, Long movie_id) {
        collectionsService.deleteCollections(user_id, movie_id);
        return AjaxResult.success("删除成功");
    }
}
