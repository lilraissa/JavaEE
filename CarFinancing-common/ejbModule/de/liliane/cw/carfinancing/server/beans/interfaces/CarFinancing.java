package de.liliane.cw.carfinancing.server.beans.interfaces;

public interface CarFinancing {
	
	  public  double computeNetLoanAmount(double paramDouble1, double paramDouble2);
	  
	  public  double computeGrossLoanAmount(double paramDouble1, double paramDouble2, int paramInt);
	  
	  public  double computeMonthlyPayment(double paramDouble1, double paramDouble2, int paramInt);
	  
	  public  double getInterestRate();

}
