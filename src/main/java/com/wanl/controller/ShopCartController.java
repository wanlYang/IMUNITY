/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.ImunityConstant;
import com.wanl.controller.ShopCartController;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import com.wanl.entity.ShopCart;
import com.wanl.service.ShopCartService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/shopcart" })
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;

    @RequestMapping(value = { "/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addShopCart(String id, String amount, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        if (user == null) {
            return new Result(Integer.valueOf(-1), "参数异常!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Result result = this.shopCartService.addShopCart(id, amount, user);
        session.setAttribute("SHOP_CART_PIECE", result.getCount());
        return result;
    }

    @RequestMapping(value = { "/piece" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result piece(HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        if (user == null) {
            return new Result(Integer.valueOf(-1), "参数异常!", Integer.valueOf(0), Integer.valueOf(0));
        }
        return this.shopCartService.getShopCartPiece(user.getId());
    }

    @RequestMapping(value = { "/list" }, method = { RequestMethod.GET })
    public ModelAndView shopCart(HttpSession session, ModelAndView modelAndView) {
        modelAndView.setViewName("templates/shopcart");
        return modelAndView;
    }

    @RequestMapping(value = { "/list/table" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result shopCartTable(HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        List<ShopCart> shopCarts = null;
        if (user != null) {
            shopCarts = this.shopCartService.getShopCartList(user.getId());
        }
        return new Result(Integer.valueOf(200), "获取成功", Integer.valueOf(0), shopCarts);
    }

    @RequestMapping(value = { "/change/amount" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result changeAmount(String id, String amount) {
        return this.shopCartService.updateAmount(id, amount);
    }

    @RequestMapping(value = { "/del/product" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delProduct(String id, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        if (user == null) {
            return new Result(Integer.valueOf(-2), "删除失败!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Result result = this.shopCartService.delProduct(id);
        Result shopCartPiece = this.shopCartService.getShopCartPiece(user.getId());
        session.setAttribute("SHOP_CART_PIECE", shopCartPiece.getData());
        return result;
    }

    @RequestMapping(value = { "/clear" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result clear(HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        if (user == null) {
            return new Result(Integer.valueOf(-2), "删除失败!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Result result = this.shopCartService.clear(user.getId());
        Result shopCartPiece = this.shopCartService.getShopCartPiece(user.getId());
        session.setAttribute("SHOP_CART_PIECE", shopCartPiece.getData());
        return result;
    }
}
