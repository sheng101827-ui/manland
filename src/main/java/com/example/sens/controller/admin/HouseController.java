package com.example.sens.controller.admin;

import com.example.sens.controller.common.BaseController;
import com.example.sens.dto.JsonResult;
import com.example.sens.entity.House;
import com.example.sens.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequestMapping(value = "/admin/house")
public class HouseController extends BaseController {

    @Autowired
    private HouseService houseService;

    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult addHouse(@RequestBody House house) {
        house.setCreateTime(new Date());
        houseService.save(house);
        return JsonResult.success("添加成功");
    }
}