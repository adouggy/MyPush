package me.promenade.xmpp.bean;

import java.util.UUID;

import me.promenade.xmpp.service.PushStatusMap;
import me.promenade.xmpp.service.PushThreadPool;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for( int i=0; i < 30; i++ ){
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			PushMessage msg = new PushMessage.Builder()
										.id("id_here")
//										.apiKey("api_key")
//										.title("title")
//										.message("html")
//										.uri("url")
										.devId("devId")
										.data("blah..")
										.build();
			
			PushTask task = new PushTask( uuid,  msg);
			
			String pushResult = PushThreadPool.INSTANCE.push(task);
			if( pushResult == null ){
				System.out.println( task.getThreadIndex() + " fail to join pool." );
			}
		}
		
		PushStatusMap.INSTANCE.dumpMapStatus();
		
		try {
			Thread.sleep(6000);
			PushStatusMap.INSTANCE.dumpMapStatus();
			
			Thread.sleep(6000);
			PushStatusMap.INSTANCE.dumpMapStatus();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}

}
