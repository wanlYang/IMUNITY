/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.config.MerchantConfig;
import com.wanl.entity.Article;
import com.wanl.entity.Result;
import com.wanl.service.ArticleService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping({ "/admin/manager/article" })
public class BackStageArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private MerchantConfig merchantConfig;

    @ResponseBody
    @RequestMapping(value = { "/richtext_img_upload" }, method = { RequestMethod.POST })
    public Map<String, Object> upload_Image(HttpServletRequest request, @RequestParam("upload_file") MultipartFile file)
            throws IllegalStateException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String rootPath = request.getServletContext().getRealPath("/community/article/");
        String res = sdf.format(new Date());
        String originalFilename = file.getOriginalFilename();
        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));
        Calendar calendar = Calendar.getInstance();

        File dateDirs = new File(calendar.get(1) + File.separator + (calendar.get(2) + 1));
        File newFile = new File(rootPath + File.separator + dateDirs + File.separator + newFileName);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        try {
            Thumbnails.of(new InputStream[] { file.getInputStream() }).scale(0.5D)
                    .watermark(Positions.BOTTOM_LEFT,
                            ImageIO.read(new File(request.getServletContext()
                                    .getRealPath(merchantConfig.getImunityWatermark()))),
                            0.6F)
                    .toFile(newFile);
        } catch (IOException e) {
            file.transferTo(newFile);
        }

        String fileUrl = "/community/article/" + calendar.get(1) + "/" + (calendar.get(2) + 1) + "/" + newFileName;
        Map<String, Object> map_data = new HashMap<String, Object>();
        map_data.put("success", Boolean.valueOf(true));
        map_data.put("msg", "上传成功!");
        map_data.put("file_path",
                request.getScheme() + "://" + request.getServerName() + request.getContextPath() + fileUrl);
        map_data.put("title", newFileName);
        return map_data;
    }

    @ResponseBody
    @RequestMapping(value = { "/upload/thumbnail" }, method = { RequestMethod.POST })
    public Map<String, Object> uploadImage(HttpServletRequest request, @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String rootPath = request.getServletContext().getRealPath("/community/article/");
        String res = sdf.format(new Date());
        String originalFilename = file.getOriginalFilename();
        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));
        Calendar calendar = Calendar.getInstance();

        File dateDirs = new File(calendar.get(1) + File.separator + (calendar.get(2) + 1));
        File newFile = new File(rootPath + File.separator + dateDirs + File.separator + newFileName);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        try {
            Thumbnails.of(new InputStream[] { file.getInputStream() }).scale(1.0D)
                    .watermark(Positions.BOTTOM_LEFT,
                            ImageIO.read(new File(request.getServletContext()
                                    .getRealPath(merchantConfig.getImunityWatermark()))),
                            0.6F)
                    .toFile(newFile);
        } catch (IOException e) {
            file.transferTo(newFile);
        }

        String fileUrl = "/community/article/" + calendar.get(1) + "/" + (calendar.get(2) + 1) + "/" + newFileName;
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map_data = new HashMap<String, Object>();
        map.put("code", Integer.valueOf(0));
        map.put("msg", "上传成功!");
        map_data.put("src", request.getContextPath() + fileUrl);
        map_data.put("title", newFileName);
        map.put("data", map_data);
        return map;
    }

    @RequestMapping(value = { "/submit/article/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addArticle(Article article) {
        Result result = new Result();
        Integer row = this.articleService.addArticle(article);
        if (row.intValue() > 0) {
            result.setMessage("添加成功!");
            result.setStatus(Integer.valueOf(200));
            result.setData(row);
            return result;
        }
        result.setMessage("添加失败!");
        result.setStatus(Integer.valueOf(-1));
        result.setData(row);
        return result;
    }

    @RequestMapping(value = { "/get/article/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result list(Integer page, Integer limit) {
        Result result = new Result();
        List<Article> articles = this.articleService.getArticleList(page, limit);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(articles);
        result.setCount(this.articleService.getAllCount());
        return result;
    }

    @RequestMapping(value = { "/submit/article/edit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result editArticle(Article article) {
        Result result = new Result();
        Integer row = this.articleService.editArticle(article);
        if (row.intValue() > 0) {
            result.setMessage("编辑成功!");
            result.setStatus(Integer.valueOf(200));
            result.setData(row);
            return result;
        }
        result.setMessage("编辑失败!");
        result.setStatus(Integer.valueOf(-1));
        result.setData(row);
        return result;
    }

    @RequestMapping(value = { "/submit/article/del" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delArticle(Integer id) {
        Result result = new Result();
        Integer row = this.articleService.delArticle(id);
        if (row.intValue() > 0) {
            result.setMessage("删除成功!");
            result.setStatus(Integer.valueOf(200));
            result.setData(row);
            return result;
        }
        result.setMessage("删除失败!");
        result.setStatus(Integer.valueOf(-1));
        result.setData(row);
        return result;
    }

    @RequestMapping(value = { "/submit/article/batch/del" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result batchDelArticle(@RequestBody Integer[] newsId) {
        Result result = new Result();
        Integer row = this.articleService.delArticleBatch(newsId);
        if (row.intValue() > 0) {
            result.setMessage("删除成功!");
            result.setStatus(Integer.valueOf(200));
            result.setData(row);
            return result;
        }
        result.setMessage("删除失败!");
        result.setStatus(Integer.valueOf(-1));
        result.setData(row);
        return result;
    }

    @RequestMapping(value = { "/get/article/count" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result getCount() {
        Result result = new Result();
        Integer row = this.articleService.getAllCount();
        if (row != null) {
            result.setMessage("获取成功!");
            result.setStatus(Integer.valueOf(200));
            result.setCount(row);
            return result;
        }
        result.setMessage("获取失败!");
        result.setStatus(Integer.valueOf(-1));
        result.setCount(null);
        return result;
    }

    @RequestMapping(value = { "/submit/article/top" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result changeIsTop(Integer id) {
        Result result = new Result();
        Integer row = this.articleService.changeIsTop(id);
        if (row != null && row.intValue() > 0) {
            result.setMessage("修改成功!");
            result.setStatus(Integer.valueOf(200));
            result.setCount(row);
            result.setData(null);
            return result;
        }
        result.setMessage("修改失败!");
        result.setStatus(Integer.valueOf(-1));
        result.setCount(null);
        return result;
    }
}
