/**
 * 
 */
$(function() {
    var loginUrl = '/o2o02/shopadmin/localauthlogincheck';
    var loginCount = 0;

    $('#submit').click(function() {
        var userName = $('#username').val();
        var password = $('#psw').val();
        var verifyCodeActual = $('#j_captcha').val();
        var formData = new FormData();
        var needVerify = false;
        formData.append("userName", userName);
        formData.append("password", password);
        formData.append("verifyCodeActual", verifyCodeActual);
        
        if (loginCount >= 3) {
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            } else {
                needVerify = true;
            }
        }
        formData.append("needVerify", needVerify);
        $.ajax({
            url : loginUrl,
//            async : false,
//            cache : false,
//            type : "post",
//            dataType : 'json',
//            data : {
//                userName : userName,
//                password : password,
//                verifyCodeActual : verifyCodeActual,
//                needVerify : needVerify
//            },
			type : "POST",
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
            success : function(data) {
                if (data.success) {
                    $.toast(data.errMsg);
                    window.location.href = '/o2o02/shopadmin/shoplist';
                } else {
                    $.toast(data.errMsg);
                    loginCount++;
                    if (loginCount >= 3) {
                        $('#verifyPart').show();
                    }
                }
            }
        });
    });

    $('#register').click(function() {
        window.location.href = '/o2o02/shopadmin/register';
    });
});