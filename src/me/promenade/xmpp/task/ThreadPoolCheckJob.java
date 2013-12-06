package me.promenade.xmpp.task;

import me.promenade.xmpp.service.PushThreadPool;

public class ThreadPoolCheckJob{

	public void doCheck(){
		System.out.println("checking... thread pool");
		PushThreadPool.INSTANCE.checkThreadPool();
	}

}
