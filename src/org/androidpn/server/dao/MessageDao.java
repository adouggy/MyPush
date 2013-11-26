package org.androidpn.server.dao;

import java.util.Date;
import java.util.List;

import org.androidpn.server.model.Message;
import org.springframework.dao.DataAccessException;

/** 
 * Message DAO interface. 
 *
 */
public interface MessageDao {

	public Message getMessage(Long id)  throws Exception ;
	
	public Message saveMessage(Message message);
	
	public void removeMessage(Long id) throws DataAccessException, Exception;
	
	public List<Message> getMessagesByTime(Date startTime, Date endTime);
	
	public List<Message> getMessages();
	
}
