layui.use(['form', 'layer', 'laydate'], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate;
	var date = new Date();
	// 添加自定义表单验证
	form.verify({});
	// 监听表单
	form.on("submit(editNavRole)", function(data) {
		var roleArr = new Array();
		$("input:checkbox[name='rolesString']:checked").each(function(i) {
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
		data.field.rolesString = roleArr.join(","); // 将数组合并成字符串
		var index = top.layer.msg('数据提交中,请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/nav/submit/nav/role/edit",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						top.layer.close(index);
						top.layer.msg("导航权限编辑成功! 当重新登陆生效!");
						layer.closeAll("iframe");
						parent.location.reload();
					}, 500);
				}else{
					top.layer.close(index);
					top.layer.msg("编辑失败!");
				}
			},
			complete: function (XMLHttpRequest,textStatus) {
				top.layer.close(index);
			}
		});
		return false;
	})
	
})