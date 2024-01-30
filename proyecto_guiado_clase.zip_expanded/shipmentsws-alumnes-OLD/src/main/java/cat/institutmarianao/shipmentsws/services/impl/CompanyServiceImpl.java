package cat.institutmarianao.shipmentsws.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.shipmentsws.exception.NotFoundException;
import cat.institutmarianao.shipmentsws.model.Company;
import cat.institutmarianao.shipmentsws.repositories.CompanyRepository;
import cat.institutmarianao.shipmentsws.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Override
	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	@Override
	public Company findById(Long id) {
		return companyRepository.findById(id).orElseThrow(NotFoundException::new);
	}

}
