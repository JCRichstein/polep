package polep.domain.technology;

import org.springframework.data.neo4j.annotation.NodeEntity;

import agentspring.simulation.SimulationParameter;

/*
* Each type of fuel has a property price (Euro/Mwh)
*
*/

@NodeEntity
public class Fuel {
	
	@SimulationParameter(label = "FUEL_PRICE", from = 0, to = 500)
	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	

}
