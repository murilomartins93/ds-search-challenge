package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	private static LocalDate TODAY = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public Page<SaleReportDTO> findAll(String minDate, String maxDate, String name, Pageable pageable) {
		
		Page<Sale> result = repository.searchSales(convertMin(minDate), convertMax(maxDate), name, pageable);
		return result.map(x -> new SaleReportDTO(x));
	}
	
	@Transactional(readOnly = true) 
	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		return repository.searchSummarySales(convertMin(minDate), convertMax(maxDate));			
	}

	private LocalDate convertMin(String minDate) {
		return (minDate.isEmpty()) ? TODAY.minusYears(1L) : LocalDate.parse(minDate);
	}
	
	private LocalDate convertMax(String maxDate) {
		return (maxDate.isEmpty()) ? TODAY : LocalDate.parse(maxDate);
	}
	
}
