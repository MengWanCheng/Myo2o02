/**
 * 
 */
$(function() {
    getlist();
    function getlist(e) {
        $.ajax({
            url : "/o2o02/shopadmin/getshoplist",
            type : "get",
            dataType : "json",
            success : function(data) {
                if (data.success) {
                    handleList(data.shopList);
                    handleUser(data.user);
                }
            }
        });
    }
    function handleUser(data) {
        $('#user-name').text(data.name);
    }

    function handleList(data) {
        var html = '';
        data.map(function(item, index) {
            html += '<div class="row row-shop"><div class="col-40">'
                    + item.shopName + '</div><div class="col-40">'
                    + shopStatus(item.enableStatus)
                    + '</div><div class="col-20">'
                    + goShop(item.enableStatus, item.shopId) + '</div></div>';

        });
        $('.shop-wrap').html(html);
    }
    
    function shopStatus(status) {
        if (status == 0) {
            return '审核中';
        } else if (status == -1) {
            return '店铺非法';
        } else {
            return '审核通过';
        }
    }
    
    function goShop(status, id) {
        if (status == 1) {
        	//我靠，好坑呀，必须清理缓存才能在浏览器中更新js文件，
            return '<a href="/o2o02/shopadmin/shopmanagement?shopId='+ id +'">进入</a>';
        } else {
            return '';
        }
    }
    $('#log-out').click(function(){
    	$.ajax({
    		url : "/o2o02/shopadmin/logout",
    		type : "post",
    		contentType : false,
    		processData : false,
    		cache : false,
    		success : function(data){
    			if(data.success){
    				window.location.href = '/o2o02/shopadmin/localauthlogin';
    			}
    		},
    		error : function(data, error){
    			alert(error);
    		}
    	});
    });
    
});