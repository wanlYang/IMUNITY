layui.use(['form', 'layer', 'laydate'], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate;
	var date = new Date();
	// 获取所有分类,最高到1级
	$.ajax({
		type: "POST",
		url: getRealPath() + "/admin/manager/shop/category/top",
		data:{"flag":2},
		success: function(result) {
			if(result.status == 200) {
				var navHtml = '';
				$.each(result.data, function(index,item) {
					navHtml += '<option value="'+item.id+'">一级> '+item.title+'</option>';
				});
				$("#parentLevel").append(navHtml);
			}
			form.render();
		}
	});
	// 添加自定义表单验证
	form.verify({});
	// 监听表单
	form.on("submit(addCate)", function(data) {
		var index = top.layer.msg('数据提交中,请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/shop/category/submit/add",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						top.layer.close(index);
						top.layer.msg("分类添加成功！");
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