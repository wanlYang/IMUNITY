/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.QueryResult;
import com.wanl.annotation.SwitchingDataSource;
import com.wanl.constant.EsmConstant;
import com.wanl.constant.ImunityConstant;
import com.wanl.entity.*;
import com.wanl.mapper.CategoryMapper;
import com.wanl.mapper.ProductImageMapper;
import com.wanl.mapper.ProductMapper;
import com.wanl.mapper.ReviewMapper;
import com.wanl.service.CategoryService;
import com.wanl.service.ProductService;
import com.wanl.service.PropertyValueService;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SwitchingDataSource
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImageMapper productImageMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private CategoryService categoryService;

    public void setProductImages(List<Product> products) {
        for (Product product : products) {
            List<ProductImage> imagesByProductId = this.productImageMapper.findImagesByProductId(product.getId());
            product.setProductSingleImages(imagesByProductId);
            Random random = new Random();
            int n = random.nextInt(imagesByProductId.size());
            product.setFirstProductImage((ProductImage) imagesByProductId.get(n));
        }
    }

    public List<Product> getProductList(Integer page, Integer limit) {
        List<Product> products = this.productMapper.findAllDetails((page.intValue() - 1) * limit.intValue(), limit);
        for (Product product : products) {
            product.setFirstProductImage((ProductImage) product.getProductSingleImages().get(0));
        }
        return products;
    }

    public Integer getProductCount() {
        return this.productMapper.getCount();
    }

    @Transactional(rollbackFor = { Exception.class })
    public Result addProduct(Product product, String imgUrls) {
        if (product == null && !StringUtils.isNotBlank(imgUrls)) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        String[] split = imgUrls.split(",");
        List<String> imgs = new ArrayList<String>();
        for (int i = 0; i < split.length; i++) {
            String img = split[i];
            imgs.add(img);
        }
        product.setReviewCount(Integer.valueOf(0));
        product.setBuyCount(Integer.valueOf(0));
        Category cateById = categoryMapper.findCateById(product.getCategory().getId());
        Category cateById1 = null;
        if (cateById.getParentId() == 0){
            product.setCategory(cateById);
            product.setCateIds(""+cateById.getId());
        }else{
            cateById1 = categoryMapper.findCateById(cateById.getParentId());
            product.setCategory(cateById1);
            product.setCateIds(""+cateById.getId() + "," + cateById1.getId() +",");
        }
        product.setImg((String) imgs.get(0));
        product.setShelfTime(new Date());
        this.productMapper.create(product);
        this.propertyValueService.init(product);
        for (String img : imgs) {
            ProductImage productImage_ = new ProductImage();
            productImage_.setProduct(product);
            productImage_.setImg(img);
            this.productImageMapper.create(productImage_);
        }

        return new Result(Integer.valueOf(200), "添加成功!", Integer.valueOf(0), product);
    }

    @Transactional(rollbackFor = { Exception.class })
    public Result delProduct(String productId) {
        if (!StringUtils.isNotBlank(productId)) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Product product = this.productMapper.findProductById(Integer.valueOf(Integer.parseInt(productId)));
        if (product == null) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        this.productMapper.del(product.getId(), Integer.valueOf(0));
        return new Result(Integer.valueOf(200), "删除成功!", Integer.valueOf(0), product);
    }

    @Transactional(rollbackFor = { Exception.class })
    public Result update(Product product, String imgUrls) {
        if (product == null && !StringUtils.isNotBlank(imgUrls)) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        Product find = this.productMapper.findProductById(product.getId());
        if (find == null) {
            return new Result(Integer.valueOf(-1), "数据有误!", Integer.valueOf(0), Integer.valueOf(0));
        }
        String[] split = imgUrls.split(",");
        List<String> imgs = new ArrayList<String>();
        for (int i = 0; i < split.length; i++) {
            String img = split[i];
            imgs.add(img);
        }
        this.productImageMapper.delByProductId(find.getId());
        find.setMainTitle(product.getMainTitle());
        find.setSubTitle(product.getSubTitle());
        find.setImg((String) imgs.get(0));
        find.setCategory(this.categoryMapper.findCateById(product.getCategory().getId()));
        find.setDetail(product.getDetail());
        find.setIsHot(product.getIsHot());
        find.setOldPrice(product.getOldPrice());
        find.setPrice(product.getPrice());
        find.setStatus(product.getStatus());
        find.setStock(product.getStock());
        this.productMapper.updates(find);
        for (String img : imgs) {
            ProductImage productImage_ = new ProductImage();
            productImage_.setProduct(find);
            productImage_.setImg(img);
            this.productImageMapper.create(productImage_);
        }

        return new Result(Integer.valueOf(200), "修改成功!", Integer.valueOf(0), Integer.valueOf(0));
    }

    public Result recovery(String productId) {
        this.productMapper.recovery(Integer.valueOf(Integer.parseInt(productId)));

        return new Result(Integer.valueOf(200), "修改成功!", Integer.valueOf(0), Integer.valueOf(0));
    }

    @Cacheable(value = { "index_hot_product" }, key = "#root.methodName")
    public List<Product> getHotproduct() {
        List<Product> hotProducts = this.productMapper.findHotProduct();
        setProductImages(hotProducts);
        return hotProducts;
    }

    @Override
    public List<Product> getAppletHotproduct(Integer index,Integer limit) {
        List<Product> hotProducts = this.productMapper.findAppletHotProduct(index,limit);
        setProductImages(hotProducts);
        return hotProducts;
    }

    @Cacheable(value = { "index_skirt_product" }, key = "#root.methodName")
    public List<Product> getSkirtProduct() {
        return getCateProduct(EsmConstant.CATE_SKIRT, Integer.valueOf(8));
    }

    public List<Product> getRandom(List<Product> products, int count) {
        Random index = new Random();

        List<Integer> indexList = new ArrayList<Integer>();
        List<Product> newList = new ArrayList<Product>();
        for (int i = 0; i < count; i++) {

            int j = index.nextInt(products.size());

            if (!indexList.contains(Integer.valueOf(j))) {

                indexList.add(Integer.valueOf(j));
                newList.add(products.get(j));
            } else {
                i--;
            }
        }
        return newList;
    }

    @Cacheable(value = { "index_clothes_product" }, key = "#root.methodName")
    public List<Product> getClothesProduct() {
        return getCateProduct(EsmConstant.CATE_CLOTHES, Integer.valueOf(8));
    }

    public List<Product> getCateProduct(Integer id, Integer count) {



        List<Product> products = this.productMapper.findProductByCategoryId(id);


        List<Product> random = getRandom(products, count.intValue());
        setProductImages(random);
        return random;
    }

    @Cacheable(value = { "index_booties_product" }, key = "#root.methodName")
    public List<Product> getBootiesProduct() {
        return getCateProduct(EsmConstant.CATE_BOOTIES, Integer.valueOf(8));
    }

    @Cacheable(value = { "product_detail" }, key = "#root.args[0]")
    public Product getProduct(Integer id) {
        Product product = this.productMapper.findProductById(id);
        List<ProductImage> images = this.productImageMapper.findImagesByProductId(id);
        product.setProductSingleImages(images);
        product.setFirstProductImage((ProductImage) images.get(0));
        product.setReviewCount(this.reviewMapper.getCount(id));
        return product;
    }

    @Cacheable(value = { "index_recommend_product" }, key = "#root.args[0]")
    public List<Product> getRecommendProduct(Integer id) {
        Product product = this.productMapper.findProductById(id);
        Integer parentId = product.getCategory().getParentId();
        if (parentId.intValue() != 0) {
            return getCateProduct(parentId, Integer.valueOf(9));
        }
        return getCateProduct(EsmConstant.CATE_SKIRT, Integer.valueOf(9));
    }

    @Override
    public List<Product> getProductByCate(Integer id) {
        List<Product> products = new ArrayList<>();
        List<Product> productByCategoryId = productMapper.findAppletProductByCategoryId(id,0,4);
        if (productByCategoryId != null || productByCategoryId.size() > 0){
            products.addAll(productByCategoryId);
        }

        return products;
    }

    @Override
    public List<Product> getAppletProductByCate(Integer index, Integer page) {
        List<Product> products = new ArrayList<>();
        List<Product> appletProductByCategoryId = productMapper.findAppletProductByCategoryId(index, page * 4, 4);
        products.addAll(appletProductByCategoryId);
        return products;
    }

    /**
     * @Author Administrator
     * @Description //商品分类页面，按照分类数据分页查询
     * @Date 15:38 2020-12-12
     * @Param [itemId, curr, limit]
     * @return java.util.List<com.wanl.entity.Product>
     **/
    @Override
    public List<Product> getProductByCategoryPage(Integer itemId,Integer curr,Integer limit) {
        List<Product> products = null;
        if (itemId == 0){
            products = productMapper.findAll((curr.intValue() - 1) * limit, limit);
            setProductImages(products);
            for (Product p: products) {
                p.setReviewCount(this.reviewMapper.getCount(p.getId()));
            }
            return products;
        }

        products = productMapper.findProductByCategoryIdPage(itemId,(curr.intValue() - 1) * limit, limit);
        for (Product p: products) {
            p.setReviewCount(this.reviewMapper.getCount(p.getId()));
        }
        setProductImages(products);
        return products;
    }

    /**
     * @param itemId
     * @return java.lang.Integer
     * @Author Administrator
     * @Description //根据分类获取对应产品数量
     * @Date 15:04 2020-12-12
     * @Param [itemId]
     */
    @Override
    public Integer getProductCountByCategory(Integer itemId) {
        Integer count = 0;
        if(itemId == 0){
            //如果itemId是0 就获取所有商品数量
            count = productMapper.getCount();
            return count;
        }
        count = productMapper.findProductCountByCategory(itemId);


        return count;
    }

    /**
     * @param page
     * @param size
     * @return
     * @Author Administrator
     * @Description //获取商品分页数据
     * @Date 12:52 2020-12-31
     * @Param [page, size]
     */
    @Override
    public Map<String,Object> findProductPages(int page, int size) {
        if (page <= 0){
            page = 1;
        }
        if (page >= 5){
            page = 5;
        }

        //开始分页
        PageHelper.startPage(page,size);
        //获取分页对象
        Page<Product> pages = productMapper.findAllHelper();
        PageInfo<Product> productPageInfo = new PageInfo<Product>(pages);

        //从分页对象中获取查询列表
        List<Product> products = pages.getResult();
        //获取总数
        long total = pages.getTotal();
        //查询对象
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageInfo",productPageInfo);
        map.put("products",products);
        map.put("total",total);
        return map;
    }
}
