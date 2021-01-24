/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.ShopCart;
import java.util.List;

public interface ShopCartMapper {
    Integer addShopCart(ShopCart paramShopCart);

    ShopCart getShopCart(Integer paramInteger);

    ShopCart getShopCartById(Integer paramInteger);

    Integer updateAmount(ShopCart paramShopCart);

    List<ShopCart> getShopCarts(String paramString);

    Integer getShopCartPiece(String paramString);

    Integer del(String paramString);

    Integer clear(String paramString);
}

