package cat.institutmarianao.shipmentsws.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.shipmentsws.exception.NotFoundException;
import cat.institutmarianao.shipmentsws.model.Courier;
import cat.institutmarianao.shipmentsws.model.Receptionist;
import cat.institutmarianao.shipmentsws.model.User;
import cat.institutmarianao.shipmentsws.model.User.Role;
import cat.institutmarianao.shipmentsws.repositories.UserRepository;
import cat.institutmarianao.shipmentsws.services.UserService;
import cat.institutmarianao.shipmentsws.specifications.UserWithFullName;
import cat.institutmarianao.shipmentsws.specifications.UserWithRole;
import cat.institutmarianao.shipmentsws.validation.groups.OnUserCreate;
import cat.institutmarianao.shipmentsws.validation.groups.OnUserUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotEmpty;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

@Validated
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User authenticate(@NotEmpty String username, @NotEmpty String password) {
		User user = getByUsername(username);
		
		/*Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
		logger.info("PWD"+password);
		logger.info("BDPWD"+user.getPassword());*/
		
		if (!passwordEncoder.matches(password, user.getPassword()))
			throw new ValidationException(messageSource.getMessage("error.UserService.user.password", new Object[] { username }, LocaleContextHolder.getLocale()));
		
		return user;
	}
	
	@Override
	public List<User> findAll(Role[] roles, String fullName) {
		Specification<User> spec = Specification.where(new UserWithRole(roles)).and(new UserWithFullName(fullName));
		return userRepository.findAll(spec);
	}

	@Override
	public User getByUsername(@NotBlank String username) {
		return userRepository.findById(username).orElseThrow(NotFoundException::new);
	}

	@Override
	@Validated(OnUserCreate.class)
	public User save(@NotNull @Valid User user) {
		/*if (!Role.LOGISTICS_MANAGER.equals(performer.getRole())) {
			String errorMerrage = messageSource.getMessage("error.Performer.is.not.valid", null, LocaleContextHolder.getLocale());
			throw new ValidationException (errorMerrage);
		}*/
		
		User ret = userRepository.saveAndFlush(user);
		return ret;
	}

	@Override
	@Validated(OnUserUpdate.class)
	public User update(@NotNull @Valid User user) {
		User dbUser = getByUsername(user.getUsername());

		if (user.getPassword() != null) {
			dbUser.setPassword(user.getPassword());
		}
		if (user.getFullName() != null) {
			dbUser.setFullName(user.getFullName());
		}
		if (user.getExtension() != null) {
			dbUser.setExtension(user.getExtension());
		}

		if (user instanceof Receptionist receptionist && dbUser instanceof Receptionist dbReceptionist) {
			
			if (receptionist.getOffice() != null) {
				dbReceptionist.setOffice(receptionist.getOffice());
			}
			if (receptionist.getPlace() != null) {
				dbReceptionist.setPlace(receptionist.getPlace());
			}
		} else if (user instanceof Courier courier && dbUser instanceof Courier dbCourier) {
			if (courier.getCompany() != null) {
				dbCourier.setCompany(courier.getCompany());
			}
		}

		return userRepository.saveAndFlush(dbUser);
	}

	@Override
	public void deleteByUsername(@NotBlank String username) {
		userRepository.deleteById(username);
	}
}
