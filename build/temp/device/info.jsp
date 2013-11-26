<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta name="menu" content="deviceInfo" />
<title>设备信息</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/tablesorter/style.css'/>" />
</head>
<body>
	<h1>请选择已记录的设备</h1>
	<select name="fruit"  multiple onchange="javascript:changeDevice();" id="deviceSelector">
		<c:forEach var="user" items="${userList}">
			<option value="${user.username}" ${ user.username==username?'selected':'' }>${user.username}
		</c:forEach>
	</select>
	<h1>设备(${ username })的MDM信息</h1>
	<h2>Root情况</h2>
	<div> 
		${ selectedUser.isRoot } 
	</div>
	
	<h2>adminPolicy情况</h2>
	<div> 
		${ selectedUser.adminPolicy } 
	</div>
	
	<h2>Sim卡</h2>
	<div> 
		${ selectedUser.simInfo } 
	</div>
	
	<h2>系统信息</h2>
	<div> 
		${ selectedUser.sysList } 
	</div>
	
	<h2>GPS位置</h2>
	<div> 
		${ selectedUser.locationGPS } 
	</div>
	
	<h2>WIFI位置</h2>
	<div> 
		${ selectedUser.locationWIFI } 
	</div>
	
	<h2>androidID</h2>
	<div> 
		${ selectedUser.androidID } 
	</div>
	
	
	<h2>cpuID</h2>
	<div> 
		${ selectedUser.cpuID } 
	</div>
	
	<h2>telID</h2>
	<div> 
		${ selectedUser.telID } 
	</div>
	
	<h2>已安装程序</h2>
	<div>
		<table id="tableList" class="tablesorter" cellspacing="1">
			<thead>
				<tr>
					<th>App package</th>
					<th>Operation</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="app" items="${appList}">
					<tr>
						<td align="center">${ app }</td>
						<td>
							<button onclick="javascript:delete('${app}');">删除</button>
							<button onclick="javascript:void">详情</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<script type="text/javascript">
		//<![CDATA[
			function changeDevice(){
				var sel = document.getElementById('deviceSelector');
				if( sel ){
					window.location.replace('/MyPush/device.do?username=' + sel.value);
				}
			}
		//]]>
	</script>



</body>
</html>