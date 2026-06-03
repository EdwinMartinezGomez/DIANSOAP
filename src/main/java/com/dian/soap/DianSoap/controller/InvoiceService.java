package com.dian.soap.DianSoap.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dian.soap.DianSoap.entities.InvoiceRequest;
import com.dian.soap.DianSoap.entities.InvoiceResponse;
import com.dian.soap.DianSoap.entities.InvoiceSaved;
import com.dian.soap.DianSoap.repository.InvoiceRepository;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final PdfService pdfService;
    private final EmailService emailService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(PdfService pdfService, EmailService emailService, InvoiceRepository invoiceRepository) {
        this.pdfService = pdfService;
        this.emailService = emailService;
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceResponse processInvoice(InvoiceRequest request) {
        validateRequest(request);

        String invoiceNumber = generateInvoiceNumber();
        BigDecimal total = request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidadVendida()));

        byte[] pdfBytes = pdfService.generateInvoicePdf(request, invoiceNumber, total);
        emailService.sendInvoiceEmail(request, pdfBytes, invoiceNumber);

        // Guardar en H2 
        BigDecimal subtotal = request.getPrecioUnitario()
            .multiply(BigDecimal.valueOf(request.getCantidadVendida()));
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.19"))
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalConIva = subtotal.add(iva);

        InvoiceSaved invoice = new InvoiceSaved();
        invoice.setNumeroFactura(invoiceNumber);
        invoice.setFechaEmision(LocalDateTime.now());
        invoice.setIdentificacionVendedor(request.getIdentificacionVendedor());
        invoice.setNombreVendedor(request.getNombreVendedor());
        invoice.setActividadProductiva(request.getActividadProductiva());
        invoice.setTelefonoVendedor(request.getTelefonoVendedor());
        invoice.setCorreoVendedor(request.getCorreoVendedor());
        invoice.setProductoVendido(request.getProductoVendido());
        invoice.setCodigoProducto(request.getCodigoProducto());
        invoice.setPrecioUnitario(request.getPrecioUnitario());
        invoice.setCantidadVendida(request.getCantidadVendida());
        invoice.setNombreCliente(request.getNombreCliente());
        invoice.setIdentificacionCliente(request.getIdentificacionCliente());
        invoice.setTelefonoCliente(request.getTelefonoCliente());
        invoice.setCorreoCliente(request.getCorreoCliente());
        invoice.setSubtotal(subtotal);
        invoice.setIva(iva);
        invoice.setTotal(totalConIva);
        invoice.setEstado("Enviada");
        invoice.setPdfBase64(Base64.getEncoder().encodeToString(pdfBytes));
        invoiceRepository.save(invoice);
      

        InvoiceResponse response = new InvoiceResponse();
        response.setStatus("OK");
        response.setMessage("Factura procesada y enviada");
        response.setInvoiceNumber(invoiceNumber);
        response.setPdfBase64(Base64.getEncoder().encodeToString(pdfBytes));
        return response;
    }

    private void validateRequest(InvoiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud es obligatoria");
        }
        requireText(request.getIdentificacionVendedor(), "IdentificacionVendedor");
        requireText(request.getNombreVendedor(), "NombreVendedor");
        requireText(request.getActividadProductiva(), "ActividadProductiva");
        requireText(request.getTelefonoVendedor(), "TelefonoVendedor");
        requireText(request.getCorreoVendedor(), "CorreoVendedor");
        requireText(request.getProductoVendido(), "ProductoVendido");
        requireText(request.getCodigoProducto(), "CodigoProducto");
        if (request.getPrecioUnitario() == null || request.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("PrecioUnitario debe ser mayor a 0");
        }
        if (request.getCantidadVendida() == null || request.getCantidadVendida() <= 0) {
            throw new IllegalArgumentException("CantidadVendida debe ser mayor a 0");
        }
        requireText(request.getNombreCliente(), "NombreCliente");
        requireText(request.getIdentificacionCliente(), "IdentificacionCliente");
        requireText(request.getTelefonoCliente(), "TelefonoCliente");
        requireText(request.getCorreoCliente(), "CorreoCliente");
    }

    private void requireText(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(fieldName + " es obligatorio");
        }
    }

    private String generateInvoiceNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "FAC-" + timestamp + "-" + suffix;
    }
}
