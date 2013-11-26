package net.synergyinfosys.xmpp.bean;

/**
 * 简单的保存一下数据库同步的状态
 * 简单的用synchronized保证线程安全，没有100%线程安全，慎重!
 * 
 * @author ade
 *
 */
public enum SyncStatus {
	INSTANCE;
	
	private String[] dbStatus;

	public synchronized String[] getDbStatus() {
		return dbStatus;
	}

	public synchronized void setDbStatus(String[] dbStatus) {
		this.dbStatus = dbStatus;
	}
	
}
