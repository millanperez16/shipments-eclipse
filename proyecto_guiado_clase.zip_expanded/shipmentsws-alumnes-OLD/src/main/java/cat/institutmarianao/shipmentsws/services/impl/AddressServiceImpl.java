package cat.institutmarianao.shipmentsws.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.shipmentsws.model.Address;
import cat.institutmarianao.shipmentsws.repositories.AddressRepository;
import cat.institutmarianao.shipmentsws.services.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public List<Address> findAll() {
		return addressRepository.findAll();
	}

	@Override
	public Address findById(Long id) {
		Optional<Address> address = addressRepository.findById(id);
		if (address.isEmpty()) {
			return null;
		}
		return address.get();
	}

}
