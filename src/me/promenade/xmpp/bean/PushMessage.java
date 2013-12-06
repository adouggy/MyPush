package me.promenade.xmpp.bean;

/**
 * represents all necessary data for a push message
 * 
 * @author ade
 *
 */
public class PushMessage {
	private String id; // unique identity for this push message
	private String devId; // push to which device
	private String data; // push content
	
	private PushMessage(Builder builder){
		this.id = builder.id;
		this.devId = builder.devId;
		this.data = builder.data;
	}
	
	public static class Builder{
		private String id;
		private String devId;
		private String data;
		public Builder(){}
		public Builder id(String id){
			this.id = id;
			return this;
		}
		public Builder devId(String devId){
			this.devId = devId;
			return this;
		}
		public Builder data(String data){
			this.data = data;
			return this;
		}
		public PushMessage build(){
			return new PushMessage(this);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
