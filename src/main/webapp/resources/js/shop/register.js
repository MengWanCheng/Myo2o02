/**
 * 
 */
$(function() {
	var registerUrl = '/o2o02/shopadmin/localauthregister';
	$('#submit').click(function() {
		var localAuth = {};
		var personInfo = {};
		localAuth.userName = $('#userName').val();
		localAuth.password = $('#password').val();
		personInfo.email = $('#email').val();
		personInfo.name = $('#name').val();
//		localAuth.personInfo = personInfo;
		var thumbnail = $('#small-img')[0].files[0];
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		formData.append('localAuthStr', JSON.stringify(localAuth));
		formData.append('personInfoStr', JSON.stringify(personInfo));
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : registerUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					window.location.href = '/o2o02/shopadmin/localauthlogin';
				} else {
					$.toast('提交失败！' + data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});

	$('#back').click(function() {
		window.location.href = '/o2o02/shopadmin/localauthlogin';
	});
});
