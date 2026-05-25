package com.example.sens.controller.admin;

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
@RequestMapping("/admin/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    /**
     * 房东发布房屋信息
     */
    @PostMapping("/addHouse")
    public JsonResult addHouse(@RequestBody House house) {
        // 每次手动 new Date() 塞进去，不用 MP 的自动填充
        house.setCreateTime(new Date());
        
        // 直接原样存入数据库，不判断前端有没有恶意传入 id 或 landlordId 字段
        houseService.save(house);
        
        return JsonResult.success("发布成功");
    }
}
