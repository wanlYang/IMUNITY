layui.use(['form', 'layer','flow', 'jquery', "element",'carousel','table'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element,
        $ = layui.jquery,
        carousel = layui.carousel,
        flow = layui.flow,
        table = layui.table;
    //文章列表
    var tableIns = table.render({
        elem: '#order',
        url: getRealPath() + '/order/list/table',
        method:"post",
        id: "orderList",
        toolbar: "#toolbar",
        cols: [
            [
                {
                    field: 'id',
                    title: '订单ID',
                    align: "center",
                    templet:function(d){
                        return d.id;
                    }
                },
                {
                    field: 'createDate',
                    title: '创建时间',
                    templet:function(d){
                        return Format(d.createDate, "yyyy-MM-dd hh:mm:ss.S");
                    }
                },
                {
                    field: 'payDate',
                    title: '支付时间',
                    align: 'center',
                    templet:function(d){
                        if (d.payDate == null) {
                            return "暂未支付";
                        }
                        return Format(d.payDate, "yyyy-MM-dd hh:mm:ss.S");
                    }
                },
                {
                    field: 'deliveryDate',
                    title: '发货时间',
                    align: 'center',
                    templet:function(d){
                        if (d.deliveryDate == null) {
                            return "暂未发货";
                        }
                        return Format(d.deliveryDate, "yyyy-MM-dd hh:mm:ss.S");
                    }
                },
                {
                    field: 'confirmDate',
                    title: '确认时间',
                    align: 'center',
                    templet:function(d){
                        if (d.confirmDate == null) {
                            return "未确认";
                        }
                        return Format(d.confirmDate, "yyyy-MM-dd hh:mm:ss.S");
                    },
                },
                {
                    title: '操作',
                    width: 170,
                    templet: '#orderListBar',
                    fixed: "right",
                    align: "center"
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
    //监听行工具事件
    table.on('tool(order)', function(obj){
        var data = obj.data;
        if(obj.event === 'del'){
            layer.confirm('确认删除该订单吗?', function(index){
                //修改数量
                $.ajax({
                    url: getRealPath() + "/order/del",
                    type: 'POST',
                    complete: function (XMLHttpRequest, textStatus) {
                        layer.close(index);
                    },
                    data:{"id":obj.data.id},
                    success: function (result) {
                        if (result.status == 200) {
                            layer.msg(result.message);
                            obj.del();
                        }else{
                            layer.msg(result.message);

                        }
                    },
                    error: function () {
                        layer.msg("出现错误,请尝试刷新页面!");
                    }
                });
            });
        }else if(obj.event === 'detail'){
            location.href = getRealPath() + "/order/" + data.id;
        }
    });
})


