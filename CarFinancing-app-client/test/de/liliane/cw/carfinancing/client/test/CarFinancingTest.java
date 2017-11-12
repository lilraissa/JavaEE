package de.liliane.cw.carfinancing.client.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fh_dortmund.inf.cw.car_financing.client.shared.CarFinancingHandler;
import de.liliane.cw.carfinancing.client.ServiceHandlerImpl;

public class CarFinancingTest {
	
	private static ServiceHandlerImpl handler ;
	
	@BeforeClass
	public  static void init(){
		handler=ServiceHandlerImpl.getInstance();
		
	}

	@Test
	public void computeNetLoanAmountTest() {
		double d=handler.computeNetLoanAmount(14500,2500);
		System.out.println(d);
		assertTrue(d==12000);
	}
	@Test
	public void computeGrossLoanAmountTest() {
		double d=handler.computeGrossLoanAmount(14500, 2500, 12);
		System.out.println(d);
		assertTrue(d==65);
	}
	@Test
	public void computeMonthlyPaymentTest() {
		double d=handler.computeMonthlyPayment(14500, 2500, 12);
		System.out.println(d);
		assertTrue(Math.round(d)==1005);
	}

}
