function boxCloser(e){
    if(e.target.id != 'notification_list' &&
        e.target.id != 'notification_container' &&
        e.target.id != 'notification_container-number'){
        document.body.removeEventListener('click', boxCloser, false);
        $('.notification_list').hide();
    }
}
function boxCloserUserDropdown(e) {
    if(e.target.id != 'nav__user-dropdown-list' &&
        e.target.id != 'nav__userName') {
        document.body.removeEventListener('click', boxCloserUserDropdown, false);
        $('#nav__user-dropdown-list').hide();
    }
}

function toggleNotificationWindow() {
    $('.notification_list').show();
    document.body.addEventListener('click', boxCloser, false);
}

function markNotificationAsRead(notification) {
    var notifyCount = $("#notification_container-number");
    var notificationElement = $(".notification-id-"+notification);
    notificationElement.attr("onmouseover", "");

    $.get(window.location.protocol + "//" + window.location.host + "/m/notifications/markasread/"+notification, function(data) {
        if(notifyCount.html() > 0) {
            notifyCount.text(notifyCount.html() - 1);
            notificationElement.css("opacity", .5);
        }
    });
}

function displayUserDropdown() {
    $("#nav__user-dropdown-list").show();
    document.body.addEventListener('click', boxCloserUserDropdown, false);
}