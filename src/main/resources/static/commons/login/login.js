layui.use(['form', 'layer', 'jquery',"element"], function () {
    var form = layui.form,
    layer = parent.layer === undefined ? layui.layer : top.layer,
    element = layui.element,
    $ = layui.jquery;
    form.verify({
        password : function(value, item){
            if(value.length < 6){
                return "密码长度不能小于6位";
            }
        }
    })
    //注册按钮
    form.on("submit(login)", function (data) {
        $(this).text("请稍后...").attr("disabled", "disabled").addClass("layui-disabled");
        var index = layer.msg('登陆中,请稍候', {
            icon: 16,
            time: false,
            shade: 0.8
        });
        $.ajax({
            type: "POST",
            url: getRealPath() + "/user/login/submit",
            data:data.field,
            complete: function (XMLHttpRequest, textStatus) {
                layer.close(index);
                $("#loginButton").text("登 陆").removeAttr("disabled").removeClass("layui-disabled");
            },
            success: function(result) {
                if(result.status == 200){
                    layer.msg("登陆成功!");
                    setTimeout(function(){
                        window.location.href = getRealPath() + "/";
                    },1000);
                }else{
                    layer.msg(result.message);
                }
            },
            error: function () {
                layer.msg("出现错误,请尝试刷新页面!");
            }
        })
        return false;
    })
})


