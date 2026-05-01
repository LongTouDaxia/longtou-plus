package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.CollectDTO;
import com.mall.LongTou.entity.Collect;
import com.mall.LongTou.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/collect")
@Validated
public class CollectController {

    @Autowired
    private CollectService collectService;

    @PostMapping("/new")
    public Result<Void> addCollect(@Valid @RequestBody CollectDTO dto) {
        collectService.addCollect(dto.getUserId(), dto.getProductId());
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> removeCollect(@Valid @RequestBody CollectDTO dto) {
        collectService.removeCollect(dto.getUserId(), dto.getProductId());
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Collect>> getUserCollects(@RequestParam Integer userId) {
        List<Collect> list = collectService.getUserCollects(userId);
        return Result.success(list);
    }
}