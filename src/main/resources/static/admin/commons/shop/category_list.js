layui.use(['form', 'layer', 'laydate', 'table', 'laytpl'], function() {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    var tableIns = table.render({
        elem: '#cateList',
        url: getRealPath() + '/admin/manager/shop/category/list',
        cellMinWidth: 100,
        height: 'full-140',
        method:"post",
        id: "cateList",
        cols: [
            [{
                    field: 'id',
                    title: 'ID',
                    width: 60,
                    align: "center"
                },
                {
                    field: 'parentId',
                    title: '所属父级',
                    templet:function(d){
                        if (d.parentId == 0){
                            return '<span class="layui-btn layui-btn-normal layui-btn-xs">顶级分类</span>';
                        }else{
                            var title = "";
                            $.ajax({
                                type: "POST",
                                async:false,
                                url: getRealPath()+"/admin/manager/shop/category/get",
                                data:{'id':d.parentId},
                                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
                                success: function(result) {
                                    if (result.status == 200) {
                                        title = result.data.title;
                                    }else{
                                        title = "导航标题获取失败!";
                                    }
                                }
                            });
                            return '<span class="layui-btn layui-btn-xs">'+title+'</span>';
                        }
                    }
                },
                {
                    field: 'title',
                    title: '分类标题',
                    templet:function(d){
                        return d.title;
                    }
                },
                {
                    field: 'description',
                    title: '描述',
                    minWidth:600,
                    align: 'center',
                    templet:function(d){
                        return d.description;
                    }
                },
                {
                    field: 'display',
                    title: '显示',
                    align: 'center',
                    templet:function(d){
                        if (d.display == 1) {
                            return '<span class="layui-btn layui-btn-xs">正常</span>';
                        }else{
                            return '<span class="layui-btn layui-btn-xs">异常</span>';
                        }

                    }
                },
                {
                    title: '操作',
                    align: 'center',
                    templet:"#cateListBar"
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

    //新增分类
    $(".addCate_btn").click(function(){
        var index = layui.layer.open({
            title: "添加分类",
            type: 2,
            content: getRealPath() + "/admin/manager/shop/category/add/page",
            success: function(layero, index) {
                setTimeout(function() {
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
                layui.layer.msg("最高添加至二级分类!");
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
    table.on('tool(cateList)', function(obj) {
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit') { //编辑
            var index = layui.layer.open({
                title: "编辑分类",
                type: 2,
                content: getRealPath() + "/admin/manager/shop/category/edit/page",
                success: function(layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    var iframeWindow = window[layero.find('iframe')[0]['name']];
                    body.find(".id").val(data.id);
                    body.find(".title").val(data.title);
                    body.find(".description").val(data.description);
                    body.find(".display").val(data.display);
                    // 获取所有分类,最高到1级
                    $.ajax({
                        type: "POST",
                        url: getRealPath() + "/admin/manager/shop/category/top",
                        data:{"flag":2},
                        success: function(result) {
                            if(result.status == 200) {
                                var navHtml = '';
                                $.each(result.data, function(index,item) {
                                    if (item.id == data.id){
                                        navHtml += '<option value="'+item.id+'" disabled title="无法选中">一级》 '+item.title+'</option>';
                                    }else{
                                        navHtml += '<option value="'+item.id+'">一级> '+item.title+'</option>';
                                    }

                                });
                                body.find("#editParentLevel").append(navHtml);
                                body.find("#editParentLevel").val(data.parentId);
                                if (typeof(iframeWindow.layui.form) != "undefined") {
                                    iframeWindow.layui.form.render();
                                }
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
            layer.confirm('确定删除此分类？', {
                icon: 3,
                title: '提示信息'
            }, function(index) {
                $.post(getRealPath() + "/admin/manager/shop/category/submit/del",{
                    id : data.id  //将需要删除的Id作为参数传入
                },function(data){
                    if (data.status == 200) {
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