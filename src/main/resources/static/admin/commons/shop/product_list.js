layui.use(['form', 'layer', 'laydate', 'table', 'laytpl'], function() {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    var tableIns = table.render({
        elem: '#productList',
        url: getRealPath() + '/admin/manager/shop/product/list',
        cellMinWidth: 100,
        page: true,
        limit: 15,
        height: "full-125",
        limits: [10, 15, 20, 25],
        method:"post",
        id: "productList",
        cols: [
            [{
                    field: 'id',
                    title: 'ID',
                    width:100,
                    sort:true,
                    align: "center"
                },
                {
                    field: 'category',
                    title: '所属分类',
                    width:100,
                    templet:function(d){
                        return '<span class="layui-btn layui-btn-xs">'+d.category.title+'</span>';
                    }
                },
                {
                    field: 'mainTitle',
                    width:100,
                    title: '商品名称',
                    templet:function(d){
                        return "<a target='_blank' title='点击查看' href='/product/"+d.id+"'>"+d.mainTitle+"</a>";
                    }
                },
                {
                    field: 'price',
                    title: '现价',
                    width:90,
                    align: 'center',
                    sort:true,
                    templet:function(d){
                        return d.price + "￥";
                    }
                },
                {
                    field: 'oldPrice',
                    title: '原价',
                    align: 'center',
                    width:90,
                    sort:true,
                    templet:function(d){
                        return d.oldPrice+ "￥";
                    }
                },
                {
                    field: 'buyCount',
                    title: '购买数量',
                    sort:true,
                    width:50,
                    align: 'center',
                    templet:function(d){
                        return '<span class="layui-btn layui-btn-xs">'+d.buyCount+'</span>';
                    }
                },
                {
                    field: 'img',
                    title: '单图',
                    align: 'center',
                    event: 'preview',
                    style: 'cursor: pointer;',
                    templet:function(d){
                        return "<img title='点击预览' src='"+d.img+"' class='cover'/>";
                    }
                },
                {
                    field: 'detail',
                    title: '详情介绍',
                    align: 'center',
                    templet:function(d){
                        return d.detail;
                    }
                },
                {
                    field: 'status',
                    title: '状态',
                    align: 'center',
                    templet:function(d){
                        if (d.status == 1){
                            return '<span class="layui-btn layui-btn-xs layui-bg-blue">正常</span>';
                        }else{
                            return '<span class="layui-btn layui-btn-xs layui-bg-red">异常</span>';
                        }
                    }
                },
                {
                    field: 'isHot',
                    title: '热门',
                    align: 'center',
                    templet:function(d){
                        if (d.isHot == 1){
                            return '<span class="layui-btn layui-btn-xs layui-bg-red">热门</span>';
                        }else{
                            return '<span class="layui-btn layui-btn-xs layui-bg-black">非热门</span>';
                        }
                    }
                },
                {
                    field: 'stock',
                    title: '库存',
                    align: 'center',
                    sort:true,
                    templet:function(d){
                        return d.stock;
                    }
                },
                {
                    field: 'shelfTime',
                    title: '上架时间',
                    align: 'center',
                    sort:true,
                    templet:function(d){
                        return Format(d.shelfTime, "yyyy-MM-dd")
                    }
                },
                {
                    title: '操作',
                    align: 'center',
                    templet:"#productListBar"
                }
            ]
        ],
        text: {
            none: '暂无相关数据' // 默认：无数据。注：该属性为 layui 2.2.5 开始新增
        },
        response: {
            statusName: 'status', // 规定数据状态的字段名称，默认：code
            statusCode: 200, // 规定成功的状态码，默认：0
            msgName: 'message', // 规定状态信息的字段名称，默认：msg
            countName: 'count', // 规定数据总数的字段名称，默认：count
            dataName: 'data' // 规定数据列表的字段名称，默认：data
        }
    });
    //显示大图方法
    function preview_img(url) {
        var img = new Image();
        img.src = url;
        var height = img.height + 44; //获取图片高度
        var width = img.width; //获取图片宽度
        var imgHtml = "<img src='" + url + "' />";
        //弹出层
        layer.open({
            type: 1,
            shade: 0.8,
            offset: 'auto',
            time:3000,
            area: [width + 'px',height+'px'],
            shadeClose:true,//点击外围关闭弹窗
            scrollbar: false,//不现实滚动条
            title: "图片预览", //不显示标题
            content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响

        });
    }
    //新增分类
    $(".addProduct_btn").click(function(){
        var index = layui.layer.open({
            title: "添加商品",
            type: 2,
            content: getRealPath() + "/admin/manager/shop/product/add/page",
            success: function(layero, index) {
                setTimeout(function() {
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            },
            end: function() {
                $(window).unbind("resize");
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index", index);
        // 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize", function() {
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    });

    //列表操作
    table.on('tool(productList)', function(obj) {
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit') { //编辑
            var index = layui.layer.open({
                title: "编辑商品",
                type: 2,
                content: getRealPath() + "/admin/manager/shop/product/edit/page",
                success: function(layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    var iframeWindow = window[layero.find('iframe')[0]['name']];
                    body.find(".id").val(data.id);
                    body.find(".mainTitle").val(data.mainTitle);
                    body.find(".subTitle").val(data.subTitle);
                    body.find(".price").val(data.price);
                    body.find(".oldPrice").val(data.oldPrice);
                    body.find(".detail").val(data.detail);
                    body.find(".status").val(data.status);
                    body.find(".isHot").val(data.isHot);
                    body.find(".stock").val(data.stock);
                    var temp = "";
                    for (var i = 0;i < data.productSingleImages.length;i ++) {
                        temp = temp + data.productSingleImages[i].img + ",";
                    }
                    body.find("#imgUrls").val(temp);
                    for (var i = 0;i < data.productSingleImages.length;i ++) {
                        body.find('#show').append('<div style="height: 210px;width: 210px;float: left;margin-left: 10px;margin-top: 10px;"><img src="' + data.productSingleImages[i].img
                            + '" alt="' + data.mainTitle
                            +'"class="cover layui-upload-img uploadImgPreView"></div>')
                    }
                    // 获取所有分类
                    $.ajax({
                        type: "POST",
                        url: getRealPath() + "/admin/manager/shop/product/category",
                        data:{"flag":2},
                        success: function(result) {
                            if(result.status == 200) {
                                var navHtml = '';
                                $.each(result.data, function(index,item) {
                                    navHtml += '<option value="'+item.id+'">'+item.title+'</option>';
                                });
                                body.find("#category").append(navHtml);
                                body.find("#category").val(data.category.id);
                                if (typeof(iframeWindow.layui.form) != "undefined") {
                                    iframeWindow.layui.form.render();
                                }
                            }
                            if (typeof(iframeWindow.layui.form) != "undefined") {
                                iframeWindow.layui.form.render();
                            }
                        }
                    });

                    if (typeof(iframeWindow.layui.form) != "undefined") {
                        iframeWindow.layui.form.render();
                    }
                    setTimeout(function() {
                        layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                },
                end: function() {
                    $(window).unbind("resize");
                }
            });
            layui.layer.full(index);
            //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
            $(window).on("resize", function() {
                layui.layer.full(index);
            })
        } else if(layEvent === 'del') { //删除
            layer.confirm('确定删除此商品?', {
                icon: 3,
                title: '提示信息'
            }, function(index) {
                $.post(getRealPath() + "/admin/manager/shop/product/submit/del",{
                    productId : data.id  //将需要删除的Id作为参数传入
                },function(data){
                    if (data.status == 200) {
                        layer.msg(data.message)
                        tableIns.reload();
                        layer.close(index);
                    }else{
                        layer.close(index);
                        layer.msg(data.message)
                    }
                })
            });
        } else if(layEvent === 'preview') { //删除
            //显示大图
            preview_img("/"+data.img);
        } else if(layEvent === 'property') {
            var index = layui.layer.open({
                title: "编辑属性",
                type: 2,
                content: getRealPath() + "/admin/manager/shop/product/property/page?id=" + data.id,
                success: function(layero, index) {
                    setTimeout(function() {
                        layui.layer.tips('修改成功后!点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500)
                },
                end: function() {
                    $(window).unbind("resize");
                }
            });
            layui.layer.full(index);
            //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
            $(window).on("resize", function() {
                layui.layer.full(index);
            })

        } else if(layEvent === 'recovery') {
            layer.confirm('确定恢复此商品?', {
                icon: 3,
                title: '提示信息'
            }, function(index) {
                $.post(getRealPath() + "/admin/manager/shop/product/submit/recovery",{
                    productId : data.id  //将需要删除的Id作为参数传入
                },function(data){
                    if (data.status == 200) {
                        layer.msg(data.message)
                        tableIns.reload();
                        layer.close(index);
                    }else{
                        layer.close(index);
                        layer.msg(data.message)
                    }
                })
            });

        }

    });

})