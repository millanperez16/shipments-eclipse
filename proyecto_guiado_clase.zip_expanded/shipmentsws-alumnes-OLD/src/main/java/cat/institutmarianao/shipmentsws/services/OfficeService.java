package cat.institutmarianao.shipmentsws.services;

import java.util.List;

import cat.institutmarianao.shipmentsws.model.Office;

public interface OfficeService {
	List<Office> findAll();
	
	Office findById(Long id);
}
