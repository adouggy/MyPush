package org.androidpn.server.service;

import java.util.List;

import net.synergyinfosys.xmpp.bean.PushStatus;

import org.androidpn.server.model.Message;
import org.androidpn.server.model.PushRecord;
import org.androidpn.server.model.User;

public interface PushRecordService {
	
	public PushRecord getPushRecord(Long recordId);
	
	public List<PushRecord> getAllPushRecords(User user, Message message, PushStatus status);
	
	public List<PushRecord> getAllPushRecords();
	
	public PushRecord updatePushRecord(PushRecord record);
	
	public void removePushRecord(Long recordId);
	
	public void removeAllPushRecords(User user, Message message, PushStatus status);
	
	public PushRecord addPushRecord(long userId, long messageId, PushStatus status) throws Exception ;
	
}
