package net.synergyinfosys.xmpp.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import net.synergyinfosys.xmpp.bean.PushTask;

/**
 * 注释丢失...
 * 
 * @author ade
 *
 */
public enum PushThreadPool {

	INSTANCE;
	
	public static final int CoreThreadCount = 4;
	public static final int ThreadCount = 10;
	public static final int KeepAliveSeconds = 3;
	public static final int QueueSize = 10;

	private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
			CoreThreadCount, 
			ThreadCount, 
			KeepAliveSeconds,
			TimeUnit.SECONDS, 
			new ArrayBlockingQueue<Runnable>(QueueSize), //working queue
			new ThreadPoolExecutor.AbortPolicy()
		);
	
	private final AtomicLong threadIndex = new AtomicLong(0);
	
	/**
	 * 
	 * 
	 * 
	 * @param task
	 * @return null if thread pool is full, which, same as task rejected
	 *     or, uuid if task added to the pool
	 */
	public String push( PushTask task ){
		try{
			long index = threadIndex.incrementAndGet();
			task.setThreadIndex(index);
			
			PushStatusMap.INSTANCE.addNewPush(task.getThreadUUID(), task);
			
			threadPool.execute(task);
			return task.getThreadUUID();
		} catch( RejectedExecutionException ree ){
			
			return null;
		}
		
	}
	
	public void checkThreadPool(){
		StringBuilder sb = new StringBuilder();
		sb.append("Core pool size:\t\t" + this.threadPool.getCorePoolSize());
		sb.append("\n");
		sb.append("Largest pool size:\t\t" + this.threadPool.getLargestPoolSize());
		sb.append("\n");
		sb.append("Pool size:\t\t" + this.threadPool.getPoolSize());
		sb.append("\n");
		sb.append("Queue size:\t\t" + this.threadPool.getQueue().size());
		sb.append("\n");
		sb.append("Active count:\t\t" + this.threadPool.getActiveCount());
		sb.append("\n");
		sb.append("Completed count:\t\t" + this.threadPool.getCompletedTaskCount());
		sb.append("\n");
		sb.append("Task count:\t\t" + this.threadPool.getTaskCount());
		sb.append("\n");
		sb.append("Keep alive time:\t\t" + this.threadPool.getKeepAliveTime(TimeUnit.SECONDS));
		sb.append("\n");
		System.out.println( sb.toString() );
	}
}
