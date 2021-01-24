/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import com.wanl.entity.ShopCart;
import com.wanl.mapper.EsmUserMapper;
import com.wanl.mapper.ProductMapper;
import com.wanl.mapper.ShopCartMapper;
import com.wanl.service.ShopCartService;
import com.wanl.utils.RegexUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SwitchingDataSource
public class ShopCartServiceImpl implements ShopCartService {
    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EsmUserMapper userMapper;

    @Transactional(rollbackFor = { Exception.class })
    public Result addShopCart(String id, String amount, EsmUser user) {
        if (!StringUtils.isNotBlank(id) && !StringUtils.isNotBlank(amount) && !StringUtils.isNotBlank(user.getId())) {
            return new Result(Integer.valueOf(-1), "参数错误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        ShopCart shopCart = this.shopCartMapper.getShopCart(Integer.valueOf(Integer.parseInt(id)));
        if (shopCart == null) {
            ShopCart newShopCart = new ShopCart();
            newShopCart.setProduct(this.productMapper.findProductById(Integer.valueOf(Integer.parseInt(id))));
            newShopCart.setUser(this.userMapper.findUserById(user.getId()));
            newShopCart.setAmount(Integer.valueOf(Integer.parseInt(amount)));
            this.shopCartMapper.addShopCart(newShopCart);
        } else {
            Integer newAmount = shopCart.getAmount();
            shopCart.setAmount(Integer.valueOf(newAmount.intValue() + Integer.parseInt(amount)));
            this.shopCartMapper.updateAmount(shopCart);
        }
        List<ShopCart> shopCarts = this.shopCartMapper.getShopCarts(user.getId());

        return new Result(Integer.valueOf(200), "添加成功!", Integer.valueOf(shopCarts.size()), shopCarts);
    }

    public Result getShopCartPiece(String id) {
        Integer piece = this.shopCartMapper.getShopCartPiece(id);

        return new Result(Integer.valueOf(200), "获取成功!", Integer.valueOf(0), piece);
    }

    public List<ShopCart> getShopCartList(String id) {
        EsmUser user = this.userMapper.findUserById(id);
        List<ShopCart> shopCarts = null;
        if (user != null) {
            shopCarts = this.shopCartMapper.getShopCarts(id);
        }

        return shopCarts;
    }

    public Result updateAmount(String id, String amount) {
        ShopCart shopCart = this.shopCartMapper.getShopCartById(Integer.valueOf(Integer.parseInt(id)));
        if (!RegexUtils.isTwoNumber(amount)) {
            return new Result(Integer.valueOf(-1), "请输入1~99之间数值!", Integer.valueOf(0), shopCart);
        }
        shopCart.setAmount(Integer.valueOf(Integer.parseInt(amount)));
        Integer integer = this.shopCartMapper.updateAmount(shopCart);
        if (integer.intValue() <= 0) {
            return new Result(Integer.valueOf(-2), "数据出错!", Integer.valueOf(0), shopCart);
        }
        shopCart.setSubPrice(
                String.valueOf(shopCart.getAmount().intValue() * Double.parseDouble(shopCart.getProduct().getPrice())));
        return new Result(Integer.valueOf(200), "修改成功!", Integer.valueOf(0), shopCart);
    }

    public Result delProduct(String id) {
        if (!StringUtils.isNotBlank(id)) {
            return new Result(Integer.valueOf(-2), "数据出错!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Integer row = this.shopCartMapper.del(id);
        if (row.intValue() <= 0) {
            return new Result(Integer.valueOf(-2), "删除失败!", Integer.valueOf(0), Integer.valueOf(0));
        }
        return new Result(Integer.valueOf(200), "删除成功!", Integer.valueOf(0), Integer.valueOf(0));
    }

    public Result clear(String id) {
        if (!StringUtils.isNotBlank(id)) {
            return new Result(Integer.valueOf(-2), "删除失败!", Integer.valueOf(0), Integer.valueOf(0));
        }
        EsmUser user = this.userMapper.findUserById(id);
        Integer integer = this.shopCartMapper.clear(user.getId());
        if (integer.intValue() <= 0) {
            return new Result(Integer.valueOf(-2), "删除失败!", Integer.valueOf(0), Integer.valueOf(0));
        }
        return new Result(Integer.valueOf(200), "删除成功!", Integer.valueOf(0), Integer.valueOf(0));
    }
}
