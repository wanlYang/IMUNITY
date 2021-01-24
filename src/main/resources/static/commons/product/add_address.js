layui.config({
    base: getRealPath() + '/static/'
}).extend({
    citypicker: 'city-picker/city-picker' // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['jquery', 'table', 'citypicker','form','layer'], function () {
    var $ = layui.$
        , table = layui.table
        , form = layui.form
        , cityPicker = layui.citypicker
        , layer = layui.layer;

    var currentPicker = new cityPicker("#city-picker", {
        provincename:"provinceId",
        cityname:"cityId",
        districtname: "districtId",
        level: 'districtId',// 级别
    });
    currentPicker.setValue("陕西省/西安市/长安区");

    // 监听表单
    form.on("submit(addAddress)", function(data) {
        var index = top.layer.msg('数据提交中,请稍候', {
            icon: 16,
            time: false,
            shade: 0.8
        });
        // 实际使用时的提交信息
        $.ajax({
            type: "POST",
            url: getRealPath() + "/order/add/address/submit",
            data: data.field,
            success: function(result) {
                if(result.status == 200) {
                    layer.close(index);
                    layer.msg("地址添加成功！");
                    setTimeout(function() {
                        location.href = $(".backUrl").val();
                    }, 1000);
                }else{
                    layer.close(index);
                    layer.msg(result.message);
                }
            },
            complete: function (XMLHttpRequest,textStatus) {
                top.layer.close(index);
            }
        });
        return false;
    })


});
