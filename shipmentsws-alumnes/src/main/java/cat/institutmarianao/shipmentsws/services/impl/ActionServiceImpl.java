package cat.institutmarianao.shipmentsws.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.shipmentsws.model.Action;
import cat.institutmarianao.shipmentsws.repositories.ActionRepository;
import cat.institutmarianao.shipmentsws.services.ActionService;

@Service
public class ActionServiceImpl implements ActionService {

	@Autowired
	private ActionRepository actionRepository;
	
	@Override
	public List<Action> findAll() {
		return actionRepository.findAll();
	}

	@Override
	public List<Action> findTrackingByTicketId(Long shipmentId) {
		return actionRepository.findTrackingByShipmentId(shipmentId);
	}

	@Override
	public Action findById(Long id) {
		Optional<Action> action = actionRepository.findById(id);
		if (action.isEmpty()) {
			return null;
		}
		return action.get();
	}

	@Override
	public Action save(Action action) {
		return actionRepository.saveAndFlush(action);
	}
}
