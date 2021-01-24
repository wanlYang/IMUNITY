// 暴露方法
// layui.define(['layer', 'table'], function (exports) {
//     var table = layui.table;
//     exports('changeItem', function (itemId) {//函数参数
//         //do something
//         alert(itemId)
//     });
//
// });
layui.use(['laypage','form', 'layer','flow', 'jquery', "element",'carousel'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element,
        $ = layui.jquery,
        flow = layui.flow,
        laypage = layui.laypage;

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
                $.each(res.data, function (index, item) {
                    cateHtml += '<button class="layui-btn layui-btn-radius layui-btn-warm" onclick="changeItem('+item.id+');">'+item.title+'<i class="layui-icon"></i></button> ';
                    if (item.children != null) {
                        $.each(item.children, function (index, itemChild) {
                            cateHtml += '<button  class="layui-btn layui-btn-radius"  onclick="changeItem('+itemChild.id+');">' + itemChild.title + '</button>';
                        })
                    }
                })
                $("#catelist-card").html(cateHtml);
            }else{
                layer.msg("出现错误,请尝试刷新页面!");
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });

    /**
     * 点击分类切换分类方法
     * @param itemId
     */

    window.changeItem = function (itemId){
        if (itemId != 0){
            $.ajax({
                url: getRealPath() +"/category/item/title",
                type: 'post',
                data:{"itemId":itemId},
                async:false,
                success: function (data) {
                    $("#itemTitle").html(data.data.title);
                },
                error: function () {
                    layer.msg("出现错误,请尝试刷新页面!");
                }
            });
        }
        $(".slice_item_product").show();
        var count;
        $.ajax({
            url: getRealPath() +"/category/item/count",
            type: 'POST',
            data:{"itemId":itemId},
            async:false,
            success: function (data) {
                count = data.count;
            },
            error: function () {
                layer.msg("出现错误,请尝试刷新页面!");
            }
        });
        $(".sort-total").text("共"+count+"个商品");
        laypage.render({
            elem: 'itemPage'
            ,count: count
            ,limit : 10
            ,layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip']
            ,jump: function(obj){
                $(".slice_item_product").show();
                $.ajax({
                    url: getRealPath() + "/category/item/data",
                    type: 'POST',
                    data:{"itemId":itemId,'curr':obj.curr,'limit':obj.limit},
                    complete: function (XMLHttpRequest, textStatus) {
                        $(".slice_item_product").hide();
                    },
                    success: function (result) {
                        if (result.status == 200){
                            if(result.data.length == 0){
                                $("#itemProduct").html('<button class="layui-btn layui-btn-sm">暂无数据!</button>');
                                $("#itemProduct").css({"text-align":"center","padding":"50px"});
                                return ;
                            }
                            $("#itemProduct").removeAttr("style","text-align");
                            $("#itemProduct").removeAttr("style","padding");
                            var itemProduct = '';
                            $.each(result.data,function (index,item) {
                                itemProduct += '<div class="item-card">';
                                itemProduct += '<a href="'+getRealPath()+'/product/'+item.id+'" title="'+item.mainTitle+'" class="photo">';
                                itemProduct += '<img lay-src="'+item.firstProductImage.img+'" class="cover">';
                                itemProduct += '<div class="name">'+item.mainTitle+'</div>';
                                itemProduct += '</a>';
                                itemProduct += '<div class="middle">';
                                itemProduct += '<div class="price"><small>￥</small>'+item.price+'</div>';
                                itemProduct += '<div class="sale"><button class="layui-btn layui-btn-sm">加入购物车</button></div>';
                                itemProduct += '</div>';
                                itemProduct += '<div class="buttom">';
                                itemProduct += '<div>销量 <b>'+item.buyCount+'</b></div>';
                                itemProduct += '<div>评论 <b>'+item.reviewCount+'</b></div>';
                                itemProduct += '</div>';
                                itemProduct += '</div>';
                            })
                            $("#itemProduct").html(itemProduct);
                        }
                    },
                    error: function () {
                        layer.msg("出现错误,请尝试刷新页面!");
                    }
                });

            }
        });

        //console.log(itemId)
    }
    //首次调用使用
    changeItem(0);
    flow.lazyimg();
})

