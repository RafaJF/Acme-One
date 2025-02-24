package acme.features.any.toolkit;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.item.Item;
import acme.entities.moneyExchange.MoneyExchange;
import acme.entities.quantity.Quantity;
import acme.entities.toolkit.Toolkit;
import acme.features.authenticated.moneyExchange.AuthenticatedMoneyExchangePerformService;
import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.roles.Any;
import acme.framework.services.AbstractListService;

@Service
public class AnyToolkitItemsService implements AbstractListService<Any,Item> {
	@Autowired
	protected AnyToolkitRepository repository;
	@Autowired
	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;	
	
	@Override
	public boolean authorise(final Request<Item> request) {
		
		assert request !=null;
		int toolkitId;
		toolkitId = request.getModel().getInteger("id");
		final Toolkit t = this.repository.findToolkitById(toolkitId);
		return t.isPublished();
			
	}
	@Override
	public Collection<Item> findMany(final Request<Item> request) {
		
		final Collection<Item> result = new HashSet<>();
		int toolkitId;
		toolkitId= request.getModel().getInteger("id");
		
		final Collection<Quantity> quantities = this.repository.findQuantitiesByToolkitId(toolkitId);
		
		for(final Quantity q: quantities) {
			final int quantityId = q.getId();
			final Item i = this.repository.findItemByQuantityId(quantityId);
			result.add(i);
		}
		
		return result;
	}

		
		@Override
		public void unbind(final Request<Item> request, final Item entity, final Model model) {
			assert request != null; 
			assert entity != null; 
			assert model != null;
			
			final Money newRetailPrice = this.moneyExchangeItem(entity);
			model.setAttribute("newRetailPrice", newRetailPrice);
			
			request.unbind(entity, model, "name", "code", "retailPrice");
		}
		
		//Método auxiliar cambio de divisa
		public Money moneyExchangeItem(final Item i) {
			final String itemCurrency = i.getRetailPrice().getCurrency();
			
			final AuthenticatedMoneyExchangePerformService moneyExchange = new AuthenticatedMoneyExchangePerformService();
			final String systemCurrency = this.systemConfigRepository.findSystemConfiguration().getSystemCurrency();
			final Double conversionAmount;
				
			if(!systemCurrency.equals(itemCurrency)) {
				MoneyExchange conversion;
				conversion = moneyExchange.computeMoneyExchange(i.getRetailPrice(), systemCurrency);
				conversionAmount = conversion.getTarget().getAmount();	
			}
			else {
				conversionAmount = i.getRetailPrice().getAmount();
			}
				
			final Money newBudget = new Money();
			newBudget.setAmount(conversionAmount);
			newBudget.setCurrency(systemCurrency);
				
			return newBudget;
		}


	

}
