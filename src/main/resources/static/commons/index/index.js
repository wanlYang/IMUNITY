layui.use(['form', 'layer','flow', 'jquery', "element",'carousel'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element,
        $ = layui.jquery,
        carousel = layui.carousel,
        flow = layui.flow;
    //建造轮播实例
    carousel.render({
        elem: '#indexcar'
        ,width: '100%' //设置容器宽度
        ,arrow: 'always' //始终显示箭头
        ,height: '450px'
        //,anim: 'updown' //切换动画方式
    });

    // 楼层导航自动 active
    $.scrollFloor();
    // 页面下拉固定楼层导航
    $('.floor-nav').smartFloat();
    $('.to-top').toTop({position: false});

    //获取导航数据
    $.ajax({
        url: getRealPath() + "/category/list",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_cate").hide();
        },
        success: function (res) {
            if (res.status == 200) {
                var cateHtml = '';
                var flag = 0;
                $.each(res.data, function (index, item) {
                    cateHtml += '<div class="cat-box">';
                    cateHtml += '<div class="title">';
                    cateHtml += '<i class="layui-icon layui-icon-component"></i> ' + item.title + '';
                    cateHtml += '</div>';
                    cateHtml += '<ul class="cat-list clearfix">';
                    if (item.children != null) {
                        $.each(item.children, function (index, itemChild) {
                            flag += 1;
                            cateHtml += '<li>' + itemChild.title + '</li>';
                            if (flag == 2) {
                                return false;
                            }
                        })
                        flag = 0;
                    }
                    cateHtml += '</ul>';
                    cateHtml += '<div class="cat-list__deploy">';
                    cateHtml += '<div class="deploy-box">';
                    $.each(item.children, function (index, itemChild) {
                        cateHtml += '<div class="genre-box clearfix">';
                        cateHtml += '<a style="text-decoration:none;"><span class="title">' + itemChild.title + '</span></a>';
                        cateHtml += '</div>';
                    })
                    cateHtml += '</div>';
                    cateHtml += '</div>';
                    cateHtml += '</div>';
                })
                $("#categoryBox").html(cateHtml);
            }else{
                layer.msg("出现错误,请尝试刷新页面!");
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });


    //获取热门商品
    $.ajax({
        url: getRealPath() + "/index/hot/product",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_hot").hide();
        },
        success: function (result) {
            if (result.status == 200){
                var hotHtml = '';
                $.each(result.data,function (index,item) {
                    hotHtml += '<a href="product/' + item.id + '" title="' + item.mainTitle + '"  class="floor-item">';
                    hotHtml += '<div class="item-img hot-img">';
                    hotHtml += '<img lay-src="' + item.firstProductImage.img + '" class="cover">';
                    hotHtml += '</div>';
                    hotHtml += '<div class="price clearfix">';
                    hotHtml += '<span class="pull-left cr fz16">￥' + item.price + '</span>';
                    hotHtml += '<span class="pull-right c6">进货价</span>';
                    hotHtml += '</div>';
                    hotHtml += '<div class="name ep" title="'+item.mainTitle+'">'+item.mainTitle+'</div>';
                    hotHtml += '</a>';
                })
                $("#index_hot_box").html(hotHtml);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });


    //获取襦裙子分类
    $.ajax({
        url: getRealPath() + "/category/list/skirt",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {

        },
        success: function (result) {
            if (result.status == 200){
                var skirt = '';
                $.each(result.data,function (index,item) {
                    skirt += '<a href="category/' + item.id + '">' + item.title + '</a>';
                })
                $("#skirt").html(skirt);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });


    //获取襦裙分类商品
    $.ajax({
        url: getRealPath() + "/index/product/skirt",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_skirt_product").hide();
        },
        success: function (result) {
            if (result.status == 200){
                var skirtProduct = '';
                $.each(result.data,function (index,item) {
                    skirtProduct += '<a href="product/'+item.id+'" title="'+item.mainTitle+'" class="floor-item">';
                    skirtProduct += '<div class="item-img hot-img">';
                    skirtProduct += '<img lay-src="'+item.firstProductImage.img+'" class="cover">';
                    skirtProduct += '</div>';
                    skirtProduct += '<div class="price clearfix">';
                    skirtProduct += '<span class="pull-left cr fz16">￥'+item.price+'</span>';
                    skirtProduct += '<span class="pull-right c6">进货价</span>';
                    skirtProduct += '</div>';
                    skirtProduct += '<div class="name ep" title="'+item.mainTitle+'">'+item.mainTitle+'</div>';
                })
                $("#skirtProduct").html(skirtProduct);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });
    //获取衣裳子分类
    $.ajax({
        url: getRealPath() + "/category/list/clothes",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {

        },
        success: function (result) {
            if (result.status == 200){
                var clothes = '';
                $.each(result.data,function (index,item) {
                    clothes += '<a href="category/' + item.id + '">' + item.title + '</a>';
                })
                $("#clothes").html(clothes);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });
    //获取衣裳分类商品
    $.ajax({
        url: getRealPath() + "/index/product/clothes",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_clothes_product").hide();
        },
        success: function (result) {
            if (result.status == 200){
                var clothesProduct = '';
                $.each(result.data,function (index,item) {
                    clothesProduct += '<a href="product/'+item.id+'" title="'+item.mainTitle+'" class="floor-item">';
                    clothesProduct += '<div class="item-img hot-img">';
                    clothesProduct += '<img lay-src="'+item.firstProductImage.img+'" class="cover">';
                    clothesProduct += '</div>';
                    clothesProduct += '<div class="price clearfix">';
                    clothesProduct += '<span class="pull-left cr fz16">￥'+item.price+'</span>';
                    clothesProduct += '<span class="pull-right c6">进货价</span>';
                    clothesProduct += '</div>';
                    clothesProduct += '<div class="name ep" title="'+item.mainTitle+'">'+item.mainTitle+'</div>';
                })
                $("#clothesProduct").html(clothesProduct);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });
    //获取鞋靴子分类
    $.ajax({
        url: getRealPath() + "/category/list/booties",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {

        },
        success: function (result) {
            if (result.status == 200){
                var booties = '';
                $.each(result.data,function (index,item) {
                	booties += '<a href="category/' + item.id + '">' + item.title + '</a>';
                })
                $("#booties").html(booties);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });
    
    //获取鞋靴分类商品
    $.ajax({
        url: getRealPath() + "/index/product/booties",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_booties_product").hide();
        },
        success: function (result) {
            if (result.status == 200){
                var bootiesProduct = '';
                $.each(result.data,function (index,item) {
                	bootiesProduct += '<a href="product/'+item.id+'" title="'+item.mainTitle+'" class="floor-item">';
                    bootiesProduct += '<div class="item-img hot-img">';
                    bootiesProduct += '<img lay-src="'+item.firstProductImage.img+'" class="cover">';
                    bootiesProduct += '</div>';
                    bootiesProduct += '<div class="price clearfix">';
                    bootiesProduct += '<span class="pull-left cr fz16">￥'+item.price+'</span>';
                    bootiesProduct += '<span class="pull-right c6">进货价</span>';
                    bootiesProduct += '</div>';
                    bootiesProduct += '<div class="name ep" title="'+item.mainTitle+'">'+item.mainTitle+'</div>';
                })
                $("#bootiesProduct").html(bootiesProduct);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });
    flow.lazyimg();
})


