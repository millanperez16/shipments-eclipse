package cat.institutmarianao.shipmentsws.services;

import java.util.List;

import cat.institutmarianao.shipmentsws.model.Company;

public interface CompanyService {
	List<Company> findAll();
	
	Company findById(Long id);
}
