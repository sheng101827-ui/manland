package com.example.sens.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.controller.common.BaseController;
import com.example.sens.dto.JsonResult;
import com.example.sens.entity.Order;
import com.example.sens.enums.OrderStatusEnum;
import com.example.sens.service.OrderService;
import com.example.sens.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     订单管理控制器
 * </pre>
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询所有订单并渲染order页面
     *
     * @return 模板路径admin/admin_order
     */
    @GetMapping
    public String orders(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                         @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "sort", defaultValue = "id") String sort,
                         @RequestParam(value = "order", defaultValue = "desc") String order, Model model) {
        Page page = PageUtil.initMpPage(pageNumber, pageSize, sort, order);
        Page<Order> orderPage = null;

        Order orderCondition = new Order();
        orderPage = orderService.findAll(orderCondition, page);
        model.addAttribute("orders", orderPage.getRecords());
        model.addAttribute("pageInfo", PageUtil.convertPageVo(page));
        return "admin/admin_order";
    }

    /**
     * 我出租的房屋订单
     *
     * @return 模板路径admin/admin_order
     */
    @GetMapping("/lease")
    public String lease(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "sort", defaultValue = "id") String sort,
                        @RequestParam(value = "order", defaultValue = "desc") String order, Model model) {
        Page page = PageUtil.initMpPage(pageNumber, pageSize, sort, order);
        Order orderCondition = new Order();
        orderCondition.setOwnerUserId(getLoginUserId());
        Page<Order> orderPage = orderService.findAll(orderCondition, page);
        model.addAttribute("orders", orderPage.getRecords());
        model.addAttribute("pageInfo", PageUtil.convertPageVo(page));

        model.addAttribute("type", "lease");
        return "admin/admin_order";
    }


    /**
     * 我的租房订单
     *
     * @return 模板路径admin/admin_order
     */
    @GetMapping("/rent")
    public String rent(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                       @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "sort", defaultValue = "id") String sort,
                       @RequestParam(value = "order", defaultValue = "desc") String order, Model model) {
        Page page = PageUtil.initMpPage(pageNumber, pageSize, sort, order);
        Order orderCondition = new Order();
        orderCondition.setUserId(getLoginUserId());
        Page<Order> orderPage  = orderService.findAll(orderCondition, page);
        model.addAttribute("orders", orderPage.getRecords());
        model.addAttribute("pageInfo", PageUtil.convertPageVo(page));

        model.addAttribute("type", "rent");
        return "admin/admin_order";
    }


    /**
     * 删除订单
     *
     * @param id 订单Id
     * @return JsonResult
     */
    @DeleteMapping(value = "/delete")
    @ResponseBody
    public JsonResult delete(@RequestParam("id") Long id) {
        Order order = orderService.get(id);
        if (order == null) {
            return JsonResult.error("订单不存在");
        }
        if (OrderStatusEnum.HAS_PAY.getCode().equals(order.getStatus())) {
            return JsonResult.error("订单生效中，不能删除");
        }

        orderService.delete(id);
        return JsonResult.success("删除成功");
    }

    /**
     * 完结订单
     *
     * @param id 订单Id
     * @return JsonResult
     */
    @PostMapping(value = "/finish")
    @ResponseBody
    public JsonResult finish(@RequestParam("id") Long id) {
        Order order = orderService.get(id);
        if (order == null) {
            return JsonResult.error("订单不存在");
        }

        order.setStatus(OrderStatusEnum.FINISHED.getCode());
        orderService.update(order);
        return JsonResult.success("完结成功");
    }

    /**
     * 关闭订单
     *
     * @param id 订单Id
     * @return JsonResult
     */
    @PostMapping(value = "/close")
    @ResponseBody
    @Transactional
    public JsonResult close(@RequestParam("id") Long id) {
        // 修改订单状态
        Order order = orderService.get(id);
        if (order == null) {
            return JsonResult.error("订单不存在");
        }

        order.setStatus(OrderStatusEnum.CLOSED.getCode());
        orderService.update(order);

        return JsonResult.success("取消订单成功");
    }

    /**
     * 财务页面
     *
     * @param model
     * @return
     */
    @GetMapping("/finance")
    public String finance(@RequestParam(value = "startDate", required = false) String startDate,
                          @RequestParam(value = "endDate", required = false) String endDate,
                          @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                          @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                          @RequestParam(value = "sort", defaultValue = "id") String sort,
                          @RequestParam(value = "order", defaultValue = "desc") String order,
                          Model model) throws ParseException {

        Page page = PageUtil.initMpPage(pageNumber, pageSize, sort, order);
        Order condition = new Order();
        Page<Order> orderPage = null;
        Order orderCondition = new Order();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(startDate)) {
            orderCondition.setStartDate(dateFormat.parse(startDate));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            orderCondition.setEndDate(dateFormat.parse(endDate));
        }
        if (loginUserIsUser()) {
            // 用户
            orderCondition.setUserId(getLoginUserId());
        }
        orderPage = orderService.findAll(orderCondition, page);

        model.addAttribute("orders", orderPage.getRecords());
        model.addAttribute("pageInfo", PageUtil.convertPageVo(page));

        Integer totalPrice = orderService.getTotalPriceSum(condition);
        model.addAttribute("totalPrice", totalPrice == null ? 0 : totalPrice);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "admin/admin_finance";
    }


    @PostConstruct
    public void autoCancelExpiredOrders() {
        new Thread(() -> {
            while (true) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.HOUR_OF_DAY, -24);
                    Date twentyFourHoursAgo = calendar.getTime();

                    QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("status", OrderStatusEnum.NOT_PAY.getCode());
                    queryWrapper.le("create_time", twentyFourHoursAgo);

                    List<Order> expiredOrders = orderService.findAll(queryWrapper);
                    if (expiredOrders != null && !expiredOrders.isEmpty()) {
                        for (Order order : expiredOrders) {
                            order.setStatus(OrderStatusEnum.CLOSED.getCode());
                            orderService.update(order);
                            log.info("自动取消过期订单，订单ID：{}", order.getId());
                        }
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("自动取消订单线程被中断", e);
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("自动取消订单异常", e);
                }
            }
        }).start();
    }

}
