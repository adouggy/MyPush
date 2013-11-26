package net.synergyinfosys.xmpp.task;

import java.util.Date;

import org.androidpn.server.model.OnlineUserCount;
import org.androidpn.server.service.OnlineUserCountService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;


public class RecordOnlineUserCountJob{
	
	public void doRecord(){
		System.out.print("record on line user count --- ");
		ClientSession[] sessions = new ClientSession[0];
        sessions = SessionManager.getInstance().getSessions().toArray(sessions);
        int count =  sessions.length;
        System.out.println(count);
        OnlineUserCount ouc = new OnlineUserCount(new Date(), count);
        
        OnlineUserCountService serv = ServiceLocator.getOnlineUserCountService();
        serv.saveRecord(ouc);

        
	}

}
