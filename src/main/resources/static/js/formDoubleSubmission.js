function disableSubmitButton(form) {
    var button = form.querySelector('button[type="submit"]');
    if (button) {
        button.disabled = true;
    }
    return true;
}