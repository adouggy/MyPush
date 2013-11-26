package net.synergyinfosys.xmpp.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidpn.server.xmpp.push.NotificationManager;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

/**
 * Task
 * 
 * @author ade
 *
 */
public class PushTask implements Runnable, Serializable {
	private static final long serialVersionUID = 0;
	
	private SessionManager sessionManager = SessionManager.getInstance();;
	
	//unique ID
	private String threadUUID = null;
	//just for reference..
	private long threadIndex = -1;
	//task status
	private PushStatus status = PushStatus.Waiting;
	//push message
	private PushMessage msg;
	
	private long timeStamp = -1;
	
	public PushTask(String uuid, PushMessage msg) {
		this.threadUUID = uuid;
		this.msg = msg;
		this.timeStamp = System.currentTimeMillis();
	}
	
	@Override
	public String toString(){
		return String.format("Pushtask> ThreadId:%3d,UUID:%s,Status:%s,timeStamp:%s", this.threadIndex, this.threadUUID, this.status, this.getTimeStampStr() );
	}
	
	private String pushMsg(PushMessage msg){
		IQ notificationIQ = new IQ();
        Element notification = DocumentHelper.createElement(QName.get("notification", NotificationManager.NOTIFICATION_NAMESPACE));
        notification.addElement("id").setText(msg.getId());
        notification.addElement("apiKey").setText("1234567890");
//        notification.addElement("title").setText(msg.getTitle());
//        notification.addElement("message").setText(msg.getMessage());
//        notification.addElement("uri").setText(msg.getUri());
        notification.addElement("devId").setText(msg.getDevId());
        notification.addElement("data").setText(msg.getData());
        
        notificationIQ.setType(IQ.Type.set);
        notificationIQ.setChildElement(notification);
        
        System.out.println("Pushed XML" + notificationIQ.toXML());
        
        boolean online = false;
        for (ClientSession session : sessionManager.getSessions()) {
            if (session.getPresence().isAvailable() && session.getAddress().toFullJID().contains(msg.getDevId())) {
            	System.out.println( ">>>>To JID:" + session.getAddress() );
            	online = true;
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ);
            }
        }
        
        if( !online ){
        	return "offline";
        }
        
        return notificationIQ.toXML();
	}

	public void run() {
		System.out.println("starting thread@" + threadIndex + " uuid:" + threadUUID);
		this.status = PushStatus.Sending;
//		try {
//			Thread.sleep(consumeTaskSleepTime);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String xml = pushMsg(msg);
		System.out.println("thread@" + threadIndex + "--->" + xml);
		
		System.out.println("End .. thread@" + threadIndex);
		if( xml.compareTo("offline") == 0 ){
			this.status = PushStatus.Offline;
		}else{
			this.status = PushStatus.Sent;
		}	
	}
	
	public static class Builder{
		
	}
	
	public String getThreadUUID() {
		return threadUUID;
	}

	public long getThreadIndex() {
		return threadIndex;
	}

	public void setThreadIndex(long threadIndex) {
		this.threadIndex = threadIndex;
	}

	public PushStatus getStatus() {
		return status;
	}

	public void setStatus(PushStatus status) {
		this.status = status;
	}
	
	public long getTimeStamp(){
		return this.timeStamp;
	}
	
	public String getTimeStampStr(){
		return  new SimpleDateFormat().format(new Date(this.timeStamp));
	}
}