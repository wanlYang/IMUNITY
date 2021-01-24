var $,element,layer;
layui.use(['form', 'layer', 'jquery',"element"], function () {
    var form = layui.form;
    layer = parent.layer === undefined ? layui.layer : top.layer;
    element = layui.element;
    $ = layui.jquery;
    form.verify({
        password : function(value, item){
            if(value.length < 6){
                return "密码长度不能小于6位";
            }
        },
        confirmPass : function(value, item){
            if(!new RegExp($("#password").val()).test(value)){
                return "两次输入密码不一致,请重新输入!";
            }
        },
        phone : function (value, item) {
            if(!(/^1[34578]\d{9}$/.test(value))){
                return "手机号码有误，请重填";
            }
        },
        phoneCode : function (value, item) {
            if(value.length < 6){
                return "验证码长度不能小于6位";
            }
        }
    })
    //注册按钮
    form.on("submit(regist)", function (data) {
        $(this).text("请稍后...").attr("disabled", "disabled").addClass("layui-disabled");
        var index = layer.msg('注册中,请稍候', {
            icon: 16,
            time: false,
            shade: 0.8
        });
        $.ajax({
            type: "POST",
            url: getRealPath() + "/user/regist/submit",
            data:data.field,
            complete: function (XMLHttpRequest, textStatus) {
                layer.close(index);
                $("#registButton").text("注 册").removeAttr("disabled").removeClass("layui-disabled");
            },
            success: function(result) {
                if(result.status == 200){
                    layer.msg("注册成功!");
                    setTimeout(function(){
                        window.location.href = getRealPath() + "/login";
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
function sendCode(sendButton) {
    time(sendButton);
    $.ajax({
        type: "POST",
        url: getRealPath() + "/message/send/phone/code",
        data:{"phone":$("#phone").val()},
        success: function(result) {
            layer.msg(result.message);
            if(result.data == '00000'){
                $("#phone").attr("disabled","disabled");
            }
        }
    });
}

var wait = 60;
function time(btn) {
    if (wait == 0) {
        btn.removeClass("layui-btn-disabled");
        btn.text("获取验证码");
        wait = 60;
        $("#phone").removeAttr("disabled");
        btn.bind("click",function () {
            sendCode(btn);
        })
    } else {
        btn.addClass("layui-btn-disabled");
        btn.text(wait + "秒后重新获取");
        btn.unbind();
        wait--;
        setTimeout(function () {
                time(btn);
        },
        1000)
    }
}
function isSendCode(phone){
    if(!(/^1[34578]\d{9}$/.test(phone))){
        $("#sendCode").unbind();
        $("#sendCode").addClass("layui-btn-disabled");
    }else{
        $("#sendCode").removeClass("layui-btn-disabled");
        $("#sendCode").bind("click",function () {
            sendCode($("#sendCode"));
        })
    }
}
