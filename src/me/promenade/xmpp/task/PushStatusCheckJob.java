package me.promenade.xmpp.task;

import java.util.List;

import me.promenade.xmpp.bean.PushTask;
import me.promenade.xmpp.service.PushStatusMap;

public class PushStatusCheckJob{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void doCheck(){
		System.out.println("checking...");
		int size = PushStatusMap.INSTANCE.getMapSize();
		System.out.println("current map size:" + size);
		List<PushTask> list = PushStatusMap.INSTANCE.cleanMap();
		for( PushTask pt : list ){
			System.out.print( "Removed:" );
			System.out.println( pt );
		}
	}

}
