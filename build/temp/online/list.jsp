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
<head>
<meta name="menu" content="user" />
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.10.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js'/>"></script>
	<script type="text/javascript">
		//<![CDATA[
	$(document).ready(function () {
		var start = new Date();
		var end = new Date();
		
		start.setTime(end.getTime()-1000*60*60);
		getChart("${username}", toStr(start), toStr(end), 'minute', '过去60分钟在线状况', 'chart1');
		
		start.setTime(end.getTime()-1000*60*60*24);
		getChart("${username}", toStr(start), toStr(end), 'hour', '过去24小时在线状况', 'chart2');
		
		start.setTime(end.getTime()-1000*60*60*24*7);
		getChart("${username}", toStr(start), toStr(end), 'day', '过去7天在线状况', 'chart3');
		
		start.setTime(end.getTime()-1000*60*60*24*30);
		getChart("${username}", toStr(start), toStr(end), 'day', '过去30天在线状况', 'chart4');
		
	});

	function getChart(username, start, end, xAxis, title, div){
		title = encodeURI(title);
		title = encodeURI(title); //中文需要转码，而且需要转码两次，而且只不能整个url一起转码，否则时间格式会不对
		url = '/MyPush/online.do?action=getChart'
				+'&username='+username
				+'&start='+start
				+'&end='+end
				+'&xAxis='+xAxis
				+'&title='+title
				+'&isLarge='+false;
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
	
	
	function getNow() {
		var now = new Date();

	}
	
	function toStr(date) {
		var y = date.getFullYear(); //获取年
		var m = date.getMonth()+1; //获取月
		m=m>9?m:"0"+m; //如果月份小于10,则在前面加0补充为两位数字
		var d=date.getDate(); //获取日
		d=d>9?d:"0"+d; //如果天数小于10,则在前面加0补充为两位数字
		var h=date.getHours(); //获取小时
		h=h>9?h:"0"+h; //如果小时数字小于10,则在前面加0补充为两位数字
		var M=date.getMinutes(); //获取分
		M=M>9?M:"0"+M; //如果分钟小于10,则在前面加0补充为两位数字
		var s=date.getSeconds(); //获取秒
		s=s>9?s:"0"+s; //如果秒数小于10,则在前面加0补充为两位数字
		
		var dateStr = y+"-"+m+"-"+d+" "+h+":"+M+":"+s; //串联字符串用于输入
		return dateStr;
	}
	
</script>
<title>online status chart</title>
</head>
<body>
<h1>在线状况统计</h1>

<table  border="1" cellspacing="8">
		<thead> </thead>
		<tbody>
			<tr>
				<td> <div id="chart1"></div> </td>
				<td> <div id="chart2"></div> </td>
			</tr>
			<tr>
				<td> <div id="chart3"></div> </td>
				<td> <div id="chart4"></div> </td>
			</tr>
		</tbody>
	</table>


</body>

</html>