layui.use(['form', 'layer', 'laydate','upload'], function() {
	var form = layui.form,
	layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate;
	var upload = layui.upload;
	var date = new Date();
	var imgUrls = "";

	// 添加自定义表单验证
	form.verify({
		money:function (value, item) {
			if (!(/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value))){
				return "请输入正确的金额!"
			}
		}
	});
	// 监听表单
	form.on("submit(editProduct)", function(data) {
		imgUrls = $("#imgUrls").val();
		if (imgUrls == ""){
			layer.msg("请上传图片!");
			return false;
		}
		var index = layer.msg('数据提交中,请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
		// 实际使用时的提交信息
		$.ajax({
			type: "POST",
			url: getRealPath() + "/admin/manager/shop/product/submit/edit",
			data: data.field,
			success: function(result) {
				if(result.status == 200) {
					setTimeout(function() {
						layer.close(index);
						layer.msg("商品修改成功！");
						layer.closeAll("iframe");
						parent.location.reload();
					}, 500);
				}else{
					layer.close(index);
					layer.msg("");
				}
			},
			complete: function (XMLHttpRequest,textStatus) {
				layer.close(index);
			}
		});
		return false;
	})


	/**
	 * 图片上传数量及其大小等控制
	 * 点击开始上传按钮(test9)，执行上传
	 *
	 */
	var success=0;
	var fail=0;

	upload.render({
		elem: '#button',
		url: getRealPath() + '/admin/manager/shop/product/upload',
		multiple: true,
		auto:false,
		//上传的单个图片大小
		size:10240,
		//最多上传的数量
		number:20,
		data: {
			id: function(){
				return $('.id').val();
			},
			type: "EDIT"
		},
		//MultipartFile file 对应，layui默认就是file,要改动则相应改动
		field:'file',
		bindAction: '#start',
		before: function(obj) {
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#show').append('<div style="height: 210px;width: 210px;float: left;margin-left: 10px;margin-top: 10px;"><img src="' + result
					+ '" alt="' + file.name
					+'"class="cover layui-upload-img uploadImgPreView"></div>')
			});
		},
		done: function(res, index, upload) {
			//每个图片上传结束的回调，成功的话，就把新图片的名字保存起来，作为数据提交
			if(res.code==1){
				fail++;
			}else{
				success++;
				imgUrls = $("#imgUrls").val();
				imgUrls=imgUrls+""+res.data.src+",";
				$('#imgUrls').val(imgUrls);

			}
		},
		allDone:function(obj){
			layer.msg("总共要上传图片总数为："+(fail+success)+"\n"
				+"其中上传成功图片数为："+success+"\n"
				+"其中上传失败图片数为："+fail
			)
		}
	});

	//清空预览图片
	cleanImgsPreview();
	/**
	 * 清空预览的图片及其对应的成功失败数
	 * 原因：如果已经存在预览的图片的话，再次点击上选择图片时，预览图片会不断累加
	 * 表面上做上传成功的个数清0，实际后台已经上传成功保存了的，只是没有使用，以最终商品添加的提交的为准
	  */
	function cleanImgsPreview(){
		$("#cleanImgs").click(function(){
			success=0;
			fail=0;
			$('#show').html("");
			$('#imgUrls').val("");
		});
	}
});


