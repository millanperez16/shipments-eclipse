package cat.institutmarianao.shipmentsws.specifications;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.shipmentsws.model.Action;
import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.Action.Type;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ShipmentWithDateTo implements Specification<Shipment> {

	private static final long serialVersionUID = 1L;
	private Date to;

	public ShipmentWithDateTo(Date to) {
		this.to = to;
	}

	@Override
	public Predicate toPredicate(Root<Shipment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (to == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
		}

		Join<Shipment, Action> shipmentsActions = root.join("tracking");

		return criteriaBuilder.and(criteriaBuilder.equal(shipmentsActions.get("type"), Type.RECEPTION),
				criteriaBuilder.lessThanOrEqualTo(shipmentsActions.get("date"), to));
	}

}
