package de.liliane.cw.chatclient.client.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.liliane.cw.chatclient.client.ServiceHandlerImpl;

public class TimerStatistic {
	private static ServiceHandlerImpl handler ;

	//private static ServiceHandlerImpl handler2 ;
		
		@BeforeClass
		public  static void init(){
			handler=ServiceHandlerImpl.getInstance();
			//handler2=ServiceHandlerImpl.getInstance();
		}


		@Test
		public void test() throws Exception {
			// Register
			handler.register("Lil3", "lil");
			handler.register("johan", "johan");
			
			// Login
			handler.login("Lil3", "lil");
			handler.login("johan", "johan");
			
			handler.sendChatMessage("Hi ich bin da!");
			
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
