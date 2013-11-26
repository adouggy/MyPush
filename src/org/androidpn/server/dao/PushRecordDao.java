package org.androidpn.server.dao;

import java.util.List;

import net.synergyinfosys.xmpp.bean.PushStatus;

import org.androidpn.server.model.Message;
import org.androidpn.server.model.PushRecord;
import org.androidpn.server.model.User;

public interface PushRecordDao {
	
	public PushRecord getPushRecord(Long recordId);
	
	public List<PushRecord> getAllPushRecords(User user, Message message, PushStatus status);
	
	public List<PushRecord> getAllPushRecords();
	
//	public List<PushRecord> getPushRecordsByUser(User user);
//	
//	public List<PushRecord> getPushRecordsByMessage(Message message);
	
//	public List<PushRecord> getPushRecordsByStatus(PushStatus status);
	
	public PushRecord savePushRecord(PushRecord record);
	
	public PushRecord updatePushRecord(PushRecord record);
	
	public void removePushRecord(Long recordId);
	
	public void removeAllPushRecords(User user, Message message, PushStatus status);
	
//	public void removeAllPushRecords(Message message);
//	
//	public void removeAllPushRecords(PushStatus status);

}
