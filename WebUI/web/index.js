function checkIfSignedUp() {

    $.ajax
    ({
        url: 'login',
        data: {action: "checkIfAlreadySignedUp"},
        type: 'GET',
        success: function(data) {
            if (data.m_HasRequestSucceeded == true) {
                window.location.href = data.m_RedirectURL;
            }
        }
    });
}

window.onload = function ()
{
   checkIfSignedUp();
};

