package polep.domain.market;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import polep.domain.agent.EnergyProducer;


/* A bid has properties price, volume, status, accepted volume and time.
 * The role of the bid is to etablish a volume-price pair for every power plant owned by an energy producer.
 * The volume price pair is established based on the bidding strategy returned.  
 */

@NodeEntity
public class Bid {

	public static int FAILED = -1;
	public static int NOT_SUBMITTED = 0;
	public static int SUBMITTED = 1;
	public static int PARTLY_ACCEPTED = 2;
	public static int ACCEPTED = 3;

	@RelatedTo(type = "BIDDER", elementClass = EnergyProducer.class, direction = Direction.INCOMING)
	private EnergyProducer bidder;

	private double volume;
	private double acceptedVolume;
	private double price;
	private long time;
	private int status;



	public EnergyProducer getBidder() {
		return bidder;
	}

	public void setBidder(EnergyProducer agent) {
		this.bidder = agent;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	 public double getAcceptedVolume() {
		return acceptedVolume;
	}

	public void setAcceptedVolume(double acceptedVolume) {
		this.acceptedVolume = acceptedVolume;
	}

	@Override
	 public String toString() {
		 return  " price: " + getPrice() + " amount: " + getVolume();
	 }
}



