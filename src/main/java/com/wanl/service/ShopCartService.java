/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import com.wanl.entity.ShopCart;
import java.util.List;

public interface ShopCartService {
  Result addShopCart(String paramString1, String paramString2, EsmUser paramEsmUser);
  
  Result getShopCartPiece(String paramString);
  
  List<ShopCart> getShopCartList(String paramString);
  
  Result updateAmount(String paramString1, String paramString2);
  
  Result delProduct(String paramString);
  
  Result clear(String paramString);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\ShopCartService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
