<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Admin Console</title>
<meta name="menu" content="user" />
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/table.css'/>" />
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.10.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery.tablesorter.js'/>"></script>
	<script type="text/javascript">
		//<![CDATA[
	$(document).ready(function () {
		pagination(1,"createdDate","desc");
	});

		function manage(username) {
			//alert(username);
			open('/MyPush/online.do?username='+username, '_blank');
		}
		function manageb(username) {
			//alert(username);
			open('/MyPush/online.do?action=query&username='+username, '_blank');
		}
		function deleteUser(id) {
			alert('确定删除用户:' + id);
			$.ajax({
				url : '/MyPush/resources/xmpp/user/' + id,
				type : 'DELETE',
				success : function(data, status) {
					window.location.replace('/MyPush/user.do');
				}
			});
		}
		function pagination(idx, orderColumn, orderType){
			$.ajax({
			    url: '/MyPush/user.do?pageIndex='+idx+'&orderColumn='+orderColumn+'&orderType='+ orderType+
			    	'&action=getUserList',
			    timeout: 5000,
			    success: function(html){
		    		$("#tablediv").html("");
		   			$("#tablediv").append(html);
			    }
			});
		}
		
		function gotoPageByOrder(idx, orderColumn, orderColumnNow, orderTypeNow) {
			var orderType;
			if(orderColumn == orderColumnNow) {
				if(orderTypeNow == "desc") orderType = "asc";
				if(orderTypeNow == "asc") orderType = "desc";
			} else {
				orderType = "desc";
			}
			
			$.ajax({
			    url: '/MyPush/user.do?pageIndex='+idx+'&orderColumn='+orderColumn+'&orderType='+ orderType+
			    		'&action=getUserList',
			    timeout: 5000,
			    success: function(html){
		    		$("#tablediv").html("");
		   			$("#tablediv").append(html);
			    }
			});
		}

		//]]>
	</script>
</head>

<body>
	<h1>Users</h1>
	<div id="tablediv"></div>


</body>

</html>
