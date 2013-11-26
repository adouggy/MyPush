package net.synergyinfosys.xmpp.bean;

/**
 * represents all necessary data for a push message
 * 
 * @author ade
 *
 */
public class PushMessage {
	private String id;
//	private String apiKey;
//	private String title;
//	private String message;
//	private String uri;
	private String devId;
	private String data;
	
	private PushMessage(Builder builder){
		this.id = builder.id;
//		this.apiKey = builder.apiKey;
//		this.title = builder.title;
//		this.message = builder.message;
//		this.uri = builder.uri;
		this.devId = builder.devId;
		this.data = builder.data;
	}
	
	public static class Builder{
		private String id;
//		private String apiKey;
//		private String title;
//		private String message;
//		private String uri;
		private String devId;
		private String data;
		public Builder(){}
		public Builder id(String id){
			this.id = id;
			return this;
		}
//		public Builder apiKey(String apiKey){
//			this.apiKey = apiKey;
//			return this;
//		}
//		public Builder title(String title){
//			this.title = title;
//			return this;
//		}
//		public Builder message(String message){
//			this.message = message;
//			return this;
//		}
//		public Builder uri(String uri){
//			this.uri = uri;
//			return this;
//		}
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

//	public String getApiKey() {
//		return apiKey;
//	}
//
//	public void setApiKey(String apiKey) {
//		this.apiKey = apiKey;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public String getUri() {
//		return uri;
//	}
//
//	public void setUri(String uri) {
//		this.uri = uri;
//	}

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
