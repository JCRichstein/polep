package polep.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import polep.domain.agent.EnergyProducer;
import polep.domain.market.Bid;
import polep.domain.technology.PowerPlant;
import polep.repository.BidRepository;
import polep.repository.ClearingPointRepository;
import polep.repository.EnergyProducerRepository;
import polep.repository.PowerPlantRepository;
import agentspring.role.AbstractRole;
import agentspring.role.Role;
import agentspring.role.RoleComponent;

/**
 * @author RubenVerweij
 *PowerPlant owners dispatch power:
 *Determine volume for dispatch = get ClearingPrice from database and compare with own bids.
 *Determine dispatch order depending upon marginal cost of generation and dispatch volume
 *Store Cash in PrevCash 
 *Update cash = cash + Revenue (double)
 *Revenues  = ClearingPrice*Volume 
 */
@RoleComponent
public class DispatchPowerPlantRole  extends AbstractRole<EnergyProducer> implements Role<EnergyProducer> {
	
	
	
	
	@Autowired
	PowerPlantRepository powerPlantRepository;
	
	@Autowired
	BidRepository bidRepository;
	
	@Autowired
	EnergyProducerRepository energyProducerRepository;
	
	@Autowired
	ClearingPointRepository clearingPointRepository; 
	
	@Transactional
	public void act(EnergyProducer producer){
		
		
		Iterable<Bid> allBids = bidRepository.findAllBidsPerProducerForTime(producer, getCurrentTick());
		// gives sorted list of bids per producer? list to be defined in repository
		
		producer.setPrevCash(producer.getCash());

		double clearingPrice = clearingPointRepository.findTheOneClearingPriceForTime(getCurrentTick());
		double totalAcceptedVolume = 0;		
		// getting the revenues		
		for (Bid currentBid:allBids){
			
			
			
			double revenue = producer.getRevenue();
			double cash = producer.getCash();	
			
			totalAcceptedVolume += currentBid.getAcceptedVolume();
							
				if (currentBid.getStatus() == Bid.ACCEPTED){
					
					revenue = currentBid.getAcceptedVolume() * clearingPrice;
					producer.setCash(cash + revenue);
							
				} 


				if (currentBid.getStatus() == Bid.PARTLY_ACCEPTED){
					
					revenue = currentBid.getAcceptedVolume() * clearingPrice;
					producer.setCash(cash + revenue);
		
				}

				else {
				
										
				}
			}	
		
		// should be ordered according to marginal costs
		Iterable<PowerPlant> allPlants = powerPlantRepository.findAllPerProducerSortedByMarginalCost(producer);
		
		// physical dispatching 
		double totalCost = 0;
		double dispatchedVolume = 0;
		
		for (PowerPlant plant:allPlants){
		
			if( plant.getCapacity() <= totalAcceptedVolume){
			
			dispatchedVolume = plant.getCapacity();
			totalAcceptedVolume = totalAcceptedVolume-dispatchedVolume;
			totalCost += plant.getMarginalCost()*dispatchedVolume;
									
			}
			
			else {
			
			dispatchedVolume = totalAcceptedVolume;
			totalCost += plant.getMarginalCost()*dispatchedVolume;
			totalAcceptedVolume = totalAcceptedVolume-dispatchedVolume;
			// should be zero	
			
			}
			
		
		
			
		}	
		producer.setCash(producer.getCash() - totalCost);
	}	
	
}			
		
		
		
	

	

		
		
		

