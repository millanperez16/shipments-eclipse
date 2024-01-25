package cat.institutmarianao.shipmentsws.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.shipmentsws.model.Action;
import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.User;
import cat.institutmarianao.shipmentsws.repositories.ActionRepository;
import cat.institutmarianao.shipmentsws.repositories.ShipmentRepository;
import cat.institutmarianao.shipmentsws.repositories.UserRepository;
import cat.institutmarianao.shipmentsws.services.ActionService;
import cat.institutmarianao.shipmentsws.validation.groups.OnActionCreate;

@Service
public class ActionServiceImpl implements ActionService {

	@Autowired
	private ActionRepository actionRepository;

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	private UserRepository userRepository;

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
	@Validated(OnActionCreate.class)
	public Action save(Action action) {
		Long shId = action.getIdShipment();
		Shipment shipment = shipmentRepository.findById(shId).get();

		if (shipment == null) {
			return null;
		} else {
			action.setShipment(shipment);
		}

		String shUser = action.getPerformer().getUsername();
		User user = userRepository.findById(shUser).get();
		if (user == null) {
			return null;
		} else {
			action.setPerformer(user);
		}

		List<Action> ShTracking = shipment.getTracking();
		if (ShTracking.isEmpty() || ShTracking == null || ShTracking.size() >= 3
				|| action.getType().equals("RECEPTION")) {
			return null;
		}
		if (action.getType().equals("ASSIGNMENT")) {
			if (ShTracking.size() != 1 || !ShTracking.get(0).getType().equals("RECEPTION")) {
				return null;
			}
			ShTracking.add(0, action);
		}
		if (action.getType().equals("DELIVERY")) {
			if (ShTracking.size() != 2 || !ShTracking.get(0).getType().equals("ASSIGNMENT")) {
				return null;
			}
			ShTracking.add(0, action);
		}
		return actionRepository.saveAndFlush(action);
	}
}
