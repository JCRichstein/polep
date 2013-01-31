package polep.role;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import polep.domain.agent.EnergyProducer;
import polep.domain.market.Bid;
import polep.domain.market.BiddingStrategy;
import polep.domain.market.PowerPlantWithholdment;
import polep.repository.PowerPlantRepository;
import agentspring.role.AbstractRole;
import agentspring.role.Role;
import agentspring.role.RoleComponent;

/**
 * 			<----- BidIntoMarketRole:  ----->
    		Each EnergyProducer bids into the market by
    			Establishes the (RealVolume, MarginalCost) for each of his Power Plants by
    				RealVolume: Getting the Capacity of the Power Plant
    				MarginalCost: Get the fuel price of the power plant / get Efficiency of the Power Plant
    			Establishes a Bid (instance of Bid class) for each PowerPlant, by
    				Volume in Bid class:
    				EnergyProducer has his Strategies
    				For each Strategy:
    				generate choice probability for each strategy using Roth Erev Algorithm (PropensityofStrategy / Sum of all StrategyPropensities) [step two algorithm]
    				Select an action k according to the current choice probability distribution. [step 3 algorithm]
    			
    				Pj(t) = qj(t)/(sigma (m from 1 to N) : qm(t))
    		
    				Depending upon the selected strategy calculate the Volume for each power plant : Strategy.getPowerPlant.getWitholdment*RealCapacity
    				Price in bid class: Getting the marginal cost multiplying by (1+Price Markup of producer)
    				Establish Volume-Price pair
 * @author Kaveri3012
 *
 */
@RoleComponent
public class BidIntoMarketRole  extends AbstractRole<EnergyProducer> implements Role<EnergyProducer> {

	@Autowired
	PowerPlantRepository powerPlantRepository;

	@Transactional
	public void act(EnergyProducer producer){

		BiddingStrategy bs = producer.getChosenStrategy();
		Set<PowerPlantWithholdment> sppw = bs.getSetOfPowerPlantWithholdments();
		
		for(PowerPlantWithholdment powerPlantWithholdment : sppw){
			
			Bid bidPerPowerPlant = new Bid();
			bidPerPowerPlant.setVolume(powerPlantWithholdment.getPowerplant().getCapacity()*(1-powerPlantWithholdment.getWithholdment()));
			bidPerPowerPlant.setPrice(powerPlantWithholdment.getPowerplant().getMarginalCost()*(1+producer.getPriceMarkUp()));
			bidPerPowerPlant.setStatus(Bid.SUBMITTED);
			bidPerPowerPlant.setTime(getCurrentTick());
			bidPerPowerPlant.setBidder(producer);
			bidPerPowerPlant.persist();
			}
	}
}
