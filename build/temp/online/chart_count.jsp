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
org.androidpn.server.model.OnlineUserCount "%> 

<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>

<%

//boolean isLarge = Boolean.valueOf(request.getParameter("isLarge"));
boolean isLarge = true;
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

List<OnlineUserCount> list = (List<OnlineUserCount>)request.getAttribute("list");

System.out.println("----list.size: " + list.size());

Class periodClz = null;
String yAxis = "人数"; //y轴刻度
Long unit = 1000l*60;

if("day".equals(xAxis)) {
	periodClz = Day.class;
} else if("month".equals(xAxis)) {
	periodClz = Month.class;
} else if("hour".equals(xAxis)) {
	periodClz = Hour.class;
} 

//----------------------------------------------------------------------------------------//

Map<RegularTimePeriod, Integer> map = new HashMap<RegularTimePeriod, Integer>();
RegularTimePeriod rtp = RegularTimePeriod.createInstance(periodClz, startTime, TimeZone.getDefault());
RegularTimePeriod endRtp = RegularTimePeriod.createInstance(periodClz, endTime, TimeZone.getDefault());
while(rtp.compareTo(endRtp)<=0) {
	map.put(rtp, 0);
	rtp = rtp.next();
}

for(OnlineUserCount uc : list) {
	RegularTimePeriod rtp2 = RegularTimePeriod.createInstance(periodClz, uc.getTime(), TimeZone.getDefault());
	map.put(rtp2, map.get(rtp2) + uc.getCount());
}

//----------------------------------------------------------------------------------//


//建立数据序列
TimeSeries timeSeries = new TimeSeries("在线人数", periodClz);
TimeSeries averageTS = new TimeSeries("平均在线人数", periodClz);
Integer total = 0;
Set<RegularTimePeriod> keys = map.keySet();
for(RegularTimePeriod key : keys) {
	timeSeries.add(key, map.get(key));
	total += map.get(key);
}
Integer average = total/map.size();
for(RegularTimePeriod key : keys) {
	averageTS.add(key, average);
}

//数据列加到collection中去，现在是加了两组数据
TimeSeriesCollection lineDataset = new TimeSeriesCollection();
lineDataset.addSeries(timeSeries);
lineDataset.addSeries(averageTS);

//lineDataset.setDomainIsPointsInTime(true); //x轴上的刻度点代表的是时间点而不是时间段

//参数意义: legend:是否显示图例,true则会在图表的下方显示各条数据曲线的名称和颜色     tooltips:是否生成工具	 urls:是否生成URL链接
JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "时间", "在线人数/", lineDataset, true, false, false); 
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
			nf.setMaximumFractionDigits(0);
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
}
dateaxis.setDateFormatOverride(sdf);
dateaxis.setTickUnit(dtu); 

//设置y轴显示方式
numAxis.setAutoTickUnitSelection(true);//数据轴的数据标签是否自动确定
NumberFormat nf = NumberFormat.getInstance();
nf.setMaximumFractionDigits(0);
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