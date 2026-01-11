package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleReportDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleReportDTO(entity);
	}

	public Page<SaleReportDTO> searchSalesReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate max = (maxDate == null || maxDate.isEmpty())? today : LocalDate.parse(maxDate);
		LocalDate min = (minDate == null || minDate.isEmpty())? max.minusYears(1L) : LocalDate.parse(minDate);
		String nameFilter = (name == null) ? "" : name;
		Page<Object[]> page = repository.searchSalesReport(min, max, nameFilter, pageable);
		return page.map(obj -> {return new SaleReportDTO(((Number) obj[0]).longValue(), ((java.sql.Date) obj[1]).toLocalDate(), ((Number) obj[2]).doubleValue(), (String) obj[3]);
		});
	}

	public List<SaleSummaryDTO> searchSalesSummary(String minDate, String maxDate){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate max = (maxDate == null || maxDate.isEmpty())? today : LocalDate.parse(maxDate);
		LocalDate min = (minDate == null || minDate.isEmpty())? max.minusYears(1L) : LocalDate.parse(minDate);
		List<Object[]> result = repository.searchSalesSummary(min, max);
		return result.stream().map(obj -> new SaleSummaryDTO((String) obj[0],((Number) obj[1]).doubleValue())).collect(Collectors.toList());
	}



}
