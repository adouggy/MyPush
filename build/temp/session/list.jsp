<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Admin Console</title>
<meta name="menu" content="session" />
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/table.css'/>" />
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.10.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery.tablesorter.js'/>"></script>
	<script type="text/javascript">
		//<![CDATA[
	$(document).ready(function () {
		pagination(1,"createdDate","desc");
	});

		function pagination(idx, orderColumn, orderType){
			$.ajax({
			    url: '/MyPush/session.do?pageIndex='+idx+'&orderColumn='+orderColumn+'&orderType='+ orderType+
			    	'&action=getSessionList',
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
			    url: '/MyPush/session.do?pageIndex='+idx+'&orderColumn='+orderColumn+'&orderType='+ orderType+
			    		'&action=getSessionList',
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

<h1>Sessions</h1>

<div id="tablediv"></div>



</body>
</html>
