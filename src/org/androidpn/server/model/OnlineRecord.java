package org.androidpn.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Index;

@Entity
@Table(name = "online_record")
public class OnlineRecord implements Serializable {
	public static enum Action {
		login, logout
	}
	
	private static final long serialVersionUID = -3628650019842304977L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;

	@Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private Action action;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time", updatable = false)
    @Index(name="time")
    private Date time;

    @Column(name = "message")
    private String msg;
    
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@ManyToOne
	@JoinColumn(name="user")
	@XmlTransient
	private User user;

	public OnlineRecord() {
		
	}
	
	public OnlineRecord(User user) {
		this.user = user;
	}
	
	public OnlineRecord(User user, Action action, Date time) {
		this.user = user;
		this.action = action;
		this.time = time;
	}
	
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
