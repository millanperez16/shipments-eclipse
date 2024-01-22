package cat.institutmarianao.shipmentsws.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.shipmentsws.model.Office;
import cat.institutmarianao.shipmentsws.repositories.OfficeRepository;
import cat.institutmarianao.shipmentsws.services.OfficeService;

@Service
public class OfficeServiceImpl implements OfficeService {
	
	@Autowired
	private OfficeRepository officeRepository;

	@Override
	public List<Office> findAll() {
		return officeRepository.findAll();
	}

	@Override
	public Office findById(Long id) {
		Optional<Office> office = officeRepository.findById(id);
		if (office.isEmpty()) {
			return null;
		}
		return office.get();
	}

}
