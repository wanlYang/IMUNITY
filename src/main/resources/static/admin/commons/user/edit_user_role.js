layui.use([ 'form', 'layer', 'laydate', 'jquery' ], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
			laydate = layui.laydate, $ = layui.jquery;
	// 添加自定义表单验证
	
	// 监听表单
	form.on("submit(editUserRole)", function(data) {
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
		console.log(data.field)
		var index = top.layer.msg('数据提交中,请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/user/submit/role",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						top.layer.close(index);
						top.layer.msg(result.data.username + "权限修改成功！");
						layer.closeAll("iframe");
						parent.location.reload();
					}, 500);
				} else {
					top.layer.close(index);
					top.layer.msg(result.data.username + "权限修改失败！");
				}
			}
		});
		return false;
	})
	
	form.verify({});
	// 格式化时间
	function filterTime(val) {
		if (val < 10) {
			return "0" + val;
		} else {
			return val;
		}
	}
	// 定时发布
	var time = new Date();
	var submitTime = time.getFullYear() + '-' + filterTime(time.getMonth() + 1)
			+ '-' + filterTime(time.getDate()) + ' '
			+ filterTime(time.getHours()) + ':' + filterTime(time.getMinutes())
			+ ':' + filterTime(time.getSeconds());

})