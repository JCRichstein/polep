package polep.domain.market;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.transaction.annotation.Transactional;

import polep.domain.market.EnergyMarket;

/**
 * The clearing point consists of the volume of supply capacity accepted 
 * and the market clearing price for a given demand at the current tick.
 * 
 * @pcbhagwat
 * 
 */

@NodeEntity
public class ClearingPoint {

	@RelatedTo(type = "MARKET_POINT", elementClass = EnergyMarket.class, direction = Direction.OUTGOING)
	EnergyMarket abstractMarket;

	private double price;
	private double volume;
	private long time;

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public EnergyMarket getAbstractMarket() {
		return abstractMarket;
	}
	public void setAbstractMarket(EnergyMarket abstractMarket) {
		this.abstractMarket = abstractMarket;
	}
	
	public String toString() 
	
	{
        return " market: " + abstractMarket + ", price " + price + ", volume " + volume + ", time " + time;
    }

}
