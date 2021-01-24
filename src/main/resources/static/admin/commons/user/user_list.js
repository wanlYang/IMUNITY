layui.use(['form', 'layer', 'table', 'laytpl','laydate','jquery'], function() {
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laytpl = layui.laytpl,
		table = layui.table,
		laydate = layui.laydate;
	// 用户列表
	var tableIns = table.render({
		elem: '#userList',
		url: getRealPath() + '/admin/manager/user/get/list',
		cellMinWidth: 95,
		page: true,
		method: "POST",
		height: "full-125",
		limits: [10, 15, 20, 25],
		limit: 20,
		id: "userListTable",
		cols: [
				[	
				{
					type: "checkbox",
					fixed: "left",
					width: 50
				},
				{
					sort: true,
					field: "id",
					title: "ID",
					align: "center",
					minWidth: 80
				},
				{
					field: 'username',
					title: '用户名',
					minWidth: 100,
					align: "center"
				},
				{
					field: 'bindemail',
					title: '绑定邮箱',
					minWidth: 100,
					align: 'center',
					templet: function(d) {
						return '<a class="layui-blue" href="mailto:' + d.bindemail + '">' + d.bindemail + '</a>';
					}
				},
				{
					field: 'sex',
					title: '用户性别',
					align: 'center',
					templet: function(d) {
						if(d.sex == "male") {
							return "男";
						} else if(d.sex == "female") {
							return "女";
						} else {
							return "保密";
						}
					}
				},
				{
					field: 'status',
					title: '用户状态',
					align: 'center',
					templet: function(d) {
						if(d.status == 1) {
							return '<span class="layui-btn layui-btn-green layui-btn-xs">' + '正常使用' + '</span>'
						} else {
							return '<span class="layui-btn layui-btn-danger layui-btn-xs">' + '限制使用' + '</span>'
						}
					}
				},
				{
					field: 'roles',
					title: '权限信息',
					event: 'showRoles',
					align: 'center',
					templet: function(d) {
						return '<button class="layui-btn layui-btn-xs showRolesBtn">点击查看</button>';
					}
				},
				{
					sort: true,
					field: 'regDate',
					title: '注册日期',
					align: 'center',
					templet: function(d) {
						return Format(d.regDate, "yyyy-MM-dd hh:mm:ss.S")
					}
				},
				{
					title: '操作',
					minWidth: 280,
					templet: '#userListBar',
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
		},
		toolbar: true
	});
	// 检测select
	var keyWordType = "";
	form.on('select(skeyType)', function(data){
		if(data.value == ""){
			$("#keyWordBox").html("");
		}else{
			if(data.value == "user_sex"){
				$("#keyWordBox").html(
					"<input type='radio' name='sex' value='male' title='男' checked>" +
					"<input type='radio' name='sex' value='female' title='女'>"
				);
				keyWordType = "radio";
				form.render();
			}else{
				$("#keyWordBox").html(
					"<input type='text' class='layui-input searchVal' placeholder='请输入关键字'>"
				);
				keyWordType = "text";
				form.render();
			}
		}
	});   
	// 搜索
	var active = {
		reload: function(){
			var keyWord = "";
			console.log(keyWordType);
			if(keyWordType == "radio"){
				keyWord = $('input:radio[name="sex"]:checked').val();
			}else if(keyWordType == "text"){
				keyWord = $('.searchVal').val();
			}
            var keyType = $('.keyType');
            table.reload('userListTable', {
            	page: {
					curr: 1 // 重新从第 1 页开始
				},
				method:'post',
                where: {
                    keyType: keyType.val(),
                    keyWord: keyWord
                }
            });
        }
    };
	$(".search_btn").on("click", function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	// 添加用户
	function addUser() {
		var index = layui.layer.open({
			title: "添加用户",
			type: 2,
			content: getRealPath() + "/admin/manager/user/add",
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
	}
	$(".addNews_btn").click(function() {
		addUser();
	})

	// 批量删除
	$(".delAll_btn").click(function() {
		var checkStatus = table.checkStatus('userListTable'),
			data = checkStatus.data,
			delId = [];
		if(data.length > 0) {
			for(var i in data) {
				delId.push(data[i].id);
			}
			layer.confirm('确定删除选中的用户?', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				var delIndex = layer.msg("删除中...", {
					icon: 16,
					time: false,
					shade: 0.8
				});
				for (var i=0;i<delId.length;i++){
					if (delId[i] == currentUserId) {
						top.layer.msg("对不起不能选择自己!",{
	                		icon: 2,
	                		time: 1500,
	                		anim: 6
                    	})
						return false;
					}
					break;
				}
				$.ajax({
					type: "post",
					url: getRealPath() + "/admin/manager/user/submit/batch/delete",
					async: true,
					data: JSON.stringify(delId),
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
						layer.close(delIndex);
					}
				});
			})
		} else {
			layer.msg("请选择需要删除的用户");
		}
	})
	// 列表操作
	table.on('tool(userList)', function(obj) {
		var layEvent = obj.event,
			data = obj.data;
		//监听单元格事件
		if(obj.event === "showRoles") {
			//定义变量
			var rolesHtml = '';
			//遍历roles
			$.each(data.roles, function(index,value) {
				//追加变量
				rolesHtml +='<tr>' + 
								'<td>' + 
									value.name
								+ '</td>' + 
							'</tr>';
			});
			//打开权限窗
			layer.open({
				type: 1,
				title: data.username + ' 权限信息',
				closeBtn: 0, //不显示关闭按钮
				anim: 2,
				shadeClose: true, //开启遮罩关闭
				area: ['380px', '200px'],
				content:'<div style="padding:20px;">' + 
							'<ul id="htmlroles">' +
								'<table class="layui-table">' + 
									'<thead>' + 
										'<tr>' +
											'<th>' + 
												'角色名称' + 
											'</th>'  +
										'</tr>' + 
									'</thead>' + 
									'<tbody>' + 
										rolesHtml
									+ '</tbody>' + 
 								'</table>' +
							'</ul>' + 
						'</div>'
			});
		};
		// 监听操作
		if(layEvent === "editrole"){
			var roleIndex = layui.layer.open({
				title: "编辑权限",
				type: 2,
				content: getRealPath() + "/admin/manager/user/edit/role",
				success: function(layero, index) {
					var body = layui.layer.getChildFrame('body', index);
					var iframeWindow = window[layero.find('iframe')[0]['name']];
					console.log(iframeWindow.layui.form);
					console.log(data.roles);
					var roles = data.roles;
					//获取权限信息
					$.ajax({
						type: "POST",
						url: getRealPath() + "/admin/manager/role/get/list",
						success: function(result) {
							if(result.status == 200) {
								var roleCheckboxHtml = "";
								$.each(result.data, function(index,item) {
									roleCheckboxHtml += "<input type='checkbox' name='stringRoles' value="+item.id+" title="+item.name+">";
								});
								body.find(".stringRoles").append(roleCheckboxHtml);
								body.find("#id").val(data.id);
								for (var i = 0; i < roles.length; i++) {
									var rolesCheckbox = body.find(".stringRoles input[name='stringRoles']");
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
					if (typeof(iframeWindow.layui.form) != "undefined") {
						iframeWindow.layui.form.render();
					}
					setTimeout(function() {
						layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
							tips: 3
						});
						layui.layer.msg("更改为管理员权限!请谨慎操作!");
					}, 500)
				},
				end: function() {
					$(window).unbind("resize");
					
				}
			})
			layui.layer.full(roleIndex);
			window.sessionStorage.setItem("roleIndex", roleIndex);
			// 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
			$(window).on("resize", function() {
				layui.layer.full(window.sessionStorage.getItem("roleIndex"));
			})
		}else if(layEvent === 'showinfo') { // 详情
			function showStatus(status){
				if (status == 1) {
					return "正常使用";
				} else{
					return "限制使用";
				}
			}
			var _index = layui.layer.open({
				title: "用户详情",
				type: 2,
				content: getRealPath() + "/admin/manager/user/showinfo",
				success: function(layero, index) {
					var body = layui.layer.getChildFrame('body', index);
					var iframeWindow = window[layero.find('iframe')[0]['name']];
					body.find("#status").val(showStatus(data.status));
					body.find("#username").val(data.username);
					var roles = data.roles;
					var rolesHtml = "";
					for (var i = 0; i < roles.length; i ++) {
						rolesHtml += 	"<div class='layui-unselect layui-form-checkbox layui-form-checked'>" +
											"<span>"+roles[i].name+"</span><i class='layui-icon layui-icon-ok'></i>" + 
										"</div>";
					}
					body.find(".roles").html(rolesHtml);
					body.find("#bindemail").val(data.bindemail);
					body.find("#sex input[value="+data.sex+"]").prop("checked","checked");  //性别
					body.find("#age").val(data.age);
					body.find("#phone").val(data.phone);
					body.find("#birthday").val(Format(data.birthday, "yyyy-MM-dd"));
					body.find("#registDate").val(Format(data.regDate, "yyyy-MM-dd"));
					body.find("#profile").val(data.profile);
					if(typeof(data.headImg) != "undefined" && data.headImg != null){
						body.find("#userHeadImg").attr("src",getRealPath() + data.headImg);
					}else{
						body.find("#userHeadImg").attr("src",getRealPath() + "/admin/uploads/img/01460b57e4a6fa0000012e7ed75e83.png");
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
			layui.layer.full(_index);
			window.sessionStorage.setItem("_index", _index);
			// 改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
			$(window).on("resize", function() {
				layui.layer.full(window.sessionStorage.getItem("_index"));
			})
		} else if(layEvent === 'usable') { // 启用禁用
			var _this = $(this),
				usableText = "是否确定禁用此用户？",
				btnText = "已禁用";
			if(_this.text() == "已禁用") {
				usableText = "是否确定启用此用户？",
				btnText = "已启用";
			}
			layer.confirm(usableText, {
				icon: 3,
				title: '系统提示',
				cancel: function(index) {
					layer.close(index);
				}
			}, function(index) {
				_this.text(btnText);
				_this.toggleClass("layui-btn-warm");
				//启用禁用接口
				$.post(getRealPath() + "/admin/manager/user/usable",{id:data.id,status:data.status},function(result){
					if(result.status == 200){
						obj.update({
							status:result.data
						})
					}else{
						top.layer.msg("修改失败!");
					}
				},"json");
				layer.close(index);
			}, function(index) {
				layer.close(index);
			});
		} else if(layEvent === 'del') { // 删除
			layer.confirm('该操作会将用户的所有信息清空!<br/>确定删除此用户?', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.post(getRealPath() + "/admin/manager/user/submit/delete",{id:data.id},function(result){
					if(result.status == 200){
						obj.del();// 删除缓存
						top.layer.msg(result.message);
					}else{
						top.layer.msg("删除失败!");
					}
				},"json");
				layer.close(index);
			});
		}
	});

})