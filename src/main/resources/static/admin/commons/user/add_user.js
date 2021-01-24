layui.use(['form', 'layer', 'laydate'], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate;
	var date = new Date();
	
	//检测用户名
	var usernameFlag = false;
	$(".username").change(function(){
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/check/user/username",
			data:{"username":$(this).val()},
			success: function(result) {
				if(result.status != 200){
					top.layer.msg(result.message, {
						icon: 5,
						time: 1500,
						anim: 6
					});
					usernameFlag = false;
					return;
				}
				usernameFlag = true;
			}
		});
	})
	//检测邮箱
	var emailFlag = false;
	$(".bindemail").change(function(){
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/check/user/email",
			data:{"email":$(this).val()},
			success: function(result) {
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
	// 创建日期选择
	var birthdayIns = laydate.render({
		elem: '#birthday', // 指定元素
		min: '1930-01-01',
		max: 'date',
		ready: function() {
			birthdayIns.hint('日期可选值设定在 <br> 1930-01-01 到 ' + Format(date, "yyyy-MM-dd") + '');
		}
	});
	//获取权限信息
	$.ajax({
		type: "POST",
		url: getRealPath() + "/admin/manager/role/get/list",
		success: function(result) {
			if(result.status == 200) {
				var roleCheckboxHtml = "";
				$.each(result.data, function(index,item) {
					roleCheckboxHtml += "<input type='checkbox' name='stringRoles' value="+item.id+" title="+item.name+">";
				});
				$(".stringRoles-super-admin").html(roleCheckboxHtml);
				$(".stringRoles-super-admin input[value='2']").attr("checked","checked");

				var roleCheckboxHtmlAdmin = "";
				$.each(result.data, function(index,item) {
					if (item.id == 2) {
						roleCheckboxHtmlAdmin += "<input type='checkbox' name='stringRoles' value="+item.id+" title="+item.name+">";
					}
				});
				$(".stringRoles-admin").html(roleCheckboxHtmlAdmin);
				$(".stringRoles-admin input[value='2']").attr("checked","checked");
			}
			form.render();
		}
	});
	// 添加自定义表单验证
	form.verify({});
	// 监听表单
	form.on("submit(addUser)", function(data) {
		if(!usernameFlag){
			top.layer.msg("请重新输入用户名!", {
				icon:5,
				time: 1500,
				anim: 6,
				shade: 0.2,
        		shadeClose: true //开启遮罩关闭
			});
			return false;
		}
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
		var roleArr = new Array();
		$("input:checkbox[name='stringRoles']:checked").each(function(i) {
			roleArr[i] = $(this).val();
		});
		if(roleArr.length == 0) {
			top.layer.msg("还未选择权限", {
				icon: 5,
				time: 1500,
				anim: 6,
				shade: 0.2,
        		shadeClose: true //开启遮罩关闭
			});
			return false;
		}
		data.field.stringRoles = roleArr.join(","); // 将数组合并成字符串
		data.field.regDate = submitTime; // 给表单隐藏域注册日期赋值
		var index = top.layer.msg('数据提交中,请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/user/submit/add",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						top.layer.close(index);
						top.layer.msg("用户添加成功！");
						layer.closeAll("iframe");
						parent.location.reload();
					}, 500);
				} else {
					top.layer.close(index);
					top.layer.msg("用户添加失败！" + result.message);
				}
			}
		});
		return false;
	})
	
	// 格式化时间
	function filterTime(val) {
		if(val < 10) {
			return "0" + val;
		} else {
			return val;
		}
	}
	// 定时发布
	var time = new Date();
	var submitTime = time.getFullYear() + '-' + filterTime(time.getMonth() + 1) +
		'-' + filterTime(time.getDate()) + ' ' +
		filterTime(time.getHours()) + ':' + filterTime(time.getMinutes()) +
		':' + filterTime(time.getSeconds());

})