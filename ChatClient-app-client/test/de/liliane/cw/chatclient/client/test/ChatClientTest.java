package de.liliane.cw.chatclient.client.test;

import static org.junit.Assert.*;

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
		//handler2.register("CHERI", "CHERI");
		assertTrue(handler.getNumberOfRegisteredUsers()==1);
	}

}
