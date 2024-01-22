package cat.institutmarianao.shipmentsws.services;

import java.util.Date;
import java.util.List;

import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.Shipment.Category;
import cat.institutmarianao.shipmentsws.model.Shipment.Status;
import jakarta.validation.Valid;

public interface ShipmentService {
	List<Shipment> findAll(Status status, String receivedBy, String courierAssigned, Category category, Date from, Date to);
	
	Shipment findById(Long id);

	List<Shipment> findAllPending(String receivedBy, String courierAssigned, Category category, Date from, Date to);
	
	List<Shipment> findAllInProcess(String receivedBy, String courierAssigned, Category category, Date from, Date to);
	
	Shipment save(@Valid Shipment shipment);
}
