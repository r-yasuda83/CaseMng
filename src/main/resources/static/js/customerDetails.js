document.addEventListener('DOMContentLoaded', function() {
	selectCase();
	selectQuotationValues();
	selectInquiryValues();

	onloadQuotationId();
	onloadInquiryId();
	
	selectQuotation();
	selectInquiry();
});
document.getElementById("submit_case_id").addEventListener('change', function() {
	selectCase();
	selectQuotationValues();
	selectInquiryValues();
});
document.getElementById("quotation_ids").addEventListener('change', function() {
	selectQuotation()
});
document.getElementById("inquiry_ids").addEventListener('change', function() {
	selectInquiry()
});


function selectCase() {
	let caseId = document.getElementById("submit_case_id").value;

	if (caseId == "") {
		$(".case-items").css({ 'display': 'none' })
		$(".quotation-items").css({ 'display': 'none' })
		$(".inquiry-items").css({ 'display': 'none' })
	} else if (caseId != null) {
		$(".case-items").css({ 'display': 'none' })
		$(".quotation-items").css({ 'display': 'none' })
		$(".inquiry-items").css({ 'display': 'none' })
		$("div[name=" + caseId + "]").css({ 'display': 'block' })
	}
}

function selectQuotationValues() {
	let caseId = document.getElementById("submit_case_id").value;
	let select = document.getElementById("quotation_ids");

	while (0 < select.childNodes.length) {
		select.removeChild(select.childNodes[0]);
	}

	for (var cases of customer.cases) {
		if ("case" + cases.id.toString() == caseId) {
			for (var quotation of cases.quotation) {
				var option = document.createElement('option');
				option.value = "quotation" + quotation.id;
				option.textContent = quotation.id;

				select.appendChild(option)
			}
		}
	}
}

function selectQuotation() {
	let quotationId = document.getElementById("quotation_ids").value;

	if (quotationId == "") {
		$(".quotation-items").css({ 'display': 'none' })
	} else if (quotationId != null) {
		$(".quotation-items").css({ 'display': 'none' })
		$("div[name=" + quotationId + "]").css({ 'display': 'block' })
	}
}

function onloadQuotationId() {
	const urlParams = new URLSearchParams(window.location.search);
	const quotationId = "quotation" + urlParams.get('quotationId');

	let select = document.getElementById("quotation_ids");

	[...select.options].forEach(option => {
		if (option.value == quotationId) {
			option.selected = true;
		}
	});
}

function selectInquiryValues() {
	let caseId = document.getElementById("submit_case_id").value;
	let select = document.getElementById("inquiry_ids");

	while (0 < select.childNodes.length) {
		select.removeChild(select.childNodes[0]);
	}

	for (var cases of customer.cases) {
		if ("case" + cases.id.toString() == caseId) {
			for (var inquiry of cases.inquiry) {
				var option = document.createElement('option');
				option.value = "inquiry" + inquiry.id;
				option.textContent = inquiry.id;

				select.appendChild(option)
			}
		}
	}
}

function selectInquiry() {
	let inquiryId = document.getElementById("inquiry_ids").value;

	if (inquiryId == "") {
		$(".inquiry-items").css({ 'display': 'none' })
	} else if (inquiryId != null) {
		$(".inquiry-items").css({ 'display': 'none' })
		$("div[name=" + inquiryId + "]").css({ 'display': 'block' })
	}
}

function onloadInquiryId() {
	const urlParams = new URLSearchParams(window.location.search);
	const inquiryId = "inquiry" + urlParams.get('inquiryId');
	
	let select = document.getElementById("inquiry_ids");

	[...select.options].forEach(option => {
		if (option.value == inquiryId) {
			option.selected = true;
		}
	});
}