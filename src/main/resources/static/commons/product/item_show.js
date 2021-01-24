layui.use(['form', 'layer','flow', 'jquery', "element",'carousel'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element,
        $ = layui.jquery,
        carousel = layui.carousel,
        flow = layui.flow;
    var showproduct = {
        "boxid":"showbox",
        "sumid":"showsum",
        "boxw":360,//宽度,该版本中请把宽高填写成一样
        "boxh":360,//高度,该版本中请把宽高填写成一样
        "sumw":55,//列表每个宽度,该版本中请把宽高填写成一样
        "sumh":55,//列表每个高度,该版本中请把宽高填写成一样
        "sumi":7,//列表间隔
        "sums":5,//列表显示个数
        "sumsel":"sel",
        "sumborder":1,//列表边框，没有边框填写0，边框在css中修改
        "lastid":"showlast",
        "nextid":"shownext"
    };//参数定义

    $.ljsGlasses.pcGlasses(showproduct);//方法调用，务必在加载完后执行
    var options = {
        elem: '#hotProduct',
        width: '100%', //设置容器宽度
        arrow: 'hover', //始终显示箭头
        anim: 'updown' ,//切换动画方式
        height: '510px'
    }
    var optionsTwo = {
        elem: '#ProductInfoHotProduct'
        ,width: '100%' //设置容器宽度
        ,arrow: 'always' //始终显示箭头
        ,height: '170px'
        ,autoplay:false
        //,anim: 'updown' //切换动画方式
    }
    var insTwo = carousel.render(optionsTwo);
    var ins = carousel.render(options);
    //获取热门商品
    $.ajax({
        url: getRealPath() + "/index/hot/product",
        type: 'GET',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_info_box").hide();
        },
        success: function (result) {
            if (result.status == 200){
                var hotHtml = '';
                var len = result.data.length;
                $.each(result.data,function (index,item) {
                    if (index == 0) {
                        hotHtml += '<div class="item-box">';
                    }
                    hotHtml += '<a href="/product/' + item.id + '" title="'+item.mainTitle+'" class="picked-item">';
                    hotHtml += '<img lay-src="' + item.firstProductImage.img + '" class="cover">';
                    hotHtml += '<div class="look_price">￥'+item.price+'</div>';
                    hotHtml += '</a>';
                    if ((index+1)%3==0&&(index<(len-1))) {
                        hotHtml += '</div><div class="item-box">';
                    }
                    if(index==len-1){
                        hotHtml+='</div>';
                    }

                })
                $("#ProductInfoHotProductBox").html(hotHtml);
                ins.reload(options);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });
    //获取推荐商品
    $.ajax({
        url: getRealPath() + "/product/recommend",
        type: 'POST',
        complete: function (XMLHttpRequest, textStatus) {
            $(".slice_recommend_box").hide();
        },
        data:{"id":$("#productId").val()},
        success: function (result) {
            if (result.status == 200){
                var foorHtml = '';
                var len = result.data.length;
                $.each(result.data,function (index,item) {
                    if (index == 0) {
                        foorHtml += '<div class="item-box">';
                    }
                    foorHtml += '<a href="/product/' + item.id + '" title="'+item.mainTitle+'" class="picked-item-footer">';
                    foorHtml += '<img lay-src="' + item.firstProductImage.img + '" class="cover">';
                    foorHtml += '<div class="look_price">￥'+item.price+'</div>';
                    foorHtml += '</a>';
                    if ((index+1)%4==0&&(index<(len-1))) {
                        foorHtml += '</div><div class="item-box">';
                    }
                    if(index==len-1){
                        foorHtml+='</div>';
                    }

                })
                $("#ProductInfoHotProductBoxFoot").html(foorHtml);
                insTwo.reload(optionsTwo);
            }
        },
        error: function () {
            layer.msg("出现错误,请尝试刷新页面!");
        }
    });

    var cur = $('#amount-input').val();
    $('.amount-btn .amount-but').on('click',function(){
        if($(this).hasClass('add')){
            cur++;

        }else{
            if(cur > 1){
                cur--;
            }
        }
        $('#amount-input').val(cur)
    })

    form.on('submit(addCart)', function(data){

        //添加购物车
        $.ajax({
            url: getRealPath() + "/shopcart/add",
            type: 'POST',
            complete: function (XMLHttpRequest, textStatus) {

            },
            data:data.field,
            success: function (result) {
                if (result.status != 200) {
                    layer.msg(result.message);
                    setTimeout(function() {
                        location.href = getRealPath() + "/login";
                    }, 1000);
                }else{
                    layer.msg(result.message);
                    $("#cartAmount").html(result.count + " 件");
                }
            },
            error: function () {
                layer.msg("出现错误,请尝试刷新页面!");
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    flow.lazyimg();
})


