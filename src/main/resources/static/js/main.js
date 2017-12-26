const ERROR_NO_DATA = 'There is no data to update';
const ERROR_NO_DATA_SELECTED = 'No row selected';

function submitCrawling(action, method, input) {
    'use strict';
    let form;
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

function setupFormReset(selector) {
    $(selector).on('hidden.bs.modal', function (e) {
        $(this)
            .find("input,textarea,select")
            .val('')
            .end()
            .find("input[type=checkbox], input[type=radio]")
            .prop("checked", "")
            .end();
    })
}

function confirmDeletedDialog(msg, callbackFunction) {
    BootstrapDialog.confirm({
        title: 'WARNING',
        message: msg,
        type: BootstrapDialog.TYPE_DANGER,
        closable: true,
        draggable: true,
        btnCancelLabel: 'Cancel',
        btnOKLabel: 'Yes',
        btnOKClass: 'btn-warning',
        callback: function(result) {
            if (result) {
                callbackFunction();
            }
        }
    });
}

function errorDialog(msg) {
    BootstrapDialog.show({
        type: BootstrapDialog.TYPE_DANGER,
        title: 'ERROR!',
        message: msg
    });
}

function successDialog(msg) {
    BootstrapDialog.show({
        type: BootstrapDialog.TYPE_SUCCESS,
        title: 'SUCCESS',
        message: msg
    });
}