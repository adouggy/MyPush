<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="menu" content="user" />
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
			 	<th id="id">#</th>
				<th id="Online" 
					class="link<% if("online".equals(column)) out.print(" order_"+type); %>"
					onclick="javascript:gotoPageByOrder('${pageIndex}','online','${orderColumn}','${orderType}');">
					Online</th>
				<th id="Username" 
					class="link<% if("username".equals(column)) out.print(" order_"+type); %>"
					onclick="javascript:gotoPageByOrder('${pageIndex}','username','${orderColumn}','${orderType}');">
				 	Username</th>
				<th id="Name" 
					class="link<% if("name".equals(column)) out.print(" order_"+type); %>"
					onclick="javascript:gotoPageByOrder('${pageIndex}','name','${orderColumn}','${orderType}');">
				 	Name</th>
				<th id="Email" 
					class="link<% if("email".equals(column)) out.print(" order_"+type); %>"
					onclick="javascript:gotoPageByOrder('${pageIndex}','email','${orderColumn}','${orderType}');">
				 	Email</th>
				<th id="Created" 
					class="link<% if("createdDate".equals(column)) out.print(" order_"+type); %>"
					onclick="javascript:gotoPageByOrder('${pageIndex}','createdDate','${orderColumn}','${orderType}');">
				 	Created</th>
				
				<th id="online_lastHour">online_lastHour</th>
				<th id="online_lastDay">online_lastDay</th>
				<th id="online_lastWeek">online_lastWeek</th>
				<th id="online_lastMonth">online_lastMonth</th>
				
				<!-- <th id="Operation">Operation</th> -->
				<th id="partner">partner</th>
				<th id="birthday">birthday</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${userList}">
				<tr>
					<td><c:out value="${user.id}" /></td>
					<td align="center"><c:choose>
							<c:when test="${user.online eq true}">
								<img src="images/user-online.png" />
							</c:when>
							<c:otherwise>
								<img src="images/user-offline.png" />
							</c:otherwise>
						</c:choose></td>
					<td><c:out value="${user.username}" /></td>
					<td><c:out value="${user.name}" /></td>
					<td><c:out value="${user.email}" /></td>
					<td align="center"><fmt:formatDate
							pattern="yyyy-MM-dd HH:mm:ss" value="${user.createdDate}" /></td>
							
					<td><fmt:formatNumber type="percent" minFractionDigits="1" 
							value="${user.online_percent_lastHour}" /></td>
					<td><fmt:formatNumber type="percent" minFractionDigits="1" 
							value="${user.online_percent_lastDay}" /></td>
					<td><fmt:formatNumber type="percent" minFractionDigits="1" 
							value="${user.online_percent_lastWeek}" /></td>
					<td ><fmt:formatNumber type="percent" minFractionDigits="1" 
							value="${user.online_percent_lastMonth}" /></td>
					
					<%-- <td>
						<button onclick="javascript:manage('${user.username}');">管理</button>
						<button onclick="javascript:manageb('${user.username}');">管理2</button>
						<button onclick="javascript:deleteUser('${user.username}');">删除</button>
					</td> --%>
					<td><c:out value="${user.partner}" /></td>
					<td><c:out value="${user.birthday}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div id="pagination">
		<ul>			
			<%
				final int maxShowSize = 8;
				int start = pageIndex - maxShowSize/2;
				int end = pageIndex + maxShowSize/2 -1; //否则就9个了
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
		</ul>
	</div>
</body>
</html>