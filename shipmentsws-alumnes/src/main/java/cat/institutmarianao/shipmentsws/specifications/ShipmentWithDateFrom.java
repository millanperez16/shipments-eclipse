package cat.institutmarianao.shipmentsws.specifications;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.shipmentsws.model.Action;
import cat.institutmarianao.shipmentsws.model.Action.Type;
import cat.institutmarianao.shipmentsws.model.Shipment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ShipmentWithDateFrom implements Specification<Shipment> {

	private static final long serialVersionUID = 1L;
	private Date from;

	public ShipmentWithDateFrom(Date from) {
		this.from = from;
	}

	@Override
	public Predicate toPredicate(Root<Shipment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (from == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
		}

		Join<Shipment, Action> shipmentsActions = root.join("tracking");

		return criteriaBuilder.and(criteriaBuilder.equal(shipmentsActions.get("type"), Type.RECEPTION),
				criteriaBuilder.greaterThanOrEqualTo(shipmentsActions.get("date"), from));
	}

}
