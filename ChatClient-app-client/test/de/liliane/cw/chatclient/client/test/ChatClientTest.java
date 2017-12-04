package de.liliane.cw.chatclient.client.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.liliane.cw.chatclient.client.ServiceHandlerImpl;


public class ChatClientTest {
	
private static ServiceHandlerImpl handler ;

//private static ServiceHandlerImpl handler2 ;
	
	@BeforeClass
	public  static void init(){
		handler=ServiceHandlerImpl.getInstance();
//		handler2=ServiceHandlerImpl.getInstance();	
	}
	
//	@Test
	public void getOnlineUsers(){
		List<String> onlineUsers = handler.getOnlineUsers();
		for (String name : onlineUsers) {
			System.out.println(name);
		}
		assert(onlineUsers != null);
	}
	//@Test
	public void numberOfRegisteredUsers() {
		int register = handler.getNumberOfRegisteredUsers();
		assertTrue(register == 2);
	}
	
//	@Test
	public void numberOfOnlineUsers() {
		int online = handler.getNumberOfOnlineUsers();
		assertTrue(online == 2);
	}
	
//	@Test
	public void test() throws Exception {
		handler.register("Lil3", "lil");
		handler.register("johan", "johan");
		
		handler.login("Lil3", "lil");
//		handler2.login("johan", "johan");
		
		handler.getUserName();
		handler.changePassword("lil", "chinoise");
		//handler.delete("johan");
		
		List mylist= handler.getOnlineUsers();
		for (int i=0; i<mylist.size();i++){
			System.out.println(mylist.get(i));
		}
		//handler2.register("CHERI", "CHERI");
		
		int registerUsers = handler.getNumberOfRegisteredUsers();
		int onlineUsers = handler.getOnlineUsers().size();
		assertTrue(registerUsers==2);
		assertTrue(onlineUsers==1);
	}

}
