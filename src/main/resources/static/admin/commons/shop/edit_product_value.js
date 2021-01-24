layui.use(['form', 'layer', 'laydate'], function() {
	var form = layui.form
	layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate;
	var date = new Date();
	$(".propvalue").change(function () {
		var value = $(this).val();
		var property_value_id = $(this).attr("property_value_id");
		$.ajax({
			type: "post",
			url: getRealPath() + "/admin/manager/shop/product/property/submit",
			data:{"id":property_value_id,"value":value},
			success: function(result) {
				if(result.status == 200) {
					layer.msg("修改成功!");
				}else{
					layer.msg("修改失败!");
					$(this).val(result.value);
				}
			}
		});
	})
})