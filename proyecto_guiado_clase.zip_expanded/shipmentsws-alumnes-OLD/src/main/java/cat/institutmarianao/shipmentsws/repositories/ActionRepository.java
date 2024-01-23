package cat.institutmarianao.shipmentsws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cat.institutmarianao.shipmentsws.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long>, JpaSpecificationExecutor<Action> {

	List<Action> findTrackingByShipmentId(Long shipmentId);
	// List<Action> findByShipmentIdOrderByDateDesc(Long shipmentId);

	// @Query(value="select max a.tracking from action a")

}
