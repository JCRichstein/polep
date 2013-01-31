package polep.domain.market;

/*
 * Energy Market Agent establishes the value of lost load, which is a price cap in case the market doesn't clear. 
 * It also determines the clearing price, based on a sorting of all the bids received and an existing demand. 
 * 
 */

import org.springframework.data.neo4j.annotation.NodeEntity;

import agentspring.agent.AbstractAgent;
import agentspring.agent.Agent;
import agentspring.role.Role;
import agentspring.simulation.SimulationParameter;

@NodeEntity
public class EnergyMarket extends AbstractAgent implements Agent {

    String name;
    double clearingPrice; 
    @SimulationParameter(label = "Value of lost load", from=0, to=2000, step = 11)
    double valueOfLostLoad; 
    @SimulationParameter(label = "Demand", from=0, to=50000, step = 1000)
    double demand; 

   
    public double getClearingPrice() {
		return clearingPrice;
	}

	public double getValueOfLostLoad() {
		return valueOfLostLoad;
	}


	public void setValueOfLostLoad(double valueOfLostLoad) {
		this.valueOfLostLoad = valueOfLostLoad;
	}


	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}


    }
