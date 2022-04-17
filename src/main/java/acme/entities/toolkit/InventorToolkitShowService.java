package acme.entities.toolkit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;

@Service
public class InventorToolkitShowService implements AbstractShowService<Inventor, Toolkit> {

	@Autowired
	protected InventorToolkitRepository repository;

	@Override
	public boolean authorise(final Request<Toolkit> request) {
		assert request != null;

		// Check that the inventor trying to see the toolkit is the one linked to it

		final int inventorId = request.getPrincipal().getActiveRoleId();

		final Collection<Toolkit> toolkits = this.repository.findToolkitsByInventorId(inventorId);

		final Toolkit requested = this.findOne(request);

		return toolkits.contains(requested);
	}

	@Override
	public Toolkit findOne(final Request<Toolkit> request) {
		assert request != null;

		final int id = request.getModel().getInteger("id");
		return this.repository.findToolkitById(id);
	}

	@Override
	public void unbind(final Request<Toolkit> request, final Toolkit entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "title", "description", "assamblyNotes", "url");
	}
}