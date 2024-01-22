package cat.institutmarianao.shipmentsws.specifications;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.Shipment.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ShipmentWithStatus implements Specification<Shipment>{
	private static final long serialVersionUID = 1L;
	private Status status;

	public ShipmentWithStatus(Status status) {
		this.status = status;
	}

	@Override
	public Predicate toPredicate(Root<Shipment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (status == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
		}
		return criteriaBuilder.equal(root.get("status"), status);
	}

}
