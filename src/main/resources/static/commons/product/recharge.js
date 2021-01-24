layui.use(['form', 'layer','flow', 'jquery', "element",'carousel'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element,
        $ = layui.jquery,
        carousel = layui.carousel,
        flow = layui.flow;


    form.verify({
        money:function (value, item) {
            if (!(/(^[1-9]\d*$)/.test(value))){
                return "请输入正确的金额!"
            }
        }
    })
    form.on('submit(recharge)',function (data) {
        var temp = $(this).parent("div").html();
        $(this).html("请稍后...").attr("disabled", "disabled").addClass("layui-disabled");
        var index = layer.msg('充值中,请稍候', {
            icon: 16,
            time: false,
            shade: 0.8
        });
        $.ajax({
            url: getRealPath() + "/account/recharge/submit",
            type: 'POST',
            complete: function (XMLHttpRequest, textStatus) {
                layer.close(index);
                $(".shopcart-submit").html(temp);
            },
            data:data.field,
            success: function (result) {
                if (result.status == 200) {
                    layer.msg(result.message);
                    $("#temp").html(result.data.balance + '￥');
                    setTimeout(function () {
                        location.href = $(".backUrl").val();
                    },1000);
                }else{
                    layer.msg(result.message);
                }
            },
            error: function () {
                layer.msg("出现错误,请尝试刷新页面!");
            }
        });

        return false;
    })


})


