package cat.institutmarianao.shipmentsws.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
<<<<<<< HEAD:shipmentsws-alumnes/src/main/java/cat/institutmarianao/shipmentsws/model/Shipment.java
=======
import jakarta.persistence.FetchType;
>>>>>>> 961af2798696a867e356e9ca500b784ebb621397:proyecto_guiado_clase.zip_expanded/shipmentsws-alumnes-OLD/src/main/java/cat/institutmarianao/shipmentsws/model/Shipment.java
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
<<<<<<< HEAD:shipmentsws-alumnes/src/main/java/cat/institutmarianao/shipmentsws/model/Shipment.java
@Table(name="shipments")
=======
@Table(name = "shipments")
>>>>>>> 961af2798696a867e356e9ca500b784ebb621397:proyecto_guiado_clase.zip_expanded/shipmentsws-alumnes-OLD/src/main/java/cat/institutmarianao/shipmentsws/model/Shipment.java
public class Shipment implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;

	public enum Category {
		PARTICULAR, COMPANY, GOVERNMENT
	}

	public static final String PENDING = "PENDING";
	public static final String IN_PROCESS = "IN_PROCESS";
	public static final String DELIVERED = "DELIVERED";

	public enum Status {
		PENDING, IN_PROCESS, DELIVERED
	}

	/* Lombok */
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Category category;

	@ManyToOne
	private Address sender;

	@ManyToOne
	private Address recipient;

	private Float weight;
	private Float height;
	private Float width;
	private Float length;

	private Boolean express;
	private Boolean fragile;

<<<<<<< HEAD:shipmentsws-alumnes/src/main/java/cat/institutmarianao/shipmentsws/model/Shipment.java
	@Size(max=MAX_DESCRIPTION)
	private String note;

	@OneToMany(mappedBy="shipment",cascade=CascadeType.ALL)
=======
	@Size(max = MAX_DESCRIPTION)
	private String note;

	@OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
>>>>>>> 961af2798696a867e356e9ca500b784ebb621397:proyecto_guiado_clase.zip_expanded/shipmentsws-alumnes-OLD/src/main/java/cat/institutmarianao/shipmentsws/model/Shipment.java
	private List<Action> tracking;

	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	/* Hibernate */
	@Formula("(SELECT CASE a.type WHEN '" + Action.RECEPTION + "' THEN '" + PENDING + "' " + " WHEN '"
			+ Action.ASSIGNMENT + "' THEN '" + IN_PROCESS + "' " + " WHEN '" + Action.DELIVERY + "' THEN '" + DELIVERED
			+ "' ELSE NULL END FROM actions a "
			+ " WHERE a.date=( SELECT MAX(last_action.date) FROM actions last_action "
			+ " WHERE last_action.shipment_id=a.shipment_id AND last_action.shipment_id=id ))")
	// Lombok
	@Setter(AccessLevel.NONE)
	@JsonIgnore
	private Status status;
}
