/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.BackStageProductController;
import com.wanl.entity.Category;
import com.wanl.entity.Product;
import com.wanl.entity.PropertyValue;
import com.wanl.entity.Result;
import com.wanl.service.CategoryService;
import com.wanl.service.ProductService;
import com.wanl.service.PropertyValueService;
import com.wanl.utils.UUIDUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/admin/manager/shop/product" })
public class BackStageProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PropertyValueService propertyValueService;

    @RequestMapping(value = { "/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result categoryList(Integer page, Integer limit) {
        Result result = new Result();
        List<Product> products = this.productService.getProductList(page, limit);
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(products);
        result.setCount(this.productService.getProductCount());
        return result;
    }

    @RequestMapping(value = { "/category/get" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result category(Integer id) {
        Result result = new Result();
        Category category = this.categoryService.getCateGory(id);
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(category);
        return result;
    }

    @RequestMapping(value = { "/category" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result categoryAll() {
        Result result = new Result();
        List<Category> cateGory = this.categoryService.getAllCateGory();
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setData(cateGory);
        return result;
    }

    @RequestMapping(value = { "/count" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result count() {
        Result result = new Result();
        Integer integer = this.productService.getProductCount();
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        result.setCount(integer);
        return result;
    }

    @RequestMapping(value = { "/submit/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result add(Product product, String imgUrls, HttpSession session) {
        String productId = (String) session.getAttribute("productId");
        if (!StringUtils.isNotBlank(productId)) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        product.setId(Integer.valueOf(Integer.parseInt(productId)));
        Result result = this.productService.addProduct(product, imgUrls);
        if (result.getStatus().intValue() == 200) {
            session.removeAttribute("productId");
        }
        return result;
    }

    @RequestMapping(value = { "/submit/del" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result del(String productId) {
        return this.productService.delProduct(productId);
    }

    @RequestMapping(value = { "/submit/recovery" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result recovery(String productId) {
        return this.productService.recovery(productId);
    }

    @RequestMapping(value = { "/submit/edit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result update(Product product, String imgUrls) {
        return this.productService.update(product, imgUrls);
    }

    @RequestMapping(value = { "/property/page" }, method = { RequestMethod.GET })
    public ModelAndView getPropertyValue(String id, ModelAndView modelAndView) {
        List<PropertyValue> propertyValues = this.propertyValueService
                .getPropertyValue(Integer.valueOf(Integer.parseInt(id)));
        modelAndView.addObject("propertyValues", propertyValues);
        modelAndView.setViewName("admin/templates/shop/product/edit_product_value");
        return modelAndView;
    }

    @RequestMapping(value = { "/property/submit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result editPropertyValue(@RequestParam("id") String id, @RequestParam("value") String value) {
        return this.propertyValueService.update(id, value);
    }

    @ResponseBody
    @RequestMapping(value = { "/upload" }, method = { RequestMethod.POST })
    public Map<String, Object> uploadImage(@RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "type", required = false) String type, HttpServletRequest request,
            HttpSession session, @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String rootPath = request.getServletContext().getRealPath("/productimg/");
        String res = sdf.format(new Date());
        String originalFilename = file.getOriginalFilename();
        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));
        Calendar calendar = Calendar.getInstance();

        String productId = "";
        if ("EDIT".equals(type)) {
            productId = id;
        } else if ("ADD".equals(type)) {
            String integer = (String) session.getAttribute("productId");
            if (integer == null) {
                productId = UUIDUtils.generateUID();
                session.setAttribute("productId", productId);
            } else {
                productId = integer;
            }
        }
        File dateDirs = new File(calendar.get(1) + File.separator + (calendar.get(2) + 1));
        File newFile = new File(
                rootPath + File.separator + dateDirs + File.separator + productId + File.separator + newFileName);

        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        file.transferTo(newFile);

        String fileUrl = request.getScheme()+"://"+ request.getServerName()+"/productimg/" + calendar.get(1) + "/" + (calendar.get(2) + 1) + "/" + productId + "/"
                + newFileName;
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map_data = new HashMap<String, Object>();
        map.put("code", Integer.valueOf(0));
        map.put("msg", "上传成功!");
        map_data.put("src", fileUrl);
        map_data.put("title", newFileName);
        map_data.put("productId", productId);
        map.put("data", map_data);
        return map;
    }
}
