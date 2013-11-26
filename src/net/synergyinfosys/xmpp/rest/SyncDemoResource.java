package net.synergyinfosys.xmpp.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.synergyinfosys.util.algorithm.EditDistance;
import net.synergyinfosys.util.algorithm.EditOperation;
import net.synergyinfosys.xmpp.bean.PushMessage;
import net.synergyinfosys.xmpp.bean.PushTask;
import net.synergyinfosys.xmpp.bean.SyncStatus;
import net.synergyinfosys.xmpp.service.PushThreadPool;

import org.androidpn.server.dao.KeyValueBeanDao;
import org.androidpn.server.model.KeyValueBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jersey.api.spring.Autowire;

@Autowire()
@Path("/sync")
public class SyncDemoResource {
	
	private static final Log log = LogFactory.getLog(SyncDemoResource.class);
	
	@Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    
	private KeyValueBeanDao keyValueBeanDao;

	
	public KeyValueBeanDao getKeyValueBeanDao() {
		return keyValueBeanDao;
	}


	public void setKeyValueBeanDao(KeyValueBeanDao keyValueBeanDao) {
		this.keyValueBeanDao = keyValueBeanDao;
	}


	public SyncDemoResource() {
	}

	private String getRandomStr(){
		final int length = 10;
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = r.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	@Path("/deleteAll")
	@GET()
	public void hello(){
		List<KeyValueBean> list = keyValueBeanDao.getKeyValueBean();
		for( KeyValueBean b: list ){
			keyValueBeanDao.removeKeyValueBean(b.getId());
		}
	}
	
	@Path("/insert")
	@GET()
	public void insert(){
		KeyValueBean bean = new KeyValueBean();
		bean.setKey( getRandomStr() );
		bean.setValue( getRandomStr() );
		keyValueBeanDao.saveKeyValueBean(bean);
	}
	
	@Path("/update")
	@GET()
	public void update(){
		List<KeyValueBean> list = keyValueBeanDao.getKeyValueBean();
		if( list == null || list.size() == 0 )
			return;
		ArrayList<Long> idList = new ArrayList<Long>(list.size());
		for( KeyValueBean b : list ){
			idList.add( b.getId() );
		}
		
		Random r = new Random();
		int index = r.nextInt(idList.size());
		Long id =  idList.get(index);
		for( KeyValueBean b: list ){
			if( b.getId().equals(id) ){
				b.setKey(getRandomStr());
				b.setValue(getRandomStr());
				keyValueBeanDao.saveKeyValueBean(b);
				return;
			}
		}
	}
	
	@Path("/delete")
	@GET()
	public void delete(){
		List<KeyValueBean> list = keyValueBeanDao.getKeyValueBean();
		if( list == null || list.size() == 0 )
			return;
		ArrayList<Long> idList = new ArrayList<Long>(list.size());
		for( KeyValueBean b : list ){
			idList.add( b.getId() );
		}
		
		Random r = new Random();
		int index = r.nextInt(idList.size());
		Long id =  idList.get(index);
		keyValueBeanDao.removeKeyValueBean(id);
	}
	
	@Path("/sync")
	@GET()
	@Produces(MediaType.TEXT_PLAIN)
	public String sync(){
		StringBuilder response = new StringBuilder();
		List<KeyValueBean> list = keyValueBeanDao.getKeyValueBean();
		if( list == null || list.size() == 0 )
			return "no data in database";
		
		String[] dbStatus = new String[list.size()];
		System.out.println( "Database current status:" );
		for( int i=0; i<list.size(); i++ ){
			dbStatus[i] = list.get(i).toString();
			System.out.println( dbStatus[i] );
		}
		
		//把这次的同步状态保存起来
		if( SyncStatus.INSTANCE.getDbStatus() == null ){
			SyncStatus.INSTANCE.setDbStatus(dbStatus);
		}else{
			String[] lastTime = SyncStatus.INSTANCE.getDbStatus();
			SyncStatus.INSTANCE.setDbStatus(dbStatus);
			
			LinkedList<EditOperation> diff = EditDistance.getEditDistance(lastTime, dbStatus);
			if( diff!=null && diff.size()>0 ){
				String[] diffStr = new String[diff.size()];
				StringBuilder sb = new StringBuilder();
				System.out.println( "Diff:" );
				for( int i=0; i<diffStr.length; i++ ){
					diffStr[i] = diff.get(i).toString();
					System.out.println( diffStr[i] );
					sb.append(diffStr[i]);
					if( i != diffStr.length -1 ){
						sb.append("|");
					}
				}
				
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				PushMessage msg = new PushMessage.Builder()
												.id(uuid)
												.devId("")
												.data(sb.toString())
												.build();
				PushTask task = new PushTask(uuid, msg);
				String pushResult = PushThreadPool.INSTANCE.push(task);
				
				//response.append("Push result:" + pushResult + "\n");
				response.append(sb.toString());
				
				return response.toString();
			}
		}
		
		return "end..";
	}

}
