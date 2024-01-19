package cat.institutmarianao.shipmentsws.services;

import java.util.List;

import cat.institutmarianao.shipmentsws.model.Action;

public interface ActionService {

	List<Action> findAll();
	
	List<Action> findTrackingByTicketId(Long shipmentId);

	Action findById(Long id);

	Action save(Action action);
}
