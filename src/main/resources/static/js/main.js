function submitCrawling(action, method, input) {
    'use strict';
    var form;
    form = $('<form />', {
        action: action,
        method: method,
        style: 'display: none;'
    });
    if (typeof input !== 'undefined' && input !== null) {
        $.each(input, function (name, value) {
            $('<input />', {
                type: 'hidden',
                name: name,
                value: value
            }).appendTo(form);
        });
    }
    form.appendTo('body').submit();
}