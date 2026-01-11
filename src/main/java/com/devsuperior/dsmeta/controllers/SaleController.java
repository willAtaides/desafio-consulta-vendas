package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.services.SaleService;

import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

	@Autowired
	private SaleService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<SaleReportDTO> findById(@PathVariable Long id) {
		SaleReportDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/report")
	public Page<SaleReportDTO> getSalesReport(
			@RequestParam(value = "minDate", required = false)String minDate,
			@RequestParam(value = "maxDate", required = false) String maxDate,
			@RequestParam(value = "name", required = false) String name,
			@PageableDefault(size =20)Pageable pageable){
		return service.searchSalesReport(minDate, maxDate, name, pageable);
	}

	@GetMapping(value = "/summary")
	public List<SaleSummaryDTO> getSalesSummary(
			@RequestParam(value = "minDate", required = false) String minDate,
			@RequestParam(value = "maxDate", required = false) String maxDate) {
		return service.searchSalesSummary(minDate,maxDate);
	}
}
