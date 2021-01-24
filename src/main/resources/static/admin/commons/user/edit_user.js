
/**
 * 暂时停止改功能
 * @returns
 */
layui.use([ 'form', 'layer', 'laydate', 'jquery' ], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
			laydate = layui.laydate, $ = layui.jquery;
	var date = new Date();
	// 创建日期选择
	var birthdayIns = laydate.render({
		elem : "#birthday", // 指定元素
		min : '1930-01-01',
		max : 'date',
		ready : function() {
			birthdayIns.hint('日期可选值设定在 <br> 1930-01-01 到 '
					+ Format(date, "yyyy-MM-dd") + '');
		}
	});
	form.on("submit(editUser)", function(data) {
		var roleArr = new Array();
		$("input:checkbox[name='stringRoles']:checked").each(function(i) {
			roleArr[i] = $(this).val();
		});
		if (roleArr.length == 0) {
			top.layer.msg("还未选择权限", {
				icon : 5,
				time : 1500,
				anim : 6
			});
			return false;
		}
		data.field.stringRoles = roleArr.join(",");// 将数组合并成字符串
		console.log(data.field)
		var index = top.layer.msg('数据提交中,请稍候', {
			icon : 16,
			time : false,
			shade : 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type : "POST",
			url : getRealPath() + "/admin/manager/user/submit/edit",
			data : data.field,
			success : function(result) {
				if (result.status == 200) {
					console.log(result.data)
					setTimeout(function() {
						top.layer.close(index);
						top.layer.msg("用户修改成功!");
						layer.closeAll("iframe");
						parent.location.reload();
					}, 500);
				} else {
					top.layer.close(index);
					top.layer.msg("用户修改失败!");
				}
			}
		});
		return false;
	})
	// 添加自定义表单验证
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