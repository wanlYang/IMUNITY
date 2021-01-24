layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    //用户等级
	    table.render({
		elem : '#userGrade',
		method:"POST",
		url : getRealPath() + '/admin/manager/role/get/list/table',
		cellMinWidth : 95,
		cols : [ [ {
			field : "id",
			title : 'ID',
			width : 60,
			fixed : "left",
			sort : "true",
			align : 'center'
		}, {
			field : 'name',
			title : '字段/名称',
			align : 'center'
		},{
			title: '操作',
			minWidth: 220,
			templet: '#roleListBar',
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
    $(".addRole").click(function(){
    	var index = layui.layer.open({
			title: "添加权限",
			type: 2,
			content: getRealPath() + "/admin/manager/user/role/add",
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
	table.on('tool(userRole)', function(obj) {
		var layEvent = obj.event,
			data = obj.data;
		//监听单元格事件
		if(layEvent === "del") {
			layer.confirm('确定删除此权限?', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.post(getRealPath() + "/admin/manager/role/submit/del",{id:data.id},function(result){
					if(result.status == 200){
						obj.del();// 删除缓存
						top.layer.msg(result.message);
					}else{
						top.layer.msg(result.message);
					}
				},"json");
				layer.close(index);
			});
		}
			
	});

})