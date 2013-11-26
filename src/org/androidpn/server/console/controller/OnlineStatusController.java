/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.console.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineRecord;
import org.androidpn.server.model.OnlineUserCount;
import org.androidpn.server.model.User;
import org.androidpn.server.service.OnlineRecordService;
import org.androidpn.server.service.OnlineUserCountService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A controller class to process the user online status.
 * 
 */
public class OnlineStatusController extends MultiActionController {

	private UserService userService;
	private OnlineRecordService onlineService;
	private OnlineUserCountService onlineUserCountService;

	public OnlineStatusController() {
		userService = ServiceLocator.getUserService();
		onlineService = ServiceLocator.getOnlineRecordService();
		onlineUserCountService = ServiceLocator.getOnlineUserCountService();
	}

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		ModelAndView mav = new ModelAndView();
		mav.addObject("username", username);
		mav.setViewName("online/list");
		return mav;
	}
	
	public ModelAndView query(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		ModelAndView mav = new ModelAndView();
		mav.addObject("username", username);
		mav.setViewName("online/query");
		return mav;
	}
	
	public ModelAndView getChart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		User user = null;
		try {
			user = userService.getUserByUsername(username);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
//			return ;
		}

		String startTimeStr = request.getParameter("start");
		String endTimeStr = request.getParameter("end");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endTime = endTimeStr==null ? new Date() : format.parse(endTimeStr);
		Date startTime = startTimeStr==null ? new Date() : format.parse(startTimeStr);
	
		List<OnlineRecord> list = this.getOnlineList(user, startTime, endTime);
		
	//	System.out.println(format.format(startTime)+"-"+format.format(endTime)+": find" + list.size());
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.setViewName("online/chart");
		return mav;
	}
	
	public ModelAndView getCountChart(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String startTimeStr = request.getParameter("start");
		String endTimeStr = request.getParameter("end");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endTime = endTimeStr==null ? new Date() : format.parse(endTimeStr);
		Date startTime = startTimeStr==null ? new Date() : format.parse(startTimeStr);
	
		List<OnlineUserCount> list = onlineUserCountService.getAllRecords(startTime, endTime, "time", Order.asc);

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.setViewName("online/chart_count");
		return mav;
	}
	
	
	// get online list
	public List<OnlineRecord> getOnlineList(User user, Date startTime, Date endTime) {
		List<OnlineRecord> list = onlineService.getOnlineRecords(user, startTime, endTime, "time", Order.desc);
		
		//如果结果为0，则要看一下在这之前和之后是否有login和logout的记录，如果有则表示这段时间一直在线着
		if(list != null && list.size() == 0) {
			List<OnlineRecord> before = onlineService.getOnlineRecords(user, null, startTime, "time", Order.desc);
			List<OnlineRecord> after = onlineService.getOnlineRecords(user, endTime, null, "time", Order.asc);
			//如果之前是login，则表示此时段在线，给列表暂时增加一个startTime的login记录
			if(before.size() > 0 && before.get(0).getAction().equals(OnlineRecord.Action.login)) {
				OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.login, startTime);
				list.add(list.size(), record);
			}
			//如果之后是logout，则表示此时段在线，给列表暂时增加一个endTime的logout记录
			if(after.size() > 0 && after.get(0).getAction().equals(OnlineRecord.Action.logout)) {
				OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.logout, endTime);
				list.add(0, record);
			}
		}
		
		if(list == null || list.size()<=0) {
			return new ArrayList<OnlineRecord>();
		}

		//如果第一个是login，则认为用户在endTime时未登出，还在登录中，给列表暂加一个endTime时间的登出记录
		if(OnlineRecord.Action.login.equals(list.get(0).getAction())) {
			OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.logout, endTime);
			list.add(0, record);
		}
		//如果最后一个是logout，则认为用户在startTime时已登入，在登录中，给列表暂加一个startTime时间的登入记录
		if(OnlineRecord.Action.logout.equals(list.get(list.size()-1).getAction())) {
			OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.login, startTime);
			list.add(list.size(), record);
		}
		return list;
	}
	
//	public JFreeChart createPieChart(List<OnlineRecord> list, Date startTime, Date endTime, String title) {
//		
//		Map<String, Long> map = this.getPieChartData(list, startTime, endTime);
//		
//		DefaultPieDataset dpd = new DefaultPieDataset();
//		Set<String> keys = map.keySet();
//		for(String key : keys) {
//			dpd.setValue(key, map.get(key));
//		}
//		
//		//第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL
//		JFreeChart chart = ChartFactory.createPieChart(title, dpd, true, true, false); 
//		chart.setTitle(new TextTitle(title, titleFont));
////		chart.setBackgroundPaint(bgColor); //设置背景色
//		
//		PiePlot pieplot = (PiePlot)chart.getPlot(); //获得图表显示对象
////	    pieplot.setLabelFont(labelFont); //设置图表标签字体
////	    pieplot.setNoDataMessage("No data available"); //设置没有数据时显示的内容
//	    pieplot.setCircular(true); //true表示圆形 false表示椭圆
////	    pieplot.setLabelGap(0.01D);//间距
//	    //("{0}: ({1}，{2})")是生成的格式，{0}表示section名，{1}表示section的值，{2}表示百分比。可以自定义。而new DecimalFormat("0.00%")表示小数点后保留两位。
//		NumberFormat nf = NumberFormat.getInstance();
//		nf.setMaximumFractionDigits(0);
//	    StandardPieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(("{0}:{1}分钟,{2}"), nf, new DecimalFormat("0%"));
//	    pieplot.setLabelGenerator(generator);
//	    //指定 section 轮廓线的颜色
////	    pieplot.setBaseSectionOutlinePaint(new Color(0xF7, 0x79, 0xED));
//        //指定 section 轮廓线的厚度
////	    pieplot.setSectionOutlineStroke(new BasicStroke(0));
//        //指定 section 的色彩       
//	    pieplot.setSectionPaint("offline", Color.gray);        
//	    pieplot.setSectionPaint("online", Color.blue); 
//	    
//		return chart;
//	}
//	
//	public Map<String, Long> getPieChartData(List<OnlineRecord> list, Date startTime, Date endTime) {
//		Map<String, Long> map = new HashMap<String, Long>();
//		
//		Date logout = null;
//		Date login = null;
//		Long onlineTime = 0l;
//		for(int j=0; j<list.size(); j++) {
//			OnlineRecord r = list.get(j);
//			if(OnlineRecord.Action.logout.equals(r.getAction())) {
//				logout = r.getTime();
//			} else if(OnlineRecord.Action.login.equals(r.getAction()) && logout != null){
//				login = r.getTime();
//				onlineTime += logout.getTime()-login.getTime();
//				logout = null;
//			}
//		}
//		
//		Long minute = 1000l*60;	
//		map.put("offline", (endTime.getTime()-startTime.getTime()-onlineTime) /minute);
//		map.put("online", onlineTime /minute );
//
//		return map;
//	}
//	
//	@SuppressWarnings("deprecation")
//	public JFreeChart createLineChart(List<OnlineRecord> list, Date startTime, Date EndTime, Class periodClz, 
//			String title, String xLabel, String yLabel) {
//		
//		Map<RegularTimePeriod, Double> map = this.getLineChartData(list, periodClz, startTime, EndTime, yLabel);
//		
//		//建立数据序列
//		TimeSeries timeSeries = new TimeSeries("在线时长", periodClz);
//		TimeSeries averageTS = new TimeSeries("平均在线时长", periodClz);
//		Double total = 0d;
//		Set<RegularTimePeriod> keys = map.keySet();
//		for(RegularTimePeriod key : keys) {
//			timeSeries.add(key, map.get(key));
//			total += map.get(key);
//		}
//		Double average = total/map.size();
//		for(RegularTimePeriod key : keys) {
//			averageTS.add(key, average);
//		}
//		
//		//数据列加到collection中去，现在是加了两组数据
//		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
//		lineDataset.addSeries(timeSeries);
//		lineDataset.addSeries(averageTS);
//		
////		lineDataset.setDomainIsPointsInTime(true); //x轴上的刻度点代表的是时间点而不是时间段
//		
//		//参数意义: legend:是否显示图例,true则会在图表的下方显示各条数据曲线的名称和颜色     tooltips:是否生成工具	 urls:是否生成URL链接
//		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "时间", "在线时长/"+yLabel, lineDataset, true, false, false); 
//		chart.setTitle(new TextTitle(title, titleFont));
//
////		//设置子标题
////		TextTitle subtitle = new TextTitle(title, new Font("黑体", Font.BOLD, 12)); 
////		chart.addSubtitle(subtitle);
//
//		//字体模糊边界
//		chart.setAntiAlias(true);
//
//		//来得到所有数据点的集合,可以设置各条曲线的颜色，和坐标轴的距离，x轴、y轴的显示方式等等属性
//		XYPlot plot = (XYPlot) chart.getPlot(); 
//		plot.setBackgroundPaint(Color.lightGray); //设定图表数据显示部分背景色
//		plot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D)); //设定坐标轴与图表数据显示部分距离
//		plot.setDomainGridlinePaint(Color.white); //网格线纵向颜色
//		plot.setRangeGridlinePaint(Color.white); //网格线横向颜色
//		
//		//数据点的调整
//		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();
//		xylineandshaperenderer.setBaseShapesVisible(false);  //数据点可见
//		xylineandshaperenderer.setStroke(new BasicStroke(1.5f)); //线条粗细宽度
//		xylineandshaperenderer.setSeriesFillPaint(0, Color.BLUE);  //设置第一条曲线数据点颜色
//		xylineandshaperenderer.setSeriesFillPaint(1, Color.red);  //
////		xylineandshaperenderer.setUseFillPaint(false); 
//
//		
//		
//		//设置数据值可见
//        xylineandshaperenderer.setBaseItemLabelsVisible(true);  
//        xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(  
//        ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));  
//        xylineandshaperenderer.setBaseItemLabelGenerator(new MyGenerator());  
////        xylineandshaperenderer.setBaseItemLabelPaint(new Color(102, 102, 102));
//		
//		//得到横竖坐标轴，并转给相应类型，此图表纵轴为数值类型，横轴为时间类型，使用如下方式
//		NumberAxis numAxis = (NumberAxis)plot.getRangeAxis();
//		DateAxis  dateaxis =   (DateAxis)plot.getDomainAxis();
//
//		//设置x轴显示方式
//		SimpleDateFormat sdf = null;
//		DateTickUnit dtu = null; 
//		int interval = 1;
//		int max = 14;
//		if(map.size()>max) {
//			interval = map.size()/max + 1; //如果数据过多，横坐标显示不下，则间隔设大点
//		}
//		if(periodClz == Day.class){ 
//			sdf = new SimpleDateFormat("MM-dd");
//			dtu = new DateTickUnit(DateTickUnit.DAY, interval);
//		} else if(periodClz == Month.class) {
//			sdf = new SimpleDateFormat("yyyy-MM");
//			dtu = new DateTickUnit(DateTickUnit.MONTH, interval);
//		} else if(periodClz == Hour.class) {
//			sdf = new SimpleDateFormat("HH:00");
//			dtu = new DateTickUnit(DateTickUnit.HOUR, interval);
//		} else if(periodClz == Minute.class) {
//			sdf = new SimpleDateFormat("HH:mm");
//			dtu = new DateTickUnit(DateTickUnit.MINUTE, interval);
//		}
//		dateaxis.setDateFormatOverride(sdf);
//		dateaxis.setTickUnit(dtu); 
//		
//		//设置y轴显示方式
//		numAxis.setAutoTickUnitSelection(true);//数据轴的数据标签是否自动确定
//		NumberFormat nf = NumberFormat.getInstance();
//		if("小时".endsWith(yLabel) || "天".endsWith(yLabel)) {
//			nf.setMaximumFractionDigits(1); // 如果是小时或天为单位，带1个小数点
//		} else if("分钟".endsWith(yLabel) || "秒".endsWith(yLabel)){
//			nf.setMaximumFractionDigits(0); //如果是分钟或秒为单位，不带小数点
//		} else {
//			nf.setMaximumFractionDigits(1); //其它不确定，先带着吧
//		}
//		nf.setMaximumFractionDigits(1);
//		numAxis.setNumberFormatOverride(nf);//设置y轴数据显示格式 
//				
//		return chart;
//	}
//	
//	class MyGenerator extends StandardXYItemLabelGenerator {
//		private static final long serialVersionUID = 1L;
//		String last = "";
//		@Override
//		public String generateLabelString(XYDataset dataset, int series, int item) {
//			String result = "";
//			//在这里你就可以根据这个series和item判断是不是你要显示是的话就if(....)不是自然就是"";没东西显示了
//			if(series == 1 && item != 0) {
//				result = "";
//			} else {
////				result = super.generateLabelString(dataset,series,item);//调用原来的方法显示出来	
//
//				TimeSeriesCollection tsc = (TimeSeriesCollection) dataset;
//				Number n = dataset.getY(series, item);
//				Class clz = tsc.getSeries(series).getTimePeriodClass();
//				NumberFormat nf = NumberFormat.getInstance();
//				if(clz == Month.class || clz == Day.class) {
//					nf.setMaximumFractionDigits(1); // 如果x轴是月或天为单位，带1个小数点
//				} else if(clz == Hour.class || clz == Minute.class || clz == Second.class){
//					nf.setMaximumFractionDigits(0); //如果x轴是小时或分钟或秒为单位，不带小数点
//				} else {
//					nf.setMaximumFractionDigits(1); //其它不确定，先带着吧
//				}
//				result = nf.format(n);
//				
//				if(!result.equals("") && result.equals(last)) {
//					result = ""; //如果与前一个值相等，那就不要显示了
//				} else {
//					last = result;
//				}
//			}
//			return result;
//		}
//	}
//	
//	@SuppressWarnings("unchecked")
//	public Map<RegularTimePeriod, Double> getLineChartData(List<OnlineRecord> list, Class periodClz, 
//			Date startTime, Date endTime, String yAxisLabel) {
//		Map<RegularTimePeriod, Double> map = new HashMap<RegularTimePeriod, Double>();
//		RegularTimePeriod rtp = RegularTimePeriod.createInstance(periodClz, startTime, TimeZone.getDefault());
//		RegularTimePeriod endRtp = RegularTimePeriod.createInstance(periodClz, endTime, TimeZone.getDefault());
//		while(rtp.compareTo(endRtp)<=0) {
//			map.put(rtp, 0d);
//			rtp = rtp.next();
//		}
//		//先初始化一堆value=0的键值，如果没有在线记录，则直接返回该map
//		if(list == null || list.size()<=0) {
//			return map;
//		}
//		Date logout = null;
//		Date login = null;
//		for(int j=0; j<list.size(); j++) {
//			OnlineRecord r = list.get(j);
//			if(OnlineRecord.Action.logout.equals(r.getAction())) {
//				logout = r.getTime();
//			} else if(OnlineRecord.Action.login.equals(r.getAction()) && logout != null){
//				login = r.getTime();
//				RegularTimePeriod rtpLogin = RegularTimePeriod.createInstance(periodClz, login, TimeZone.getDefault());
//				RegularTimePeriod rtpLogout = RegularTimePeriod.createInstance(periodClz, logout, TimeZone.getDefault());
//				
//				if(rtpLogout.equals(rtpLogin)) {
//					//如果位于同一个时段
//					Long online = logout.getTime() - login.getTime();
//					map.put(rtpLogout, map.get(rtpLogout) + online);
//				} else if(rtpLogout.compareTo(rtpLogin)>0){
//					//如果位于不同时段
//					while(!rtpLogout.equals(rtpLogin)) {
//						Long online = logout.getTime() - rtpLogout.getFirstMillisecond() + 1;						
//						map.put(rtpLogout, map.get(rtpLogout) + online);
//						rtpLogout = rtpLogout.previous();
//						logout = rtpLogout.getEnd();
//					}
//					//rtpLogout递减，直到位于同一时段
//					Long online = logout.getTime() - login.getTime();
//					map.put(rtpLogout, map.get(rtpLogout) + online);
//				}
//				logout = null;
//			}
//		}
//		
//		Long unit = 1000l*60;
//		if("小时".endsWith(yAxisLabel)) {
//			unit = 1000l*60*60; //1小时
//		} else if("天".endsWith(yAxisLabel)) {
//			unit = 1000l*60*60*24; //1天
//		} else if("秒".endsWith(yAxisLabel)) {
//			unit = 1000l; //1秒
//		} else {
//			unit = 1000l*60; //1分钟
//		}
//		
//		Set<RegularTimePeriod> keys = map.keySet();
//		for(RegularTimePeriod key : keys) {
//			map.put(key, map.get(key)/unit);
//		}
//		
//		return map;
//	}
//
//
//	public String saveChartAsPNG(JFreeChart chart, int width, int height, HttpSession session) {
//		String filename = null;
//		try {
//			filename = ServletUtilities.saveChartAsPNG(chart, width, height, null, session);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return filename;
//	}
//	
}
