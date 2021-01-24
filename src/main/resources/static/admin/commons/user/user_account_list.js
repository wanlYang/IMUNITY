layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;
    	//用户账户
	    table.render({
		elem : '#userAccountList',
		method:"POST",
		page: true,
		height: "full-125",
		limits: [10, 15, 20, 25],
		limit: 20,
		url : getRealPath() + '/admin/manager/user/get/account/list/',
		cellMinWidth : 95,
		cols : [ [ {
			field : "accountId",
			title : 'ID',
			width : 60,
			fixed : "left",
			sort : "true",
			align : 'center'
		},{
			field : 'username',
			title : '用户名',
			align : 'center',
			templet:function(d){
					return d.user.username;
			}
		},{
			field : 'accountBalance',
			title : '账户余额',
			align : 'center',
			sort : "true",
		},{
			field : 'accountStatus',
			title : '账户状态',
			align : 'center',
			templet: function(d){
				if(d.accountStatus == 0){
					return "正常";
				}else{
					return "冻结";
				}
			}
		},{
			title: '操作',
			minWidth: 220,
			templet: '#accountListBar',
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
    $(".addAccount").click(function(){
    	var index = top.layer.msg("暂不提供添加账户功能");
    });
    // 列表操作
	table.on('tool(userAccount)', function(obj) {
		var layEvent = obj.event,
			data = obj.data;
		//监听单元格事件
		if(layEvent === "changeStatus") {
			var _this = $(this),
				frozenText = "是否确定冻结此账户？",
				btnText = "冻结";
			if(_this.text() == "冻结") {
				frozenText = "是否确定启解冻此账户？",
				btnText = "正常";
			}
			layer.confirm(frozenText, {
				icon: 3,
				title: '系统提示',
				cancel: function(index) {
					layer.close(index);
				}
			}, function(index) {
				//接口
				$.post(getRealPath() + "/admin/manager/user/account/change/status",{id:data.accountId,status:data.accountStatus},function(result){
					if(result.status == 200){
						_this.text(btnText);
						if (result.data == 0) {
							_this.removeClass("layui-btn-warm");
							_this.addClass("layui-btn-normal");
						}else if(result.data == 1){
							_this.removeClass("layui-btn-normal");
							_this.addClass("layui-btn-warm");
						}
						obj.update({
							accountStatus:result.data
						})
					}else{
						top.layer.msg(result.message);
					}
				},"json");
				layer.close(index);
			}, function(index) {
				layer.close(index);
			});
		}
			
	});

})