package polep.domain.market;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * 
 * Each bidding strategy provides each power plant owner the volume witholdment from each of its power plant.
 * 
 * 
 **/

@NodeEntity
public class BiddingStrategy {

	private double propensity;
	
	  private String strategyName; 
	
	@RelatedTo(type = "STRATEGY_ELEMENT", elementClass = PowerPlantWithholdment.class, direction=Direction.OUTGOING)
	Set<PowerPlantWithholdment> setOfPowerPlantWithholdments;

	public BiddingStrategy() {
		super();
	}


	public double getPropensity() {
		return propensity;
	}


	public void setPropensity(double propensity) {
		this.propensity = propensity;
	}


	public Set<PowerPlantWithholdment> getSetOfPowerPlantWithholdments() {
		return setOfPowerPlantWithholdments;
	}

	public void setSetOfPowerPlantWithholdments(
			Set<PowerPlantWithholdment> setOfPowerPlantWithholdments) {
		this.setOfPowerPlantWithholdments = setOfPowerPlantWithholdments;
	}
	
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	
}
