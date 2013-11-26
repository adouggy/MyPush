package net.synergyinfosys.xmpp.task;

import net.synergyinfosys.xmpp.service.PushThreadPool;

public class ThreadPoolCheckJob{

	public void doCheck(){
		System.out.println("checking... thread pool");
		PushThreadPool.INSTANCE.checkThreadPool();
	}

}
