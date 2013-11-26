<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta name="menu" content="syncDB" />
<title>Sync database</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/tablesorter/style.css'/>" />
</head>
<body>
	<h1>Sync Demo Server端数据库信息：</h1>
	<a href="javascript:deleteAll()">全部删除</a>
	<a href="javascript:insert()">随机添加</a>
	<a href="javascript:update()">随机修改</a>
	<a href="javascript:del()">随机删除</a>
	<a href="javascript:sync()">同步数据库</a>
	<div id="list_div">
		<table id="tableList" class="tablesorter" cellspacing="1">
			<thead>
				<tr>
					<th>ID</th>
					<th>Key</th>
					<th>Value</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="bean" items="${list}">
					<tr>
						<td align="center">${ bean.id }</td>
						<td align="center">${ bean.key }</td>
						<td align="center">${ bean.value }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<script type="text/javascript">
		//<![CDATA[
			
		function deleteAll() {
			var listDiv = document.getElementById("list_div");

			listDiv.innerHTML = '正在删除...';
			$.ajax({
				url : '/MyPush/resources/sync/deleteAll',
				//async: true,
				type : 'GET',
				dataType : 'text',
				//data: {username: username},
				timeout : 1000,
				error : function() {
					alert('Error loading...');
				},
				success : function(res) {
					window.location.replace('/MyPush/syncDemo.do');
				}
			});
		}
		
		function insert() {
			var listDiv = document.getElementById("list_div");

			//listDiv.innerHTML = '正在添加...';
			$.ajax({
				url : '/MyPush/resources/sync/insert',
				//async: true,
				type : 'GET',
				dataType : 'text',
				//data: {username: username},
				timeout : 1000,
				error : function() {
					alert('Error loading...');
				},
				success : function(res) {
					window.location.replace('/MyPush/syncDemo.do');
				}
			});
		}
		
		function update() {
			var listDiv = document.getElementById("list_div");

			//listDiv.innerHTML = '正在更新...';
			$.ajax({
				url : '/MyPush/resources/sync/update',
				//async: true,
				type : 'GET',
				dataType : 'text',
				//data: {username: username},
				timeout : 1000,
				error : function() {
					alert('Error loading...');
				},
				success : function(res) {
					window.location.replace('/MyPush/syncDemo.do');
				}
			});
		}
		
		function sync() {
			$.ajax({
				url : '/MyPush/resources/sync/sync',
				type : 'GET',
				dataType : 'text',
				timeout : 1000,
				error : function() {
					alert('Error loading...');
				},
				success : function(res) {
					//window.location.replace('/MyPush/syncDemo.do');
					alert( res );
				}
			});
		}
		
		function del() {
			$.ajax({
				url : '/MyPush/resources/sync/delete',
				type : 'GET',
				dataType : 'text',
				timeout : 1000,
				error : function() {
					alert('Error loading...');
				},
				success : function(res) {
					window.location.replace('/MyPush/syncDemo.do');
				}
			});
		}
		
		//]]>
	</script>

</body>
</html>