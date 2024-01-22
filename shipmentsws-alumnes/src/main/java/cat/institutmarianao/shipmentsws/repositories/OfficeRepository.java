package cat.institutmarianao.shipmentsws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cat.institutmarianao.shipmentsws.model.Office;

public interface OfficeRepository extends JpaRepository<Office, Long>, JpaSpecificationExecutor<Office>{

}
