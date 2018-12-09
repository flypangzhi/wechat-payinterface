$(function() {
	$("#pay_submit").click(function() {
		buy('wx_51talk_003');// 传入可乐的ID号
	});

});

/**
 * 购买
 */
function buy(productId) {
	if (productId == "") {
		alert("请输入产品ID");
	} else {
		// 打开付费二维码 -- 微信二维码
		layer.open({
			area : [ '300px', '300px' ],
			type : 2,
			closeBtn : false,
			title : false,
			shift : 2,
			shadeClose : true,
			content : 'wechat/requestCode.do?productId=' + productId
		});

	}

	// 重复执行某个方法
	var t1 = window.setInterval("getPayState('" + productId + "')", 1500);
}

function getPayState(productId) {
	var url = 'user/hadPay.do?productId=' + productId;
	// 轮询是否已经付费
	$.ajax({
		type : 'post',
		url : url,
		data : {
			productId : productId
		},
		cache : false,
		async : true,
		success : function(json) {
			if (json.result == 0) {
				location.href = '/2.html';
			}
		},
		error : function() {
			layer.msg("执行错误！", 8);
		}
	});
}