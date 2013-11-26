<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ page import = "org.jfree.chart.ChartFactory,org.jfree.chart.JFreeChart, org.jfree.chart.servlet.ServletUtilities,
org.jfree.chart.title.TextTitle, org.jfree.data.time.TimeSeries,
org.jfree.data.time.Day,org.jfree.chart.plot.XYPlot,
org.jfree.data.time.TimeSeriesCollection,
java.awt.Font,
org.jfree.chart.renderer.xy.XYLineAndShapeRenderer,
org.jfree.chart.renderer.xy.XYItemRenderer,
org.jfree.ui.RectangleInsets,
org.jfree.chart.labels.*,
org.jfree.ui.*,
org.jfree.chart.axis.*,
org.androidpn.server.model.OnlineRecord,
java.util.*"%> 
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<page:applyDecorator name="default">
<head>
<meta name="menu" content="user" />
<style type='text/css'>

	#begintime,#endtime,#xAxis,#query,#xAxisCount,#queryCount{
		padding-left: 10px;
		float: left;
		margin-top:4px;
		margin-bottom:5px; 
	}


</style>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.10.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js'/>"></script>
	<script type="text/javascript">
		//<![CDATA[
	$(document).ready(function () {

	});

	function query(){
		var username = "${username}";
		var start = $("#begintime_input").val();
		var end = $("#endtime_input").val();
		var xAxis = $("#xAxis_select").val();
		var title = "在线时长统计";
		getChart(username, start, end, xAxis, title, "chart1");
	}
	
	function queryCount(){
		var username = "${username}";
		var start = $("#begintime_input").val();
		var end = $("#endtime_input").val();
		var xAxis = $("#xAxis_count_select").val();
		var title = "在线人数统计";
		getCountChart(start, end, xAxis, title, "chart2");
	}	
	
	function getChart(username, start, end, xAxis, title, div){
		title = encodeURI(title);
		title = encodeURI(title); //中文需要转码，而且需要转码两次，而且只不能整个url一起转码，否则时间格式会不对
		url = '/MyPush/online.do?action=getChart'
				+'&username='+username
				+'&start='+start
				+'&end='+end
				+'&xAxis='+xAxis
				+'&title='+title
				+'&isLarge='+true;
		$.ajax({
		    url: url,
		    timeout: 5000,
		    contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		    success: function(html){
	    		$("#"+div).html("");
	   			$("#"+div).append(html);
		    }
		});
	}

	function getCountChart(start, end, xAxis, title, div){
		title = encodeURI(title);
		title = encodeURI(title); //中文需要转码，而且需要转码两次，而且只不能整个url一起转码，否则时间格式会不对
		url = '/MyPush/online.do?action=getCountChart'
				+'&start='+start
				+'&end='+end
				+'&xAxis='+xAxis
				+'&title='+title
				+'&isLarge='+true;
		$.ajax({
		    url: url,
		    timeout: 5000,
		    contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		    success: function(html){
	    		$("#"+div).html("");
	   			$("#"+div).append(html);
		    }
		});
	}
	
	
</script>
<title>online status chart</title>
</head>
<body>
<h1>在线状况统计</h1>

<div id="begintime">起始时间：<input id="begintime_input"  type="text" value="${begin }" 
	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></input></div>
<div id="endtime">结束时间：<input id="endtime_input" type="text" value="${end }" 
	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></input></div>
<div id="xAxis">在线时间查询-x轴：<select name="select" id="xAxis_select" class="">
	<option value="day" <c:if test="${xAxis == 'day' }">selected="selected"</c:if>>天 </option>
	<option value="hour" <c:if test="${xAxis == 'hour' }">selected="selected"</c:if>>小时 </option>
	<option value="minute" <c:if test="${xAxis == 'minute' }">selected="selected"</c:if>>分钟 </option>
	<option value="second" <c:if test="${xAxis == 'second' }">selected="selected"</c:if>>秒</option>
	<option value="month" <c:if test="${xAxis == 'month' }">selected="selected"</c:if>>月</option>
	</select>
</div>
<div id="query"> <button onclick="javascript:query();">在线时间查询</button> </div>

<div id="xAxisCount">在线人数查询-x轴：<select name="select" id="xAxis_count_select" class="">
	<option value="hour" <c:if test="${xAxis == 'hour' }">selected="selected"</c:if>>小时 </option>
	<option value="day" <c:if test="${xAxis == 'day' }">selected="selected"</c:if>>天 </option>
	<option value="month" <c:if test="${xAxis == 'month' }">selected="selected"</c:if>>月</option>
	</select>
</div>
<div id="queryCount"> <button onclick="javascript:queryCount();">在线人数查询</button> </div>

<br />
<br />
<div id="chart1"></div>
<div id="chart2"></div>

</body>
</page:applyDecorator>
</html>