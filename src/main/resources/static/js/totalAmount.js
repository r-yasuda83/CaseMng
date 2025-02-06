//行計算
function rowsum() {
	$('#itemTable tbody tr, #addTable tbody tr').each(function() {

		var price = parseInt($(this).find('#price').html());
		var quantity = $(this).find('#quantity').val();
		var discount = $(this).find('#discount').val();
		var result = price * quantity - discount;

		$(this).find('#rowTotal').text(result);
	});
};

//商品金額反映
function selectPrice() {
	$('#itemTable tbody tr, #addTable tbody tr').each(function() {

		let id = $(this).find(".product").val();
		for (var i = 0; i < productList.length; i++) {
			if (id == productList[i].id) {
				$(this).find('#price').text(productList[i].price);
			}
		}
	});
};

//合計金額
function totalPrice() {
	var tableTotal = 0;
	$('#itemTable tbody tr').each(function() {
		var rowTotal = parseInt($(this).find("#rowTotal").html());
		tableTotal = tableTotal + rowTotal;
	});
	$('span#totalPrice').html(tableTotal.toString());
};

//読み込み時
$(window).on('load', function() {
	selectPrice();
	rowsum();
	totalPrice();
});
//商品変更時
$(document).ready(function() {
	$(".product").change(function() {
		selectPrice();
		rowsum();
		totalPrice();
	})
});
//個数、値引きフォーカスアウト時
$(document).ready(function() {
	$("#quantity,#discount").focusout(function() {
		rowsum();
		totalPrice();
	})
});