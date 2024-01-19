package cat.institutmarianao.shipmentsws.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.shipmentsws.model.Courier;
import cat.institutmarianao.shipmentsws.model.LogisticsManager;
import cat.institutmarianao.shipmentsws.model.Receptionist;
import cat.institutmarianao.shipmentsws.model.User;
import cat.institutmarianao.shipmentsws.model.User.Role;
import cat.institutmarianao.shipmentsws.services.UserService;
import cat.institutmarianao.shipmentsws.validation.groups.OnUserCreate;
import cat.institutmarianao.shipmentsws.validation.groups.OnUserUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/* Swagger */
@Tag(name = "User", description = "UserController API")
/**/
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/* Swagger */
	@Operation(summary = "Authenticate a user")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
			@Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"username\":\"string\",\"password\":\"string\"}") }) })
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(oneOf = {
			Receptionist.class, LogisticsManager.class,	Courier.class }, discriminatorProperty = "role")) }, description = "User authenticated ok")
	@ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json") }, description = "Resource not found")
	/**/
	@PostMapping("/authenticate")
	public User authenticate(@RequestBody Map<String, String> usernamePassword) {
		return userService.authenticate(usernamePassword.get("username"), usernamePassword.get("password"));
	}

	/* Swagger */
	@Operation(summary = "Find all users")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(anyOf = {
					Receptionist.class, LogisticsManager.class, Courier.class }, discriminatorProperty = "role"))) }, description = "Users retrieved ok")
	/**/
	@GetMapping(value = "/find/all")
	public List<User> findAll(@RequestParam(value = "roles", required = false) Role[] roles,
			@RequestParam(value = "fullName", required = false) String fullName) {

		return userService.findAll(roles, fullName);

	}

	/* Swagger */
	@Operation(summary = "Get user by id")
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(anyOf = {
			Receptionist.class, LogisticsManager.class,	Courier.class }, discriminatorProperty = "role")) }, description = "User retrieved ok")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Resource not found")
	/**/
	@GetMapping("/get/by/username/{username}")
	public User findByUsername(@PathVariable("username") @NotBlank String username) {
		return userService.getByUsername(username);
	}

	/* Swagger */
	@Operation(summary = "Save a user")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
			@Content(mediaType = "application/json", schema = @Schema(oneOf = { 
					Receptionist.class,	LogisticsManager.class, Courier.class }, discriminatorProperty = "role"))})
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(anyOf = {
			Receptionist.class, LogisticsManager.class,	Courier.class }, discriminatorProperty = "role")) }, description = "User saved ok")
	/**/
	@PostMapping("/save")
	@Validated(OnUserCreate.class)
	public User save(@RequestBody @Valid User user) {
		return userService.save(encodePassword(user));
	}

	/* Swagger */
	@Operation(summary = "Update a user")
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(anyOf = {
			Receptionist.class, LogisticsManager.class, Courier.class }, discriminatorProperty = "role")) }, description = "User updated ok")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Resource not found")
	/**/
	@PutMapping("/update")
	@Validated(OnUserUpdate.class)
	public User update(@RequestBody @Valid User user) {
		return userService.update(encodePassword(user));
	}

	/* Swagger */
	@Operation(summary = "Delete a user")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json") }, description = "User deleted ok")
	/**/
	@DeleteMapping("/delete/by/username/{username}")
	public void deleteByUsername(@PathVariable("username") @NotBlank String username) {
		userService.deleteByUsername(username);
	}

	private User encodePassword(User user) {
		Logger logger = LoggerFactory.getLogger(UserController.class);
		logger.info("PWD"+user.getPassword());
		
		String rawPassword = user.getPassword();
		if (rawPassword != null) user.setPassword(passwordEncoder.encode(rawPassword));
		logger.info("BDPWD"+user.getPassword());
		return user;
	}
}
