var form, $,areaData;
layui.config({
    base : getRealPath() + "/admin/static/js/"
}).extend({
    "address" : "address",
    "croppers" : "{/}static/cropper/croppers"
})
layui.use(['form','layer','upload','laydate',"address","croppers"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address,
        croppers = layui.croppers;
    //检测邮箱
    var emailFlag = false;
    $("#bindemail").change(function(){
        $.ajax({
            type: "POST",
            url: getRealPath() + "/admin/manager/check/user/email",
            data:{"email":$(this).val(),"isEdit":true},
            success: function(result) {
                console.log(result.status)
                if(result.status != 200){
                    top.layer.msg(result.message, {
                        icon: 5,
                        time: 1500,
                        anim: 6
                    });
                    emailFlag = false;
                    return;
                }
                emailFlag = true;
            }
        });
    })
    // 创建一个头像上传组件
    croppers.render({
        elem: '#editimg',
        saveW: 1000,// 保存宽度
        saveH: 1000,
        mark: 1/1,// 选取比例
        area: ['900px', '700px'],// 弹窗宽度
        url: getRealPath() + "/admin/upload/uploadImg",// 图片上传接口返回和(layui 的upload 模块)返回的JOSN一样
        done: function(result){// 上传完毕回调
        	var userHeadImg = $("#userHeadImg");
            var userAvatar = $('.userAvatar', parent.document);
            for (var i = 0;i < userAvatar.length;i++) {
                userAvatar[i].src = result.data.src;
            }
        	userHeadImg[0].src = result.data.src;
        	imunityCookie.set("adminHeadImg",result.data.src,24);
            layer.msg(result.msg,{icon: 1});
        }
    });
    // 添加验证规则
    form.verify({
        birthday : function(value){
            if(!/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
                return "出生日期格式不正确！";
            }
        }
    })
    
    var date = new Date();
    // 创建日期选择
	var birthdayIns = laydate.render({
		elem: '#birthday', // 指定元素
		min: '1930-01-01',
		max: 'date',
		ready: function() {
			birthdayIns.hint('日期可选值设定在 <br> 1930-01-01 到 ' + Format(date, "yyyy-MM-dd") + '');
		}
	});

    // 获取省信息
    // address.provinces();

    // 提交个人资料
    form.on("submit(changeAdmin)",function(data){
        if(!emailFlag){
            top.layer.msg("请重新输入邮箱!!", {
                icon:5,
                time: 1500,
                anim: 6,
                shade: 0.2,
                shadeClose: true //开启遮罩关闭
            });
            return false;
        }
        var index = layer.msg('提交中,请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/submit/change/info",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						layer.close(index);
						layer.msg("修改成功!");
					}, 500);
				} else {
					top.layer.close(index);
					top.layer.msg("修改失败!");
				}
			}
		});

        return false; // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })
})