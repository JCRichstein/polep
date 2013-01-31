package polep.role;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;

import polep.domain.agent.EnergyProducer;
import polep.domain.market.Bid;
import polep.domain.market.BiddingStrategy;
import polep.domain.market.PowerPlantWithholdment;
import polep.domain.technology.PowerPlant;
import polep.repository.PowerPlantRepository;
import agentspring.role.AbstractRole;
import agentspring.role.Role;
import agentspring.role.RoleComponent;

/**
 * 
 * Depending upon the selected strategy calculate the Volume for each power plant : Strategy.getPowerPlant.getWitholdment*RealCapacity
 * Price in bid class: Getting the marginal cost multiplying by (1+Price Markup of producer)
 * Establish Volume-Price pair
 * 
 * Pj(t) = qj(t)/(sigma (m from 1 to N) : qm(t))
 * 
 * @author Kaveri
 *
 */
@RoleComponent
public class PickStrategyRole  extends AbstractRole<EnergyProducer> implements Role<EnergyProducer>{ 

	Set<BiddingStrategy> strategySet; //declaration of a new strategy set
	
	
	@Transactional
	public void act(EnergyProducer producer){
		int k;
		
		strategySet = producer.getBiddingStrategySet(); //this means that it has to be loaded as part of the scenario for the energy producer already. 
		BiddingStrategy[] strategyArray =  new BiddingStrategy[strategySet.size()];
	
		int i = 0;
		for(BiddingStrategy biddingStrategy : strategySet){
			strategyArray[i] = biddingStrategy;
					i++;
		}
		double[] propensityArray = new double[strategyArray.length];
		
		
		for(i = 0; i<strategyArray.length; i++){
			propensityArray[i] = strategyArray[i].getPropensity();
		}

		EmpiricalWalker em = new EmpiricalWalker(propensityArray, Empirical.NO_INTERPOLATION, EmpiricalWalker.makeDefaultGenerator());
		
		k=em.nextInt();
		
		BiddingStrategy chosenStrategy = strategyArray[k];
		
		producer.setChosenStrategy(chosenStrategy);
		
				

	}
}
