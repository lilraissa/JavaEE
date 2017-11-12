package de.liliane.cw.carfinancing.client;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.car_financing.client.shared.CarFinancingHandler;
import de.fh_dortmund.inf.cw.car_financing.client.shared.ServiceHandler;
import de.liliane.cw.carfinancing.server.beans.interfaces.CarFinancing;
import de.liliane.cw.carfinancing.server.beans.interfaces.CarFinancingRemote;

public class ServiceHandlerImpl extends ServiceHandler implements CarFinancingHandler {

	private static ServiceHandlerImpl instance;
	private Context ctx;
	private CarFinancingRemote carFinancing;

	private ServiceHandlerImpl() {
		
		try {
			ctx=new InitialContext();
			carFinancing=(CarFinancingRemote)ctx.lookup("java:global/CarFinancing-ear/CarFinancing-ejb/CarFinancingBean!de.liliane.cw.carfinancing.server.beans.interfaces.CarFinancingRemote");
		} catch (NamingException e) {
			
			e.printStackTrace();
		}

	}

	public static ServiceHandlerImpl getInstance() {
		if (instance == null){
			instance = new ServiceHandlerImpl();
		}
		return instance;
	}

	@Override
	public double computeGrossLoanAmount(double arg0, double arg1, int arg2) {
		// TODO Auto-generated method stub
		return carFinancing.computeGrossLoanAmount(arg0, arg1, arg2);
	}

	@Override
	public double computeMonthlyPayment(double arg0, double arg1, int arg2) {
		// TODO Auto-generated method stub
		return carFinancing.computeMonthlyPayment(arg0, arg1, arg2);
	}

	@Override
	public double computeNetLoanAmount(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return carFinancing.computeNetLoanAmount(arg0, arg1);
	}

	@Override
	public double getInterestRate() {
		// TODO Auto-generated method stub
		return carFinancing.getInterestRate();
	}

}
