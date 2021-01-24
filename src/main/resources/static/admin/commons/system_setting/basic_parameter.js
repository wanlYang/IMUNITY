layui.use(['form','layer','jquery'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		laypage = layui.laypage,
		$ = layui.jquery;
 	var systemParameter;
 	form.on("submit(systemParameter)",function(data){
 		systemParameter = '{"websiteName":"'+$(".cmsName").val()+'",';  //模版名称
 		systemParameter += '"websiteVersion":"'+$(".version").val()+'",';	 //当前版本
 		systemParameter += '"websiteId":"'+$(".websiteId").val()+'",';	 //当前版本
 		systemParameter += '"websiteAuthor":"'+$(".author").val()+'",'; //开发作者
 		systemParameter += '"websiteHomePage":"'+$(".homePage").val()+'",'; //网站首页
 		systemParameter += '"websiteServer":"'+$(".server").val()+'",'; //服务器环境
 		systemParameter += '"websiteDataBase":"'+$(".dataBase").val()+'",'; //数据库版本
 		systemParameter += '"websiteMaxUpload":"'+$(".maxUpload").val()+'",'; //最大上传限制
 		systemParameter += '"websitePowerby":"'+$(".powerby").val()+'",'; //版权信息
 		systemParameter += '"websiteDescription":"'+$(".description").val()+'",'; //站点描述
 		systemParameter += '"websiteRecord":"'+$(".record").val()+'"}'; //网站备案号
 		window.sessionStorage.setItem("systemParameter",systemParameter);
 		//弹出loading
 		var index = top.layer.msg('数据提交中,请稍候',{icon: 16,time:false,shade:0.8});
        console.log(data.field)
        $.post(getRealPath() + "/admin/manager/system_setting/change/parameter", data.field, function(result) {
			if(result.status != 200){
				layer.msg("修改失败!");
				return;
			}
			top.layer.close(index);
			top.layer.msg("系统基本参数修改成功！");
			setTimeout(function(){
				parent.location.reload();
			},500);
		}, "json");
 		return false;
 	})

 	//加载默认数据
 	if(window.sessionStorage.getItem("systemParameter")){
 		var data = JSON.parse(window.sessionStorage.getItem("systemParameter"));
 		fillData(data);
 	}else{
 		$.ajax({
			url : getRealPath() + "/admin/manager/system_setting/get/parameter",
			type : "GET",
			dataType : "json",
			success : function(result){
				var index = layer.msg('数据获取中,请稍候',{icon: 16,time:false,shade:0.8});
		        setTimeout(function(){
		            layer.close(index);
					layer.msg("数据获取成功!");
					fillData(result.data);
		        },500);
			}
		})
 	}
 	//填充数据方法
 	function fillData(data){
 		$(".websiteId").val(data.websiteId);      //ID
 		$(".version").val(data.websiteVersion);      //当前版本
		$(".author").val(data.websiteAuthor);        //开发作者
		$(".homePage").val(data.websiteHomePage);    //网站首页
		$(".server").val(data.websiteServer);        //服务器环境
		$(".dataBase").val(data.websiteDataBase);    //数据库版本
		$(".maxUpload").val(data.websiteMaxUpload);  //最大上传限制
		$(".cmsName").val(data.websiteName);      //模版名称
		$(".description").val(data.websiteDescription);//站点描述
		$(".powerby").val(data.websitePowerby);      //版权信息
		$(".record").val(data.websiteRecord);      //网站备案号
 	}
 	
})
