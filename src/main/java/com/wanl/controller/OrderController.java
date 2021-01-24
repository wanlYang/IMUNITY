/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.EsmConstant;
import com.wanl.constant.ImunityConstant;
import com.wanl.controller.OrderController;
import com.wanl.entity.Address;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Order;
import com.wanl.entity.Result;
import com.wanl.service.AccountService;
import com.wanl.service.OrderService;
import com.wanl.service.ShopCartService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/order" })
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ShopCartService shopCartService;

    @RequestMapping(value = { "/down" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result orderDown(@RequestBody String[] cartId, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        Result result = this.orderService.createOrder(cartId);
        Result shopCartPiece = this.shopCartService.getShopCartPiece(user.getId());
        session.setAttribute("SHOP_CART_PIECE", shopCartPiece.getData());
        return result;
    }

    @RequestMapping(value = { "/{id}" }, method = { RequestMethod.GET })
    @ResponseBody
    public ModelAndView orderDetail(@PathVariable("id") String id, ModelAndView modelAndView, HttpSession session) {
        Result result = this.orderService.getOrderInfo(id);
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        if (result.getStatus().intValue() == 0) {
            modelAndView.setViewName("templates/info");
            modelAndView.addObject("message", "数据有误!");
            return modelAndView;
        }
        Order order = (Order) result.getData();
        if (result.getStatus().intValue() != 200 && !user.getId().equals(order.getUser().getId())) {
            modelAndView.setViewName("templates/info");
            modelAndView.addObject("message", "数据有误!");
            return modelAndView;
        }
        List<Address> addresses = null;
        modelAndView.setViewName("templates/order_info");
        modelAndView.addObject("order", order);
        if (order.getStatus().intValue() == EsmConstant.ORDER_NOTPAY.intValue()) {
            if (user != null) {
                addresses = this.orderService.getAddress(user.getId());
            }
            EsmAccount account = this.accountService.get(user.getId());
            modelAndView.addObject("addresses", addresses);
            modelAndView.addObject("account", account);
        }
        return modelAndView;
    }

    @RequestMapping(value = { "/pay" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result pay(String address, String paymode, String orderId, HttpSession session) {
        //EsmUser user = (EsmUser) session.getAttribute("USER_SESSION");
        if (paymode.equals("020")) {
            return this.orderService.payOrder(address, orderId);
        }

        return new Result(Integer.valueOf(-1), "数据有误!");
    }

    @RequestMapping(value = { "/confirm" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result confirm(String orderId, HttpSession session) {
        //EsmUser user = (EsmUser) session.getAttribute("USER_SESSION");
        return this.orderService.confirm(orderId);
    }

    @RequestMapping(value = { "/del" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result clear(String id, HttpSession session) {
        //EsmUser user = (EsmUser) session.getAttribute("USER_SESSION");
        return this.orderService.del(id);
    }

    @RequestMapping(value = { "/list/table" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result list(String orderId, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        List<Order> orders = this.orderService.orderList(user.getId());

        return new Result(Integer.valueOf(200), "获取成功!", Integer.valueOf(0), orders);
    }

    @RequestMapping(value = { "/list" }, method = { RequestMethod.GET })
    public ModelAndView listView(ModelAndView modelAndView) {
        modelAndView.setViewName("templates/order_list");
        return modelAndView;
    }

    @RequestMapping(value = { "/add/address" }, method = { RequestMethod.GET })
    public ModelAndView addAddressPage(ModelAndView modelAndView, String backUrl) {
        modelAndView.setViewName("templates/add_address");
        modelAndView.addObject("backUrl", backUrl);
        return modelAndView;
    }

    @RequestMapping(value = { "/add/address/submit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addAddress(Address address, String city, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        Integer row = this.orderService.addAddress(address, city, user.getId());
        Result result = new Result();
        result.setMessage("添加成功!");
        result.setData(row);
        result.setStatus(Integer.valueOf(200));
        return result;
    }
}
