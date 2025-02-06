const DELETE_MESSAGE = "削除してもよろしいですか？"
$('.delete-action').click(function() {
	if(!confirm(DELETE_MESSAGE)){
		return false;
	}
});