layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    table.render({
		elem : '#navRoleListTable',
		method:"POST",
		page: true,
		limit: 20,
		limits: [20, 30, 40],
		url : getRealPath() + '/admin/manager/nav/get/nav/role/list',
		cols : [ [ {
			field : "id",
			title : '导航权限ID',
			width : 125,
			fixed : "left",
			sort : "true",
			align : 'center'
		}, {
			field : 'name',
			title : '权限名称',
			align : 'center',
			templet: function(d){
				return d.name;
			}
		}, {
			field : 'descritpion',
			title : '权限描述',
			align : 'center'
		}, {
			field : 'url',
			title : '权限url',
			align : 'center',
			templet: function(d){
				return d.url;
			}
		}, {
			field : 'pid',
			title : '父节点ID',
			align : 'center',
			templet: function(d){
				if (d.pid == "" || d.pid == null) {
					return "无";
				}
				return d.pid;
			}
		}, {
			field : 'roles',
			title : '权限',
			minWidth : 280,
			align : 'center',
			templet: function(d){
				var roleHtml = '';
				for (var i = 0; i < d.roles.length; i++) {
					roleHtml += '<span class="layui-btn layui-btn-green layui-btn-xs">'+d.roles[i].name+'</span>';
				}
				return roleHtml;
			}
		},{
			title: '操作',
			minWidth: 220,
			templet: '#navRoleListBar',
			fixed: "right",
			align: "center"
		}
		] ],
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
    $(".addNavRole").click(function(){
    	var index = layui.layer.open({
			title: "添加导航权限",
			type: 2,
			content: getRealPath() + "/admin/manager/nav/role/add/page",
			success: function(layero, index) {
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
		layui.layer.full(index);
		window.sessionStorage.setItem("index", index);
		// 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize", function() {
			layui.layer.full(window.sessionStorage.getItem("index"));
		})
    });
    // 列表操作
	table.on('tool(navRoleTable)', function(obj) {
		var layEvent = obj.event,
			data = obj.data;
		//监听单元格事件
		if(layEvent === "del") {
			layer.confirm('确定删除此导航权限信息?', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.post(getRealPath() + "/admin/manager/nav/submit/nav/role/delete",{id:data.id},function(result){
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
			var navRoleIndex = layui.layer.open({
				title: "编辑导航权限",
				type: 2,
				content: getRealPath() + "/admin/manager/nav/role/edit/page",
				success: function(layero, index) {
					var body = layui.layer.getChildFrame('body', index);
					var iframeWindow = window[layero.find('iframe')[0]['name']];
					var roles = data.roles;
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
										if (rolesCheckbox[j].value == roles[i].id) {
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
					body.find(".name").val(data.name);
					body.find(".descritpion").val(data.descritpion);
					body.find(".url").val(data.url);
					body.find(".id").val(data.id);
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
			layui.layer.full(navRoleIndex);
			window.sessionStorage.setItem("navRoleIndex", navRoleIndex);
			// 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
			$(window).on("resize", function() {
				layui.layer.full(window.sessionStorage.getItem("navRoleIndex"));
			})
		}
			
	});

})