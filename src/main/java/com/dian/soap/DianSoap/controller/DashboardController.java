package com.dian.soap.DianSoap.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dian.soap.DianSoap.entities.InvoiceSaved;
import com.dian.soap.DianSoap.repository.InvoiceRepository;


@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    // GET /api/dashboard/facturas
    @GetMapping("/facturas")
    public ResponseEntity<List<InvoiceSaved>> getFacturas(
            @RequestParam(required = false) String buscar) {

        List<InvoiceSaved> facturas;
        if (buscar != null && !buscar.isBlank()) {
            facturas = invoiceRepository
                .findByNombreClienteContainingIgnoreCaseOrNombreVendedorContainingIgnoreCase(
                    buscar, buscar);
        } else {
            facturas = invoiceRepository.findAll();
        }
        // Más reciente primero
        facturas.sort(Comparator.comparing(InvoiceSaved::getFechaEmision).reversed());
        return ResponseEntity.ok(facturas);
    }

    // GET /api/dashboard/stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFacturas",  invoiceRepository.count());
        stats.put("totalVentas",    invoiceRepository.sumTotalVentas());
        stats.put("facturasHoy",    invoiceRepository.countFacturasHoy());
        long vendedores = invoiceRepository.findAll().stream()
            .map(InvoiceSaved::getNombreVendedor)
            .distinct().count();
        stats.put("totalVendedores", vendedores);
        return ResponseEntity.ok(stats);
    }
}
