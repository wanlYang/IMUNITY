layui.use(['form', 'layer', 'laydate'], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate;
	var date = new Date();
	
	// 添加自定义表单验证
	form.verify({});
	// 监听表单
	form.on("submit(addRole)", function(data) {
		var index = top.layer.msg('数据提交中,请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/role/submit/add",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						top.layer.close(index);
						top.layer.msg("权限添加成功！");
						layer.closeAll("iframe");
						parent.location.reload();
					}, 500);
				}else{
					top.layer.close(index);
					top.layer.msg(result.message);
				}
			},
			complete: function (XMLHttpRequest,textStatus) {
				top.layer.close(index);
			}
		});
		return false;
	})
	
})