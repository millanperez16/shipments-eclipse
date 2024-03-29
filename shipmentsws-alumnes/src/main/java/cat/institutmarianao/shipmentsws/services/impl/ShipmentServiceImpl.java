package cat.institutmarianao.shipmentsws.services.impl;
 
import java.util.Date;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.shipmentsws.exception.NotFoundException;
import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.Shipment.Category;
import cat.institutmarianao.shipmentsws.model.Shipment.Status;
import cat.institutmarianao.shipmentsws.model.Reception;
import cat.institutmarianao.shipmentsws.repositories.ShipmentRepository;
import cat.institutmarianao.shipmentsws.services.ShipmentService;
import cat.institutmarianao.shipmentsws.specifications.ShipmentWithStatus;
import cat.institutmarianao.shipmentsws.validation.groups.OnShipmentCreate;
import cat.institutmarianao.shipmentsws.specifications.ShipmentWithCategory;
import cat.institutmarianao.shipmentsws.specifications.ShipmentWithReceiver;
import cat.institutmarianao.shipmentsws.specifications.ShipmentWithCourier;
import cat.institutmarianao.shipmentsws.specifications.ShipmentWithDateFrom;
import cat.institutmarianao.shipmentsws.specifications.ShipmentWithDateTo;

@Validated
@Service
public class ShipmentServiceImpl implements ShipmentService {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Override
	public List<Shipment> findAll(Status status, String receivedBy, String courierAssigned, Category category,
			Date from, Date to) {
		Specification<Shipment> spec = Specification.where(new ShipmentWithStatus(status))
				.and(new ShipmentWithCategory(category).and(new ShipmentWithReceiver(receivedBy))
						.and(new ShipmentWithCourier(courierAssigned)).and(new ShipmentWithDateFrom(from)).and(new ShipmentWithDateTo(to)));
		return shipmentRepository.findAll(spec);
	}
	
	public List<Shipment> findAllPending(String receivedBy, String courierAssigned, Category category, Date from, Date to){
		Status pendingStatus = Status.PENDING;
		Specification<Shipment> spec = Specification.where(new ShipmentWithStatus(pendingStatus))
				.and(new ShipmentWithCategory(category).and(new ShipmentWithReceiver(receivedBy))
						.and(new ShipmentWithCourier(courierAssigned)).and(new ShipmentWithDateFrom(from)).and(new ShipmentWithDateTo(to)));
		return shipmentRepository.findAll(spec);
	}

	@Override
	public List<Shipment> findAllInProcess(String receivedBy, String courierAssigned, Category category, Date from,
			Date to) {
		Status inProcessStatus = Status.IN_PROCESS;
		Specification<Shipment> spec = Specification.where(new ShipmentWithStatus(inProcessStatus))
				.and(new ShipmentWithCategory(category).and(new ShipmentWithReceiver(receivedBy))
						.and(new ShipmentWithCourier(courierAssigned)).and(new ShipmentWithDateFrom(from)).and(new ShipmentWithDateTo(to)));
		return shipmentRepository.findAll(spec);
	}

	@Override
	public Shipment findById(Long id) {
		return shipmentRepository.findById(id).orElseThrow(NotFoundException::new);
	}

	@Override
	@Validated(OnShipmentCreate.class)
	public Shipment save(@Valid Shipment shipment) {
		if (shipment.getTracking().size() != 1) {
			return null; // lanzar excepción
		}
		if (shipment.getTracking().get(0) instanceof Reception) {
			shipment.getTracking().get(0).setShipment(shipment);
		} else {
			return null; // lanzar excepción
		}
		return shipmentRepository.saveAndFlush(shipment);
		
	}

	@Override
	public void deleteById(@NotNull Long id) {
		shipmentRepository.deleteById(id);
	}
	
}
