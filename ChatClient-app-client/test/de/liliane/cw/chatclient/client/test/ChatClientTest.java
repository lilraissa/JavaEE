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
		//handler2=ServiceHandlerImpl.getInstance();
		
	}


	@Test
	public void test() throws Exception {
		handler.register("Lil3", "lil");
		handler.register("johan", "johan");
		//handler2.register("CHERI", "CHERI");
		handler.login("Lil3", "lil");
		handler.login("johan", "johan");
		handler.getUserName();
		handler.changePassword("lil", "chinoise");
		handler.delete("johan");
		handler.logout();
		
		List mylist= handler.getOnlineUsers();
		for (int i=0; i<mylist.size();i++){
			System.out.println(mylist.get(i));
		}
		//handler2.register("CHERI", "CHERI");
		
		assertTrue(handler.getNumberOfRegisteredUsers()==2);
		assertTrue(handler.getOnlineUsers().size()==1);
	}

}
