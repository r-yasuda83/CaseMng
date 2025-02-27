// 読み込み時
$(window).on('load', function() {
	const button = document.getElementById('insert');
	let option = "<option value=''></option>";
	for (let item of productList) {
		option += "<option value=" + item.id + ">" + item.productName + "</option>"
	}

	button.addEventListener('click', function() {
		addRow(option, distinction);
		selectPrice();
		rowsum();
		totalPrice();
		$('.select2').select2({
			width: '100%'
		});
	});

	selectPrice();
	rowsum();
	totalPrice();
	$('.select2').select2({
		width: '100%'
	});
});

// 商品変更時
$(document).ready(function() {
	$("body").on("change", ".product", function() {
		selectPrice();
		rowsum();
		totalPrice();
	})
});

// 個数、値引きフォーカスアウト時
$(document).ready(function() {
	$("body").on("focusout", ".quantity, .discount", function() {
		rowsum();
		totalPrice();
	})
});


// 行計算
function rowsum() {
	$('#itemTable tbody tr, #addTable tbody tr').each(function() {
		var price = parseInt($(this).find('.price').text());
		var quantity = $(this).find('.quantity').val();
		var discount = $(this).find('.discount').val();
		var result = price * quantity - discount;

		$(this).find('.rowTotal').text(result);
	});
}

// 商品金額反映
function selectPrice() {
	$('#itemTable tbody tr, #addTable tbody tr').each(function() {
		let id = $(this).find(".product").val();
		for (var i = 0; i < productList.length; i++) {
			if (id == productList[i].id) {
				$(this).find('.price').text(productList[i].price);
			} else if (id == 0) {
				$(this).find('.price').text(0);
			}
		}
	});
}

// 合計金額
function totalPrice() {
	var tableTotal = 0;
	$('#itemTable tbody tr, #addTable tbody tr').each(function() {
		var rowTotal = parseInt($(this).find(".rowTotal").text());
		tableTotal = tableTotal + rowTotal;
	});
	$('.totalPrice').text(tableTotal.toString());
}

//商品追加
function addRow(option, distinction) {
	const container = document.getElementById('tbody');

	let select = `
	<tr>
	<td class="col-md-2">
		<input type="hidden" th:field="*{` + distinction + `[${container.children.length}].id}">
		<select class="product select2" name="` + distinction + `[${container.children.length}].productId">
		`+ option + `
		</select>
	</td>
	<td class="col-md-1">
		<span class="price" id="price"></span>
	</td>
	<td class="col-md-1">
		<input class="form-control quantity" type="number" min="0" name="` + distinction + `[${container.children.length}].quantity">
	</td>
	<td class="col-md-1">
		<input class="form-control discount" type="number" min="0" name="` + distinction + `[${container.children.length}].discount">
	</td>
	<td class="col-md-1">
		<span class="rowTotal" id="rowTotal"></span>
	</td>
	<td class="col-md-1">
		<div class="error" style="color:red;"></div>
	</td>
	</tr>`;

	if (container !== null) {
		container.insertAdjacentHTML('beforeend', select);
	}
}