$(function() {
	$(window).on("load", selectCase);
	$('#submit_case_id').change(selectCase);

	$(window).on("load", selectQuotation);
	$('#submit_quotation_id').change(selectQuotation);

	$(window).on("load", selectInquiry);
	$('#submit_inquiry_id').change(selectInquiry);
})

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

function selectQuotation() {
	let quoId = document.getElementById("submit_quotation_id").value;

	if (quoId == "") {
		$(".quotation-items").css({ 'display': 'none' })
	} else if (quoId != null) {
		$(".quotation-items").css({ 'display': 'none' })
		$("div[name=" + quoId + "]").css({ 'display': 'block' })
	}
}

function selectInquiry() {
	let inqId = document.getElementById("submit_inquiry_id").value;

	if (inqId == "") {
		$(".inquiry-items").css({ 'display': 'none' })
	} else if (inqId != null) {
		$(".inquiry-items").css({ 'display': 'none' })
		$("div[name=" + inqId + "]").css({ 'display': 'block' })
	}
}