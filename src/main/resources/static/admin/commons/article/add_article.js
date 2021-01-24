layui.config({
    base : getRealPath() + "/admin/static/"
}).extend({
    "croppers_article" : "cropper/croppers_article"
})
layui.use(['form','layer','laydate','upload','croppers_article'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laypage = layui.laypage,
        upload = layui.upload,
        laydate = layui.laydate,
        croppers = layui.croppers_article,
        $ = layui.jquery;
    //获取所有主题
	$.ajax({
		type: "POST",
		url: getRealPath() + "/admin/manager/theme/get/list",
		success: function(result) {
			if(result.status == 200) {
				var themeHtml = "";
				$.each(result.data, function(index,item) {
					themeHtml += "<span><input type='radio' name='theme' value="+item.themeId+" lay-skin='primary' title="+item.themeTitle+"></span>";
				});
				$(".themeList").html(themeHtml);
				$(".themeList input[type='radio']:first").attr("checked","checked");
			}
			form.render();
		}
	});
    // 上传缩略图
    // 创建一个头像上传组件
	var thumbnailImg = "/community/article/img/default_thumbnail.png";
    croppers.render({
        elem: '.thumbBox',
        saveW: 1000,// 保存宽度
        saveH: 500,
        mark: 16/8,// 选取比例
        area: ['870px', '700px'],// 弹窗宽度
        url: getRealPath() + "/admin/manager/article/upload/thumbnail",// 图片上传接口返回和(layui 的upload 模块)返回的JOSN一样
        done: function(result){// 上传完毕回调
        	if (result.code == 0) {
        		$('.thumbImg').attr('src',result.data.src);
        		$('.thumbBox').css("background","#fff");
        		thumbnailImg = result.data.src;
			}else{
				top.layer.msg("上传失败!");
			}
        }
    });
    Simditor.locale = 'zh-CN'; //设置中文
	var editor = new Simditor({
		textarea: $('#news_content'), //textarea的id
		placeholder: '这里输入文字...',
		toolbar: [
			'title',
			'bold',
			'italic',
			'underline',
			'strikethrough',
			'fontScale',
			'color',
			'|',
			'ol',
			'ul',
			'blockquote',
			'code',
			'table',
			'|',
			'link',
			'image',
			'hr',
			'|',
			'indent',
			'outdent',
			'alignment'
		], //工具条都包含哪些内容
		pasteImage: true, //允许粘贴图片
		defaultImage: getRealPath() + '/admin/static/simditor/images/image.png', //编辑器插入的默认图片，此处可以删除
		upload: {
			url: 'richtext_img_upload', //文件上传的接口地址
			params: null, //键值对,指定文件上传接口的额外参数,上传的时候随文件一起提交
			fileKey: 'upload_file', //服务器端获取文件数据的参数名
			connectionCount: 3,
			leaveConfirm: '正在上传文件',
		}
	});
	editor.uploader.on('uploadsuccess', (function(_this) {
      return function(e, file, result) {
    	  $img = file.img;
    	  $img.attr("alt",result.title);
      };
    })(this));
	
	
    form.verify({
        newsName : function(val){
            if(val == ''){
                return "文章标题不能为空";
            }
        },
        content : function(value){
        	if(value.match(/^[ ]*$/)) {
				return "内容不能空!";
			}
        }
    })
    form.on("submit(addNews)",function(data){
    	console.log(data.field);
    	console.log(editor.getValue());
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        //实际使用时的提交信息
		$.post(getRealPath() + "/admin/manager/article/submit/article/add",{
			title : $(".newsName").val(),  //文章标题
			contents : editor.getValue(),  //文章内容
			time : submitTime,  //时间
			"theme.themeId" : $('.themeList input[type="radio"]:checked').val(),    //文章分类
			flag : $('.newsStatus select').val(),    //发布状态
			thumbnail : thumbnailImg,
			isTop:data.field.newsTop
		 },function(result){
			 if (result.status == 200) {
				 setTimeout(function(){
		            top.layer.close(index);
		            top.layer.msg("文章添加成功！");
		            layer.closeAll("iframe");
		            //刷新父页面
		            parent.location.reload();
				 },500);
			 }else{
				 top.layer.close(index);
		         top.layer.msg("文章添加失败！");
			 }
		 })
        return false;
    })

    //预览
    form.on("submit(look)",function(){
        layer.alert("此功能需要前台展示，实际开发中传入对应的必要参数进行文章内容页面访问");
        return false;
    })
    
    
    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());
    laydate.render({
        elem: '#release',
        type: 'datetime',
        trigger : "click",
        done : function(value, date, endDate){
            submitTime = value;
        }
    });
    form.on("radio(release)",function(data){
        if(data.elem.title == "定时发布"){
            $(".releaseDate").removeClass("layui-hide");
            $(".releaseDate #release").attr("lay-verify","required");
        }else{
            $(".releaseDate").addClass("layui-hide");
            $(".releaseDate #release").removeAttr("lay-verify");
            submitTime = time.getFullYear()+'-'+(time.getMonth()+1)+'-'+time.getDate()+' '+time.getHours()+':'+time.getMinutes()+':'+time.getSeconds();
        }
    });

})