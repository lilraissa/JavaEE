package de.liliane.cw.carfinancing.server.beans;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import de.liliane.cw.carfinancing.server.beans.interfaces.CarFinancingLocal;
import de.liliane.cw.carfinancing.server.beans.interfaces.CarFinancingRemote;

@Stateless
public class CarFinancingBean implements CarFinancingLocal, CarFinancingRemote {
	
	@Resource(name="zinsensatz")
	private double interestRate;

	@Override
	public double computeNetLoanAmount(double price, double firstInstalment) {
		
		return price - firstInstalment;
	}

	@Override
	public double computeGrossLoanAmount(double price, double firstInstalment, int paymentTerm) {
		//interestRate=0.01;
		double  nettoDarlehenBetrag=computeNetLoanAmount(price, firstInstalment);
		double schuldenStand = nettoDarlehenBetrag;
		double monatTilgung=nettoDarlehenBetrag/paymentTerm;
		double zinsen=0.0;
		double gesamtZinsen=0.0;
		
		for(int i=1;i<=paymentTerm;i++)
		{
			zinsen=(schuldenStand*interestRate)/paymentTerm;
			schuldenStand=schuldenStand-monatTilgung;
			gesamtZinsen=gesamtZinsen+zinsen;
			
		}
		
		return gesamtZinsen;
	}

	@Override
	public double computeMonthlyPayment(double price, double firstInstalment, int paymentTerm) {
		
		return (computeNetLoanAmount(price, firstInstalment)+computeGrossLoanAmount(price, firstInstalment, paymentTerm))/paymentTerm;
		
	}

	@Override
	public double getInterestRate() {
		
		return interestRate;
	}

}
