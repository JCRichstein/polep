package polep.role;


import java.util.ArrayList;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;



import agentspring.role.AbstractRole;
import agentspring.role.Role;
import agentspring.role.RoleComponent;
import polep.domain.agent.EnergyProducer;
import polep.domain.agent.Regulator;
import polep.repository.BidRepository;
import polep.repository.EnergyProducerRepository;
import polep.repository.PowerPlantRepository;
import polep.util.BiasedCoin;

/*<--- RegulatorRole: Prad ----->
Regulator controls Power plant owner:
	Throw a weighted coin (probability to control) to select owners for control (this is to simulate imperfect knowledge of the regulator)
	Compare Bid volume with installed capacity
	If Bid volume is less than installed capacity and greater than accepted withholdment = Fine
	Fine is property of regulator
	Update Cash = Cash - Fine */

@RoleComponent
public class RegulatorRole extends AbstractRole<Regulator> implements Role<Regulator> {


	@Autowired
	EnergyProducerRepository energyProducerRepository;

	@Autowired
	PowerPlantRepository plantRepository;

	@Autowired
	BidRepository bidRepository;


	@Transactional
	public void act(Regulator regulator){


		Iterable<EnergyProducer> listofPowerPlantOwners = energyProducerRepository.findAll();

		double fine = regulator.getFine();
		double capacity = 0;
		double probabilitytoCheck = regulator.getProbabilityToCheck();
		double acceptableWitholdment = regulator.getAcceptableWitholdment();

		for (EnergyProducer currentProducer:listofPowerPlantOwners)
		{
			// creates a coin

			BiasedCoin coin = new BiasedCoin(); 

			// Simplified, so that the checking is random every time. Thus there is no bias

			// coin.flip(currentProducer.getProbability()); 

			currentProducer.setFineLastPaid(0);

			coin.flip(0); 
			double result = coin.getSide(); // flips coin gets value

			if (result <= probabilitytoCheck){

				// Gets actual total capacity from the repository considering complete information (repository query may need correction)

				capacity = plantRepository.calculateCapacityOfPowerPlantsofProducer(currentProducer);

				// Compares capacity with total of all bids of the producer for total tick. If it is < then fines the producer.			

				double biddedCapacity = bidRepository.calculateTotalSupplyofProducerForTime(currentProducer, getCurrentTick());
				
				if (((1-acceptableWitholdment)*capacity) >= biddedCapacity){
					double cash = currentProducer.getCash() - fine;
					currentProducer.setCash(cash);
					double regulatorsCash = regulator.getCash()+fine;
					regulator.setCash(regulatorsCash);
					
					currentProducer.setFineLastPaid(fine);

					// Changes probability value so that next time the probability of checking is higher. Currently set to zero					

					// double probability = currentProducer.getProbability() + 0;
					// currentProducer.setProbability(probability);

				}
				
			}
		}
	}
}


