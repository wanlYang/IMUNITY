// 修改密码
layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    //添加验证规则
    form.verify({
        newPwd : function(value, item){
            if(value.length < 6){
                return "密码长度不能小于6位";
            }
        },
        confirmPwd : function(value, item){
            if(!new RegExp($("#oldPwd").val()).test(value)){
                return "两次输入密码不一致,请重新输入!";
            }
        }
    })
    form.on("submit(changePassword)",function(data){
        var index = null;
        $.ajax({
            type:"POST",
            url: getRealPath() + "/admin/submit/change/password",
            data:{oldPass:data.field.oldPassword,newPass:data.field.newPassword},
            beforeSend: function(){
                index = layer.msg('提交中,请稍候',{icon: 16,time:false,shade:0.8});
                $("#changePassword_1").prop('disabled','disabled');
            },
            success:function (data) {
                console.log(data)
                if (data.status == 200) {
                    layer.close(index);
                    layer.msg("密码修改成功!请重新登陆!");
                    setTimeout(function () {
                        $('#signOutForm', parent.document).submit();
                    },1000)
                    $(".pwd").val('');
                }else if(data.status == -1){
                    layer.close(index);
                    layer.msg("旧密码输入错误!");
                }
                $("#changePassword_1").removeAttr("disabled");
            },
            error:function () {
                layer.msg("链接出错!");
                layer.close(index);
                $("#changePassword_1").removeAttr("disabled");
            },
            complete:function () {
                layer.close(index);
                $("#changePassword_1").removeAttr("disabled");
            }
        })
        return false; // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })

})

