<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta name="menu" content="function" />
<title>推送服务器管理</title>
<script type="text/javascript" src="<c:url value='/scripts/jquery.js'/>"></script>
</head>
<body>
	
	<h1>在线人数: ${onlineCount } / ${totalCount}</h1>
	
	<h1>消息数: ${msgCount }</h1>
	
	<h1>发送数: ${sentCount }</h1>
	
	<script type="text/javascript">
	//<![CDATA[
		
	//]]>
	</script>



</body>
</html>