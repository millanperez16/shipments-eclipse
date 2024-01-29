package cat.institutmarianao.shipmentsws.model;

import java.io.Serializable;

<<<<<<< HEAD:shipmentsws-alumnes/src/main/java/cat/institutmarianao/shipmentsws/model/Receptionist.java
import com.fasterxml.jackson.annotation.JsonSubTypes;

=======
>>>>>>> 961af2798696a867e356e9ca500b784ebb621397:proyecto_guiado_clase.zip_expanded/shipmentsws-alumnes-OLD/src/main/java/cat/institutmarianao/shipmentsws/model/Receptionist.java
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("RECEPTIONIST")
public class Receptionist extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_PLACE = 100;

	@ManyToOne
	private Office office;

	@Column(length = MAX_PLACE, nullable = true)
<<<<<<< HEAD:shipmentsws-alumnes/src/main/java/cat/institutmarianao/shipmentsws/model/Receptionist.java
	@Size(max=MAX_PLACE)
=======
	@Size(max = MAX_PLACE)
>>>>>>> 961af2798696a867e356e9ca500b784ebb621397:proyecto_guiado_clase.zip_expanded/shipmentsws-alumnes-OLD/src/main/java/cat/institutmarianao/shipmentsws/model/Receptionist.java
	private String place;

}
