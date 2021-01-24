layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    table.render({
		elem : '#navListTable',
		method:"POST",
		page: true,
		limit: 10,
		url : getRealPath() + '/admin/manager/nav/get/nav/list',
		cellMinWidth : 95,
		cols : [ [ {
			field : "navId",
			title : '导航ID',
			width : 90,
			fixed : "left",
			sort : "true",
			align : 'center'
		}, {
			field : 'parentId',
			title : '父级导航',
			align : 'center',
			sort : "true",
			templet: function(d){
				if (d.parentId == 0) {
					return '<span class="layui-btn layui-btn-green layui-btn-xs">顶级导航</span>';
				}else{
					var title = "";
					$.ajax({
			            type: "POST",
			            async:false,
			            url: getRealPath()+"/admin/manager/nav/top/nav/title",
			            data:{'id':d.parentId},
			            contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			            success: function(result) {
			            	if (result.status == 200) {
			            		title = result.data.title;
			    			}else{
			    				title = "导航标题获取失败!";
			    			}
			            }
			        });
					return '<span class="layui-btn layui-btn-normal layui-btn-xs">'+title+'</span>';
				}
			}
		}, {
			field : 'title',
			title : '标题',
			align : 'center'
		}, {
			field : 'index',
			title : 'INDEX',
			align : 'center'
		}, {
			field : 'href',
			title : '链接',
			align : 'center'
		}, {
			field : 'icon',
			title : '图标',
			align : 'center',
			templet: function(d) {
				return HtmlUtil.htmlEncodeByRegExp(d.icon);
			}
		}, {
			field : 'target',
			title : '是否新开',
			align : 'center',
			templet: function(d) {
				if(d.target == "_blank"){
					return d.target;
				}
				return "非新标签";
			}
		}, {
			field : 'spread',
			title : '是否展开',
			align : 'center'
		}, {
			field : 'rolesString',
			title : '权限ID',
			align : 'center'
		}
		,{
			title: '操作',
			minWidth: 220,
			templet: '#navListBar',
			fixed: "right",
			align: "center"
		}] ],
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
    //新增等级
    $(".addNav").click(function(){
    	var index = layui.layer.open({
			title: "添加导航",
			type: 2,
			content: getRealPath() + "/admin/manager/nav/add/page",
			success: function(layero, index) {
				setTimeout(function() {
					layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				}, 500)
				layui.layer.msg("最高添加至三级导航!");
			},
			end: function() {
				$(window).unbind("resize");
			}
		})
		layui.layer.full(index);
		window.sessionStorage.setItem("index", index);
		// 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize", function() {
			layui.layer.full(window.sessionStorage.getItem("index"));
		})
    });
    // 列表操作
	table.on('tool(navTable)', function(obj) {
		var layEvent = obj.event,
			data = obj.data;
		//监听单元格事件
		if(layEvent === "del") {
			layer.confirm('确定删除此导航?此操作会将所关联的导航权限删除!', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.post(getRealPath() + "/admin/manager/nav/submit/nav/delete",{id:data.navId},function(result){
					if(result.status == 200){
						obj.del();// 删除缓存
						top.layer.msg(result.message);
					}else{
						top.layer.msg(result.message);
					}
				},"json");
				layer.close(index);
			});
		}else if(layEvent === "edit"){
			var navIndex = layui.layer.open({
				title: "编辑导航",
				type: 2,
				content: getRealPath() + "/admin/manager/nav/edit/page",
				success: function(layero, index) {
					var body = layui.layer.getChildFrame('body', index);
					var iframeWindow = window[layero.find('iframe')[0]['name']];
					var roles = data.rolesString.split(",");
					//获取权限信息
					$.ajax({
						type: "POST",
						url: getRealPath() + "/admin/manager/role/get/list",
						data:{"flag":1},
						success: function(result) {
							if(result.status == 200) {
								var roleCheckboxHtml = "";
								$.each(result.data, function(index,item) {
									roleCheckboxHtml += "<input type='checkbox' name='rolesString' value="+item.id+" title="+item.name+">";
								});
								body.find(".stringRoles-super-admin").html(roleCheckboxHtml);
								for (var i = 0; i < roles.length; i++) {
									var rolesCheckbox = body.find(".stringRoles-super-admin input[name='rolesString']");
									for (var j = 0; j < rolesCheckbox.length; j++) {
										if (rolesCheckbox[j].value == roles[i]) {
											rolesCheckbox[j].checked = true;
										}
									}
								}
								if (typeof(iframeWindow.layui.form) != "undefined") {
									iframeWindow.layui.form.render();
								}
							}
						}
					});
					// 获取所有导航,最高到二级
					$.ajax({
						type: "POST",
						url: getRealPath() + "/admin/manager/nav/page/nav/top",
						data:{"flag":2},
						success: function(result) {
							if(result.status == 200) {
								var navHtml = '';
								$.each(result.data, function(index,item) {
									navHtml += '<option value="'+item.navId+'">'+item.title+'</option>';
								});
								body.find("#editparentLevel").append(navHtml);
								body.find("#editparentLevel").val(data.parentId);
								if (typeof(iframeWindow.layui.form) != "undefined") {
									iframeWindow.layui.form.render();
								}
							}
						}
					});
					body.find(".title").val(data.title);
					body.find(".href").val(data.href);
					body.find(".index").val(data.index);
					body.find(".target").val(data.target);
					body.find(".icon").val(data.icon);
					body.find(".spread").val(data.spread);
					body.find(".navId").val(data.navId);
					if (data.spread == false) {
						body.find(".spread").val(0);
					}else{
						body.find(".spread").val(1);
					}
					if (typeof(iframeWindow.layui.form) != "undefined") {
						iframeWindow.layui.form.render();
					}
					setTimeout(function() {
						layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
							tips: 3
						});
					}, 500)
				},
				end: function() {
					$(window).unbind("resize");
					
				}
			})
			layui.layer.full(navIndex);
			window.sessionStorage.setItem("navIndex", navIndex);
			// 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
			$(window).on("resize", function() {
				layui.layer.full(window.sessionStorage.getItem("navIndex"));
			})
		}
			
	});

})