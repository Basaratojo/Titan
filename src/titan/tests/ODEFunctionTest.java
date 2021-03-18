package titan.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import titan.RateInterface;
import titan.impl.ODEFunction;
import titan.impl.Planet;

public class ODEFunctionTest {

	@Test
	public void testCall() {
		
		ODEFunction odeFunction = new ODEFunction();
		

		System.out.println("Eath Position  "+Planet.EARTH.getLastPosition());
		//after 1 hours
		for (int time = 0; time < 3600*24*365.25; time++) {
		odeFunction.call(time, Planet.EARTH);
		}
		
		System.out.println("Eath Position  "+Planet.EARTH.getLastPosition());
		
		
	}

}
