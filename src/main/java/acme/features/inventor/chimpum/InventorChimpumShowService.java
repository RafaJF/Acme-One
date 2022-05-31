package acme.features.inventor.chimpum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.entities.moneyExchange.MoneyExchange;
import acme.features.authenticated.moneyExchange.AuthenticatedMoneyExchangePerformService;
import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;

@Service
public class InventorChimpumShowService implements AbstractShowService<Inventor, Chimpum> {

	@Autowired
	protected InventorChimpumRepository repository;
	@Autowired
	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;

	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;

		boolean result;
		Chimpum chimpum;
		int chimpumId;

		chimpumId = request.getModel().getInteger("id");
		chimpum = this.repository.findOneChimpumById(chimpumId);
		result = chimpum.getItem().getInventor().getId() == request.getPrincipal().getActiveRoleId();

		return result;
	}

	@Override
	public Chimpum findOne(final Request<Chimpum> request) {
		assert request != null;
		Chimpum result;
		int chimpumId;

		chimpumId = request.getModel().getInteger("id");
		result = this.repository.findOneChimpumById(chimpumId);

		return result;
	}

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		final Money newBudget = this.moneyExchangeChimpum(entity);
		model.setAttribute("newBudget", newBudget);
		
		model.setAttribute("item", entity.getItem().getName());

		request.unbind(entity, model, "code", "title", "description", "budget", "creationMoment", "startDate", "endDate", "moreInfo");
	}
	
	//Método auxiliar cambio de divisa

	public Money moneyExchangeChimpum(final Chimpum c) {

		final String chimpumCurrency = c.getBudget().getCurrency();
		
		final AuthenticatedMoneyExchangePerformService moneyExchange = new AuthenticatedMoneyExchangePerformService();
		final String systemCurrency = this.systemConfigRepository.findSystemConfiguration().getSystemCurrency();
		final Double conversionAmount;

		if(!systemCurrency.equals(chimpumCurrency)) {
			MoneyExchange conversion;
			conversion = moneyExchange.computeMoneyExchange(c.getBudget(), systemCurrency);
			conversionAmount = conversion.getTarget().getAmount();	
		}
		else {
			conversionAmount = c.getBudget().getAmount();
		}

		final Money newBudget = new Money();
		newBudget.setAmount(conversionAmount);
		newBudget.setCurrency(systemCurrency);
				
		return newBudget;
	}

}