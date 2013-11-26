<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ page import = "
org.jfree.chart.*,
org.jfree.chart.title.*,
org.jfree.chart.plot.*,
org.jfree.chart.axis.*,
org.jfree.chart.labels.*,
org.jfree.chart.renderer.xy.*,
org.jfree.data.xy.*,
org.jfree.data.time.*,
org.jfree.ui.*,
org.jfree.chart.servlet.ServletUtilities,
java.awt.Font,
java.awt.Color,
java.awt.BasicStroke,
java.util.*,
java.text.*,
java.io.IOException,
org.androidpn.server.model.OnlineRecord "%> 

<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>

<%

boolean isLarge = Boolean.valueOf(request.getParameter("isLarge"));
int width = 500;
int height = 360;
if(isLarge) {
	width = 1200;
	height = 600;
}

Color bgColor = Color.lightGray;
Font titleFont = new Font("隶书", Font.ITALIC, 15);
Font labelFont = new Font("宋体", Font.PLAIN, 12);

String title = request.getParameter("title");
title = java.net.URLDecoder.decode(title, "UTF-8");

String startTimeStr = request.getParameter("start");
String endTimeStr = request.getParameter("end");
DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date endTime = endTimeStr==null ? new Date() : format.parse(endTimeStr);
Date startTime = startTimeStr==null ? new Date() : format.parse(startTimeStr);

String xAxis = request.getParameter("xAxis");

List<OnlineRecord> list = (List<OnlineRecord>)request.getAttribute("list");

Class periodClz = null;
String yAxis = "minute"; //y轴刻度
Long unit = 1000l*60;

if("day".equals(xAxis)) {
	periodClz = Day.class;
	yAxis = "小时";
	unit = 1000l*60*60; //1小时
} else if("month".equals(xAxis)) {
	periodClz = Month.class;
	yAxis = "天";
	unit = 1000l*60*60*24; //1天
} else if("hour".equals(xAxis)) {
	periodClz = Hour.class;
	yAxis = "分钟";
	unit = 1000l*60; //1分钟
} else if("minute".equals(xAxis)) {
	periodClz = Minute.class;
	yAxis = "秒";
	unit = 1000l; //1秒
} else {
	periodClz = Day.class;
	yAxis = "小时";
	unit = 1000l*60*60; //1小时
}


//----------------------------------------------------------------------------------------//

Map<RegularTimePeriod, Double> map = new HashMap<RegularTimePeriod, Double>();
RegularTimePeriod rtp = RegularTimePeriod.createInstance(periodClz, startTime, TimeZone.getDefault());
RegularTimePeriod endRtp = RegularTimePeriod.createInstance(periodClz, endTime, TimeZone.getDefault());
while(rtp.compareTo(endRtp)<=0) {
	map.put(rtp, 0d);
	rtp = rtp.next();
}

Date logout = null;
Date login = null;
for(int j=0; j<list.size(); j++) {
	OnlineRecord r = list.get(j);
	if(OnlineRecord.Action.logout.equals(r.getAction())) {
		logout = r.getTime();
	} else if(OnlineRecord.Action.login.equals(r.getAction()) && logout != null){
		login = r.getTime();
		RegularTimePeriod rtpLogin = RegularTimePeriod.createInstance(periodClz, login, TimeZone.getDefault());
		RegularTimePeriod rtpLogout = RegularTimePeriod.createInstance(periodClz, logout, TimeZone.getDefault());
		
		if(rtpLogout.equals(rtpLogin)) {
			//如果位于同一个时段
			Long online = logout.getTime() - login.getTime();
			map.put(rtpLogout, map.get(rtpLogout) + online);
		} else if(rtpLogout.compareTo(rtpLogin)>0){
			//如果位于不同时段
			while(!rtpLogout.equals(rtpLogin)) {
				Long online = logout.getTime() - rtpLogout.getFirstMillisecond() + 1;	
				map.put(rtpLogout, map.get(rtpLogout) + online);
				rtpLogout = rtpLogout.previous();
				logout = rtpLogout.getEnd();
			}
			//rtpLogout递减，直到位于同一时段
			Long online = logout.getTime() - login.getTime();
			map.put(rtpLogout, map.get(rtpLogout) + online);
		}
		logout = null;
	}
}

Set<RegularTimePeriod> keys1 = map.keySet();
for(RegularTimePeriod key : keys1) {
	map.put(key, map.get(key)/unit);
}


//----------------------------------------------------------------------------------//


//建立数据序列
TimeSeries timeSeries = new TimeSeries("在线时长", periodClz);
TimeSeries averageTS = new TimeSeries("平均在线时长", periodClz);
Double total = 0d;
Set<RegularTimePeriod> keys = map.keySet();
for(RegularTimePeriod key : keys) {
	timeSeries.add(key, map.get(key));
	total += map.get(key);
}
Double average = total/map.size();
for(RegularTimePeriod key : keys) {
	averageTS.add(key, average);
}

//数据列加到collection中去，现在是加了两组数据
TimeSeriesCollection lineDataset = new TimeSeriesCollection();
lineDataset.addSeries(timeSeries);
lineDataset.addSeries(averageTS);

//lineDataset.setDomainIsPointsInTime(true); //x轴上的刻度点代表的是时间点而不是时间段

//参数意义: legend:是否显示图例,true则会在图表的下方显示各条数据曲线的名称和颜色     tooltips:是否生成工具	 urls:是否生成URL链接
JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "时间", "在线时长/"+yAxis, lineDataset, true, false, false); 
chart.setTitle(new TextTitle(title, titleFont));

////设置子标题
//TextTitle subtitle = new TextTitle(title, new Font("黑体", Font.BOLD, 12)); 
//chart.addSubtitle(subtitle);

//字体模糊边界
chart.setAntiAlias(true);

//来得到所有数据点的集合,可以设置各条曲线的颜色，和坐标轴的距离，x轴、y轴的显示方式等等属性
XYPlot plot = (XYPlot) chart.getPlot(); 
plot.setBackgroundPaint(Color.lightGray); //设定图表数据显示部分背景色
plot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D)); //设定坐标轴与图表数据显示部分距离
plot.setDomainGridlinePaint(Color.white); //网格线纵向颜色
plot.setRangeGridlinePaint(Color.white); //网格线横向颜色

//数据点的调整
XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();
xylineandshaperenderer.setBaseShapesVisible(false);  //数据点可见
xylineandshaperenderer.setStroke(new BasicStroke(1.5f)); //线条粗细宽度
xylineandshaperenderer.setSeriesFillPaint(0, Color.BLUE);  //设置第一条曲线数据点颜色
xylineandshaperenderer.setSeriesFillPaint(1, Color.red);  //
//xylineandshaperenderer.setUseFillPaint(false); 



//设置数据值可见
xylineandshaperenderer.setBaseItemLabelsVisible(true);  
xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
		ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));  
xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator() {
	private static final long serialVersionUID = 1L;
	String last = "";
	@Override
	public String generateLabelString(XYDataset dataset, int series, int item) {
		String result = "";
		//在这里你就可以根据这个series和item判断是不是你要显示是的话就if(....)不是自然就是"";没东西显示了
		if(series == 1 && item != 0) {
			result = "";
		} else {
//			result = super.generateLabelString(dataset,series,item);//调用原来的方法显示出来	

			TimeSeriesCollection tsc = (TimeSeriesCollection) dataset;
			Number n = dataset.getY(series, item);
			Class clz = tsc.getSeries(series).getTimePeriodClass();
			NumberFormat nf = NumberFormat.getInstance();
			if(clz == Month.class || clz == Day.class) {
				nf.setMaximumFractionDigits(1); // 如果x轴是月或天为单位，带1个小数点
			} else if(clz == Hour.class || clz == Minute.class || clz == Second.class){
				nf.setMaximumFractionDigits(0); //如果x轴是小时或分钟或秒为单位，不带小数点
			} else {
				nf.setMaximumFractionDigits(1); //其它不确定，先带着吧
			}
			result = nf.format(n);
			
			if(!result.equals("") && result.equals(last)) {
				result = ""; //如果与前一个值相等，那就不要显示了
			} else {
				last = result;
			}
		}
		return result;
	}
});  
//xylineandshaperenderer.setBaseItemLabelPaint(new Color(102, 102, 102));

//得到横竖坐标轴，并转给相应类型，此图表纵轴为数值类型，横轴为时间类型，使用如下方式
NumberAxis numAxis = (NumberAxis)plot.getRangeAxis();
DateAxis  dateaxis =   (DateAxis)plot.getDomainAxis();

//设置x轴显示方式
SimpleDateFormat sdf = null;
DateTickUnit dtu = null; 
int interval = 1;
int max = 14;
if(map.size()>max) {
	interval = map.size()/max + 1; //如果数据过多，横坐标显示不下，则间隔设大点
}
if(periodClz == Day.class){ 
	sdf = new SimpleDateFormat("MM-dd");
	dtu = new DateTickUnit(DateTickUnit.DAY, interval);
} else if(periodClz == Month.class) {
	sdf = new SimpleDateFormat("yyyy-MM");
	dtu = new DateTickUnit(DateTickUnit.MONTH, interval);
} else if(periodClz == Hour.class) {
	sdf = new SimpleDateFormat("HH:00");
	dtu = new DateTickUnit(DateTickUnit.HOUR, interval);
} else if(periodClz == Minute.class) {
	sdf = new SimpleDateFormat("HH:mm");
	dtu = new DateTickUnit(DateTickUnit.MINUTE, interval);
}
dateaxis.setDateFormatOverride(sdf);
dateaxis.setTickUnit(dtu); 

//设置y轴显示方式
numAxis.setAutoTickUnitSelection(true);//数据轴的数据标签是否自动确定
NumberFormat nf = NumberFormat.getInstance();
if("小时".equals(yAxis) || "天".equals(yAxis)) {
	nf.setMaximumFractionDigits(1); // 如果是小时或天为单位，带1个小数点
} else if("分钟".equals(yAxis) || "秒".equals(yAxis)){
	nf.setMaximumFractionDigits(0); //如果是分钟或秒为单位，不带小数点
} else {
	nf.setMaximumFractionDigits(1); //其它不确定，先带着吧
}
nf.setMaximumFractionDigits(1);
numAxis.setNumberFormatOverride(nf);//设置y轴数据显示格式 


//------------------------create png-------------------------------------------------//

String chartPng = null;
try {
	chartPng = ServletUtilities.saveChartAsPNG(chart, width, height, null, session);
} catch (IOException e) {
	e.printStackTrace();
}

//   String chartPng = (String)request.getAttribute("chartPng");
   String graphURL = request.getContextPath() + "/DisplayChart?filename=" + chartPng; 
    
%>
 <img src="<%= graphURL %>" border=0 /> 

</body>

</html>