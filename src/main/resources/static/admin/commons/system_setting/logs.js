layui.use(['table'], function() {
	var table = layui.table;

	//系统日志
	table.render({
		elem: '#logs',
		url: getRealPath() + '/admin/manager/system_setting/getlogs',
		cellMinWidth: 95,
		page: true,
		height: "full-20",
		limit: 20,
		method: 'post',
		limits: [10, 15, 20, 25],
		id: "systemLog",
		cols: [
			[{
					type: "checkbox",
					fixed: "left",
					width: 50
				},
				{
					field: 'id',
					title: '序号',
					width: 60,
					align: "center"
				},
				{
					field: 'requestUrl',
					title: '请求地址',
					width: 350
				},
				{
					field: 'requestMethod',
					title: '操作方式',
					align: 'center',
					templet: function(d) {
						if(d.requestMethod.toUpperCase() == "GET") {
							return '<span class="layui-blue">' + d.requestMethod + '</span>'
						} else {
							return '<span class="layui-red">' + d.requestMethod + '</span>'
						}
					}
				},
				{
					field: 'ipAddress',
					title: '操作IP',
					align: 'center',
					minWidth: 130
				},
				{
					field: 'timeConsuming',
					title: '耗时',
					align: 'center',
					templet: function(d) {
						return '<span class="layui-btn layui-btn-normal layui-btn-xs">' + d.timeConsuming + '</span>'
					}
				},
				{
					field: 'isAbnormal',
					title: '是否异常',
					align: 'center',
					minWidth: 50,
					templet: function(d) {
						if(d.isAbnormal == "正常") {
							return '<span class="layui-btn layui-btn-green layui-btn-xs">' + d.isAbnormal + '</span>'
						} else {
							return '<span class="layui-btn layui-btn-danger layui-btn-xs">' + d.isAbnormal + '</span>'
						}
					}
				},
				{
					field: 'operator',
					title: '操作人',
					minWidth: 100,
					templet: '#newsListBar',
					align: "center"
				},
				{
					field: 'modelName',
					title: '模块',
					minWidth: 100,
					templet: '#newsListBar',
					align: "center"
				},
				{
					field: 'operatingTime',
					title: '操作时间',
					align: 'center',
					width: 210,
					templet: function(d) {
						return Format(d.operatingTime,"yyyy-MM-dd hh:mm:ss.S")
					}
				}
			]
		],
		text: {
		    none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
		},
		response: {
			statusName: 'status', //规定数据状态的字段名称，默认：code
			statusCode: 200, //规定成功的状态码，默认：0
			msgName: 'message', //规定状态信息的字段名称，默认：msg
			countName: 'count', //规定数据总数的字段名称，默认：count
			dataName: 'data' //规定数据列表的字段名称，默认：data
		}
	});
})