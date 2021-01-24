layui.use(['form', 'layer', 'laydate', 'table', 'laytpl'], function() {
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate,
		laytpl = layui.laytpl,
		table = layui.table;

	//文章列表
	var tableIns = table.render({
		elem: '#newsList',
		url: getRealPath() + '/admin/manager/article/get/article/list',
		cellMinWidth: 100,
		page: true,
		height: "full-125",
		limit: 20,
		method:"post",
		limits: [10, 15, 20, 25],
		id: "newsList",
		cols: [
			[{
					type: "checkbox",
					fixed: "left",
					width: 50
				},
				{
					field: 'id',
					title: 'ID',
					width: 60,
					align: "center"
				},
				{
					field: 'title',
					title: '文章标题',
					width: 350
				},
				{
					field: 'newsAuthor',
					title: '发布者',
					align: 'center',
					templet:function(d){
						return d.user.username;
					}
				},
				{
					field: 'theme',
					title: '所属主题',
					align: 'center',
					templet:function(d){
						return d.theme.themeTitle;
					}
				},
				{
					field: 'newsStatus',
					title: '发布状态',
					align: 'center',
					templet: "#newsStatus"
				},
				{
					field: 'replyCount',
					title: '回复数',
					align: 'center'
				},
				{
					field: 'newsTop',
					title: '是否置顶',
					align: 'center',
					event: 'setTop',
					templet: function(d) {
						if (d.isTop == 1) {
							return '<input type="checkbox" name="isTop" lay-filter="isTop" lay-skin="switch" checked lay-text="是|否" value="' + d.id + '">'
						}else{
							return '<input type="checkbox" name="isTop" lay-filter="isTop" lay-skin="switch" lay-text="是|否" value="' + d.id + '"d>'
						}
					}
				},
				{
					field: 'newsTime',
					title: '发布时间',
					align: 'center',
					minWidth: 110,
					templet: function(d) {
						return Format(d.time, "yyyy-MM-dd hh:mm:ss.S")
					}
				},
				{
					title: '操作',
					width: 170,
					templet: '#newsListBar',
					fixed: "right",
					align: "center"
				}
			]
		],
		text: {
			none: '暂无相关数据' // 默认：无数据。注：该属性为 layui 2.2.5 开始新增
		},
		response: {
			statusName: 'status', // 规定数据状态的字段名称，默认：code
			statusCode: 200, // 规定成功的状态码，默认：0
			msgName: 'message', // 规定状态信息的字段名称，默认：msg
			countName: 'count', // 规定数据总数的字段名称，默认：count
			dataName: 'data' // 规定数据列表的字段名称，默认：data
		}
	});

	//是否置顶
	form.on('switch(isTop)', function(data) {
		var index = layer.msg('修改中，请稍候', {
			icon: 16,
			time: false,
			shade: 0.8
		});
    	$.post(getRealPath() + "/admin/manager/article/submit/article/top",{
		    id : data.value
		},function(result){
			if (result.status == 200) {
				setTimeout(function() {
					layer.close(index);
					if(data.elem.checked) {
						layer.msg("置顶成功！");
					} else {
						layer.msg("取消置顶成功！");
					}
				}, 500);
			}else{
				layer.msg("操作失败！请务必刷新重试!否者数据可能有无!");
			}
		})
	})

	//搜索【此功能需要后台配合，所以暂时没有动态效果演示】
	$(".search_btn").on("click", function() {
		if($(".searchVal").val() != '') {
			table.reload("newsList", {
				page: {
					curr: 1 //重新从第 1 页开始
				},
				where: {
					key: $(".searchVal").val() //搜索的关键字
				}
			})
		} else {
			layer.msg("请输入搜索的内容");
		}
	});

	//添加文章
	function addArticle() {
		var index = layui.layer.open({
			title: "添加文章",
			type: 2,
			content: getRealPath() + "/admin/manager/article/add",
			success: function(layero, index) {
				var body = layui.layer.getChildFrame('body', index);
				setTimeout(function() {
					layui.layer.tips('点击此处返回文章列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				}, 500)
			},
			end: function() {
				$(window).unbind("resize");
			}
		});
		layui.layer.full(index);
		//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize", function() {
			layui.layer.full(index);
		});
	}
	$(".addNews_btn").click(function() {
		addArticle();
	})

	//批量删除
	$(".delAll_btn").click(function() {
		var checkStatus = table.checkStatus('newsList'),
			data = checkStatus.data,
			newsId = [];
		if(data.length > 0) {
			for(var i in data) {
				newsId.push(data[i].id);
			}
			layer.confirm('确定删除选中的文章？', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.ajax({
					type: "post",
					url: getRealPath() + "/admin/manager/article/submit/article/batch/del",
					async: true,
					data: JSON.stringify(newsId),
					contentType: "application/json",
					success: function(data) {
						layer.msg(data.message, {
							time: 1000
						});
						if(!checkStatus.isAll) {
							tableIns.reload({});
						} else {
							tableIns.reload({
								page: {
									curr: 1
								}
							});
						}
						layer.close(index);
					}
				});
			})
		} else {
			layer.msg("请选择需要删除的文章");
		}
	})

	//列表操作
	table.on('tool(newsList)', function(obj) {
		var layEvent = obj.event,
			data = obj.data;
		if(layEvent === 'edit') { //编辑
			var index = layui.layer.open({
				title: "编辑文章",
				type: 2,
				content: getRealPath() + "/admin/manager/article/edit",
				success: function(layero, index) {
					var body = layui.layer.getChildFrame('body', index);
					var iframeWindow = window[layero.find('iframe')[0]['name']];
					console.log(data);
					body.find(".articleId").val(data.id);
					body.find(".newsName").val(data.title);
					body.find(".thumbImg").attr("src",data.thumbnail);
					body.find("#news_content").val(data.contents);
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
								body.find(".themeList").html(themeHtml);
								var radioBox = body.find(".themeList input[name='theme']");
								for (var i = 0; i < result.data.length; i++) {
									if (radioBox[i].value == data.theme.themeId) {
										radioBox[i].checked = true;
									}
									
								}
							}
							if (typeof(iframeWindow.layui.form) != "undefined") {
								iframeWindow.layui.form.render();
							}
						}
					});
					body.find(".newsStatus select").val(data.flag);
					if (data.isTop == 1) {
						body.find(".newsTop input[name='newsTop']")[0].checked = true;
					}
					
					if (typeof(iframeWindow.layui.form) != "undefined") {
						iframeWindow.layui.form.render();
					}
					setTimeout(function() {
						layui.layer.tips('点击此处返回文章列表', '.layui-layer-setwin .layui-layer-close', {
							tips: 3
						});
					}, 500)
				},
				end: function() {
					$(window).unbind("resize");
				}
			});
			layui.layer.full(index);
			//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
			$(window).on("resize", function() {
				layui.layer.full(index);
			})
		} else if(layEvent === 'del') { //删除
			layer.confirm('确定删除此文章？', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.post(getRealPath() + "/admin/manager/article/submit/article/del",{
				    id : data.id  //将需要删除的newsId作为参数传入
				},function(data){
					if (data.status == 200) {
						tableIns.reload();
						layer.close(index);
					}else{
						layer.close(index);
						layer.msg("删除失败!")
					}
				})
			});
		} else if(layEvent === 'look') { //预览
			layer.alert("此功能需要前台展示，实际开发中传入对应的必要参数进行文章内容页面访问")
		} else if(layEvent === 'setTop') {
			obj.update({
				isTop : data.isTop == 0 ? 1 : 0,
			})
		}
	});

})