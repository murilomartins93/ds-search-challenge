package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query("SELECT obj FROM Sale obj WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%')) AND obj.date BETWEEN :initialDate AND :finalDate")
	Page<Sale> searchSales(LocalDate initialDate, LocalDate finalDate, String sellerName, Pageable pageable);
	
	@Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, SUM(obj.amount)) "
			+ "FROM Sale obj WHERE obj.date BETWEEN :initialDate AND :finalDate GROUP BY obj.seller.name")
	List<SaleSummaryDTO> searchSummarySales(LocalDate initialDate, LocalDate finalDate);
}


