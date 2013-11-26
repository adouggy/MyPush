<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="menu" content="session" />
<title>Admin Console</title>
</head>
<body>
	<%
	String column = (String)request.getAttribute("orderColumn");
	String type = (String)request.getAttribute("orderType");
	int pageIndex = (Integer)request.getAttribute("pageIndex");
	int pageCount = (Integer)request.getAttribute("pageCount");
	%>
		
		<table id="tableList" class="tableList" cellspacing="1">
		<thead>
			<tr>
			 
				<th id="Username"
					class="link<% if("username".equals(column)) out.print(" order_"+type); %>" 
					onclick="javascript:gotoPageByOrder('${pageIndex}','username','${orderColumn}','${orderType}');">
					Username</th>
				<th id="resource"
					class="link<% if("resource".equals(column)) out.print(" order_"+type); %>" 
					onclick="javascript:gotoPageByOrder('${pageIndex}','resource','${orderColumn}','${orderType}');">
				 	Resource</th>
				<th id="Status" class="" >Status</th>
				<th id="Presence" class="" >Presence</th>  
				<th id="ClientIp"
					class="link<% if("clientIp".equals(column)) out.print(" order_"+type); %>" 
					onclick="javascript:gotoPageByOrder('${pageIndex}','clientIp','${orderColumn}','${orderType}');">
				 	Client IP</th>
				<th id="Created"
					class="link<% if("createdDate".equals(column)) out.print(" order_"+type); %>" 
					onclick="javascript:gotoPageByOrder('${pageIndex}','createdDate','${orderColumn}','${orderType}');">
				 	Created</th>
			
			</tr>
		</thead>
		<tbody>
		<c:forEach var="sess" items="${sessionList}">
			<tr>
				<td><c:out value="${sess.username}" /></td>
				<td><c:out value="${sess.resource}" /></td>
				<td align="center"><c:out value="${sess.status}" /></td>
				<td>
					<c:choose>
					<c:when test="${sess.presence eq 'Online'}">
						<img src="images/user-online.png" />
					</c:when>
					<c:when test="${sess.presence eq 'Offline'}">
						<img src="images/user-offline.png" />
					</c:when>
					<c:otherwise>
						<img src="images/user-away.png" />
					</c:otherwise>
					</c:choose>
					<c:out value="${sess.presence}" />
				</td>
				<td><c:out value="${sess.clientIP}" /></td>
				<td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${sess.createdDate}" /></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<div id="pagination">
		<ul>			
			<%
				final int maxShowSize = 8;
				int start = pageIndex - maxShowSize/2;
				int end = pageIndex + maxShowSize/2 - 1; //否则就9个了
				if( start < 1 ){
					start = 1;
					end = maxShowSize;
					end = (end > pageCount)?pageCount:end;
				}
				if( end > pageCount ){
					end = pageCount;
					start = pageCount - maxShowSize + 1;
					start = (start < 1)?1:start;
				}
				
				out.print("<li><a href=\"javascript:pagination(1,\'"+column+"\',\'"+type+"\')\">首页</a></li>");
				out.print("<li><a href=\"javascript:pagination(" + (pageIndex<=1?1:pageIndex-1)  + ",\'"+column+"\',\'"+type+"\')\">上页</a></li>");
				for( int i=start; i<=end; i++ ){
					if( i == pageIndex ){
						out.print( "<li class=\"current\"><a href=\"javascript:pagination("+ i +",\'"+column+"\',\'"+type+"\');\">" + i + "</a></li>" );
					}else{
						out.print( "<li><a href=\"javascript:pagination("+ i +",\'"+column+"\',\'"+type+"\');\">" + i + "</a></li>" );
					}
				}
				out.print("<li><a href=\"javascript:pagination(" + (pageIndex>=pageCount?pageCount:pageIndex+1) + ",\'"+column+"\',\'"+type+"\')\">下页</a></li>");
				out.print("<li><a href=\"javascript:pagination(" + pageCount + ",\'"+column+"\',\'"+type+"\')\">尾页</a></li>");
			%>
			<li class="pageinfo">第${ pageIndex }页</li>
			<li class="pageinfo">共${ pageCount }页</li>
			<li class="pageinfo">共${ totalCount }个</li>
		</ul>
	</div>
</body>
</html>