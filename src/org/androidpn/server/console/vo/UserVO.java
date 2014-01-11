package org.androidpn.server.console.vo;

import java.util.Date;

public class UserVO {

    private boolean online;

    private String username;

    private String name;

    private String email;

    private Date createdDate;

    private double online_percent_lastWeek;
    
    private double online_percent_lastMonth;
    
    private double online_percent_lastDay;
    
    private double online_percent_lastHour;
    
    private int partner;
    
    private int id;
    
    private long birthday;

	public UserVO() {
    	
    }
    
    public UserVO(String username, String name, String email, Date createdDate, int id, int partner, long birthday) {
    	this.username = username;
    	this.name = name;
    	this.email = email;
    	this.createdDate = createdDate;
    	this.birthday = birthday;
    	this.id = id;
    	this.partner = partner;
    }

	public boolean isOnline() {
		return online;
	}

	public double getOnline_percent_lastWeek() {
		return online_percent_lastWeek;
	}

	public void setOnline_percent_lastWeek(double online_percent_lastWeek) {
		this.online_percent_lastWeek = online_percent_lastWeek;
	}

	public double getOnline_percent_lastMonth() {
		return online_percent_lastMonth;
	}

	public void setOnline_percent_lastMonth(double online_percent_lastMonth) {
		this.online_percent_lastMonth = online_percent_lastMonth;
	}

	public double getOnline_percent_lastDay() {
		return online_percent_lastDay;
	}

	public void setOnline_percent_lastDay(double online_percent_lastDay) {
		this.online_percent_lastDay = online_percent_lastDay;
	}

    public double getOnline_percent_lastHour() {
		return online_percent_lastHour;
	}

	public void setOnline_percent_lastHour(double online_percent_lastHour) {
		this.online_percent_lastHour = online_percent_lastHour;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getPartner() {
		return partner;
	}

	public void setPartner(
			int partner) {
		this.partner = partner;
	}

	public int getId() {
		return id;
	}

	public void setId(
			int id) {
		this.id = id;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(
			long birthday) {
		this.birthday = birthday;
	}

}
