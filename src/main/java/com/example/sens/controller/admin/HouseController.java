package com.example.sens.controller.admin;

import com.example.sens.controller.common.BaseController;
import com.example.sens.dto.JsonResult;
import com.example.sens.entity.Post;
import com.example.sens.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 房屋特殊检索接口控制器
 */
@RestController
@RequestMapping("/admin/house")
public class HouseController extends BaseController {

    @Autowired
    private HouseService houseService;

    /**
     * 按面积检索房屋列表接口
     *
     * @param minArea 最小面积
     * @param maxArea 最大面积
     * @param page    页码
     * @param size    每页数量
     * @return JsonResult 包含房屋列表
     */
    @GetMapping("/getByArea")
    public JsonResult getByArea(@RequestParam(value = "minArea", defaultValue = "0") int minArea,
                                @RequestParam(value = "maxArea", defaultValue = "999999") int maxArea,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        
        List<Post> list = houseService.getByArea(minArea, maxArea, page, size);
        return JsonResult.success("查询成功", list);
    }
}
