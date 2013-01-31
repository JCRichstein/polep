package polep.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import agentspring.role.AbstractRole;
import agentspring.role.Role;
import agentspring.role.RoleComponent;
import polep.domain.agent.EnergyProducer;
import polep.domain.agent.PolepModel;
import polep.domain.market.Bid;
import polep.domain.market.ClearingPoint;
import polep.domain.market.EnergyMarket;
import polep.repository.BidRepository;
import polep.repository.ClearingPointRepository;

/** 
 * All bids that are submitted in the market are sorted by price. While the sum of all the accepted bid is less than demand bids are marked as accepted.
 * The price of the last bid is the market clearing price. If the volume of the last bid exceeds the volume that is required, then it is marked as partially accepted.
 * Remaining bids are rejected. New clearing point is set based of the clearing price and volume.
 *  
 * @author pbhagwat
 *
 */
@RoleComponent
public class ClearSpotMarketRole extends AbstractRole<EnergyMarket> implements Role<EnergyMarket> {

	@Autowired
	BidRepository bidRepository;

	@Autowired
	ClearingPointRepository clearingPointRepository;

	@Transactional
	public void act (EnergyMarket market){

		Iterable<Bid> sortedListofBidPairs = bidRepository.findAllSortedBidsByPrice(getCurrentTick());
		double demand = market.getDemand();
		double sumofSupplyBidsAccepted = 0;
		double acceptedPrice = 0;
		boolean isTheMarketCleared = false;
		
		//This epsilon is to account for rounding errors for java (only relevant for exact clearing)
		double clearingEpsilon = 0.001;
		
		if (demand == 0){
			isTheMarketCleared = true;
			acceptedPrice=0;
		}

		for (Bid currentBid:sortedListofBidPairs){
			


			if (isTheMarketCleared == false ) {


				if (demand-(sumofSupplyBidsAccepted + currentBid.getVolume()) >=  - clearingEpsilon){
						acceptedPrice = currentBid.getPrice();
						currentBid.setStatus(Bid.ACCEPTED);
						currentBid.setAcceptedVolume(currentBid.getVolume());
						sumofSupplyBidsAccepted += currentBid.getVolume();

					// logger.warn("{}", sumofSupplyBidsAccepted);
				} 


				else if (demand-(sumofSupplyBidsAccepted + currentBid.getVolume())< clearingEpsilon){

					currentBid.setStatus(Bid.PARTLY_ACCEPTED);
					currentBid.setAcceptedVolume((demand-sumofSupplyBidsAccepted));
					acceptedPrice = currentBid.getPrice();
					sumofSupplyBidsAccepted += currentBid.getAcceptedVolume();
					isTheMarketCleared = true;

					//logger.warn("Accepted" + currentBid.getAcceptedVolume());

				}

			} else{
				currentBid.setStatus(Bid.FAILED);
				currentBid.setAcceptedVolume(0);
			}

			if(demand - sumofSupplyBidsAccepted <   clearingEpsilon)
				isTheMarketCleared = true;

		}
		if (isTheMarketCleared==true){
			sumofSupplyBidsAccepted = demand;
			ClearingPoint clearingPoint = new ClearingPoint();
			clearingPoint.setPrice(acceptedPrice);
			clearingPoint.setVolume(demand);
			clearingPoint.setTime(getCurrentTick());
			clearingPoint.persist(); 
		}
		else {
			ClearingPoint clearingPoint = new ClearingPoint();
			clearingPoint.setPrice(market.getValueOfLostLoad());
			clearingPoint.setVolume(sumofSupplyBidsAccepted);
			clearingPoint.setTime(getCurrentTick());
			clearingPoint.persist();
		}
	}

}
