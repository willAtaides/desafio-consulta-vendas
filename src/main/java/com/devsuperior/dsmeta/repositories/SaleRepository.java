package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(
            nativeQuery = true,
            value = """
        SELECT 
            s.id,
            s.date,
            s.amount,
            sel.name AS seller_name
        FROM tb_sales s
        INNER JOIN tb_seller sel ON s.seller_id = sel.id
        WHERE 
            s.date BETWEEN :minDate AND :maxDate
            AND LOWER(sel.name) LIKE LOWER(CONCAT('%', :name, '%'))
        ORDER BY s.date DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_sales s
        INNER JOIN tb_seller sel ON s.seller_id = sel.id
        WHERE 
            s.date BETWEEN :minDate AND :maxDate
            AND LOWER(sel.name) LIKE LOWER(CONCAT('%', :name, '%'))
    """
    )
    Page<Object[]> searchSalesReport(@Param("minDate") LocalDate minDate, @Param("maxDate")LocalDate maxDate, @Param("name") String name, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = """
        SELECT 
            sel.name AS seller_name,
            SUM(s.amount) AS total
        FROM tb_sales s
        INNER JOIN tb_seller sel ON s.seller_id = sel.id
        WHERE 
            s.date BETWEEN :minDate AND :maxDate
        GROUP BY sel.name
        ORDER BY sel.name
    """
    )
    List<Object[]> searchSalesSummary(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate);

}
