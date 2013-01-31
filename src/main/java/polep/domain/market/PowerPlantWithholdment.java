package polep.domain.market;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import polep.domain.technology.PowerPlant;

/**
 * The witholdment is the volume of capacity that can be withheld from a certain power plant.
 * 
 * */

@NodeEntity
public class PowerPlantWithholdment {

	private double withholdment;
	
	@RelatedTo(type = "STRATEGY_POWERPLANT", elementClass=PowerPlant.class, direction=Direction.OUTGOING)
	private PowerPlant powerplant;
	
	public double getWithholdment() {
		return withholdment;
	}
	
	public void setWithholdment(double witholdment) {
		this.withholdment = witholdment;
	}
	
	public PowerPlant getPowerplant() {
		return powerplant;
	}

	public void setPowerplant(PowerPlant powerplant) {
		this.powerplant = powerplant;
	}
		
}
