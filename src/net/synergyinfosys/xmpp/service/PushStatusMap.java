package net.synergyinfosys.xmpp.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.synergyinfosys.xmpp.bean.PushStatus;
import net.synergyinfosys.xmpp.bean.PushTask;

/**
 * 记录推送的状态，
 * 暂时记录在内存中，
 * 未来如果写数据库也是要内存数据库或者异步写文件IO的。
 * 
 * 新来的push都放在这里，无论队列是否满了。如果被threadPool拒绝的task会在这里处于waiting状态
 * TODO 做一个守护线程，扫描map，如果发现之前被丢弃过的，再添加到threadPool一次
 * TODO 做一个守护线程，扫描map，如果发现task已经完成，进行archive或者删除
 * 
 * @author ade
 *
 */
public enum PushStatusMap {

	INSTANCE;
	
	private ConcurrentHashMap<String, PushTask> statusMap 
		= new ConcurrentHashMap<String, PushTask>();
	
	public void dumpMapStatus(){
		System.out.println( this.toString() );
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("Push Status Map @" + this.hashCode() + "\n");
		
		Iterator<String> keys = statusMap.keySet().iterator();
		while( keys.hasNext() ){
			String key = keys.next();
			sb.append( key );
			sb.append( "->" );
			sb.append( statusMap.get(key) );
			sb.append( "\n" );
		}
		
		return sb.toString();
	}
	
	/**
	 * @param uuid
	 * @return null if create new successful,
	 * 	 or previous value associated with key
	 */
	public PushTask addNewPush(String uuid, PushTask task){
		return statusMap.putIfAbsent(uuid, task);
	}
	
	public boolean changeStatus( String uuid, PushStatus status ){
		PushTask task = statusMap.get(uuid);
		if( task != null ){
			synchronized( statusMap ){
				task.setStatus(status);
			}
			return true;
		}
		return false;
	}
	
	public synchronized PushStatus getStatus(String uuid){
		PushTask task =  statusMap.get(uuid);
		if( task != null){
			return task.getStatus();
		}
		return null;
	}

	public PushTask getPushTask(String uuid){
		return statusMap.get(uuid);
	}
	
	public int getMapSize(){
		return statusMap.size();
	}
	
	public List<PushTask> cleanMap(){
		ArrayList<PushTask> list = new ArrayList<PushTask>();
		long curr = System.currentTimeMillis();
		long timeInterval = 1000 * 60 * 10;
		
		Iterator<String> keyIter = this.statusMap.keySet().iterator();
		while( keyIter.hasNext() ){
			String uuid = keyIter.next();
			PushTask pt = this.statusMap.get(uuid);
			if(  curr - pt.getTimeStamp() > timeInterval ){
				list.add(pt);
			}
		}
		
		for( PushTask pt: list ){
			this.statusMap.remove(pt.getThreadUUID());
		}
		
		return list;
	}
}
