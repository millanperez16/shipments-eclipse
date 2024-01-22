package cat.institutmarianao.shipmentsws.services;

import java.util.List;

import cat.institutmarianao.shipmentsws.model.Address;

public interface AddressService {
	List<Address> findAll();
	
	Address findById(Long id);
}
