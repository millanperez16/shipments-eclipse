package cat.institutmarianao.shipmentsws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cat.institutmarianao.shipmentsws.PasswordSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//JPA
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "role")
//JSON

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role", visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = Courier.class, name = "COURIER"),
		@JsonSubTypes.Type(value = Receptionist.class, name = "RECEPTIONIST"),
		@JsonSubTypes.Type(value = LogisticsManager.class, name = "LOGISTICS_MANAGER") })
public abstract class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Values for role - MUST be constants (can not be enums) */
	public static final String RECEPTIONIST = "RECEPTIONIST";
	public static final String LOGISTICS_MANAGER = "LOGISTICS_MANAGER";
	public static final String COURIER = "COURIER";

	public enum Role {
		RECEPTIONIST, LOGISTICS_MANAGER, COURIER
	}

	public static final int MIN_USERNAME = 2;
	public static final int MAX_USERNAME = 25;
	public static final int MIN_PASSWORD = 4;
	public static final int MIN_FULL_NAME = 3;
	public static final int MAX_FULL_NAME = 100;
	public static final int MAX_EXTENSION = 9999;

	/* Lombok */
	@EqualsAndHashCode.Include
	@Id
	@Column(length = MAX_USERNAME, nullable = false)
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	protected String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, insertable = false, updatable = false)
	protected Role role;

	@Column(nullable = false)
	@Size(min = MIN_PASSWORD)
	@JsonSerialize(using = PasswordSerializer.class)
	protected String password;

	@Column(name = "full_name", length = MAX_FULL_NAME, nullable = false)
	@Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME)
	protected String fullName;

	@Max(value = MAX_EXTENSION)
	protected Integer extension;

}
