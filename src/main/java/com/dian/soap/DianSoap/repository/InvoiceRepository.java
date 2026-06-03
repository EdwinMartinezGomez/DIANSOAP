package com.dian.soap.DianSoap.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dian.soap.DianSoap.entities.InvoiceSaved;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceSaved, Long> {

    // Buscar por cliente o vendedor (para el filtro del dashboard)
    List<InvoiceSaved> findByNombreClienteContainingIgnoreCaseOrNombreVendedorContainingIgnoreCase(
        String cliente, String vendedor
    );

    // Total de ventas acumuladas
    @Query("SELECT COALESCE(SUM(f.total), 0) FROM InvoiceSaved f")
    BigDecimal sumTotalVentas();

    // Conteo de facturas de hoy
    @Query("SELECT COUNT(f) FROM InvoiceSaved f WHERE CAST(f.fechaEmision AS date) = CURRENT_DATE")
    Long countFacturasHoy();
}
