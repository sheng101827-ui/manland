package com.example.sens.controller;

import com.example.sens.common.base.BaseController;
import com.example.sens.dto.JsonResult;
import com.example.sens.entity.House;
import com.example.sens.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/house")
public class HouseController extends BaseController {

    @Autowired
    private HouseService houseService;

    @PostMapping("/add")
    public JsonResult addHouse(@RequestBody House house) {
        house.setCreateTime(new Date());
        houseService.save(house);
        return JsonResult.success("发布成功");
    }
}
