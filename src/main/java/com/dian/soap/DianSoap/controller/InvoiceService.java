package com.dian.soap.DianSoap.controller;

import com.dian.soap.DianSoap.entities.InvoiceRequest;
import com.dian.soap.DianSoap.entities.InvoiceResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final PdfService pdfService;
    private final EmailService emailService;

    public InvoiceService(PdfService pdfService, EmailService emailService) {
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    public InvoiceResponse processInvoice(InvoiceRequest request) {
        validateRequest(request);

        String invoiceNumber = generateInvoiceNumber();
        BigDecimal total = request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidadVendida()));

        byte[] pdfBytes = pdfService.generateInvoicePdf(request, invoiceNumber, total);
        emailService.sendInvoiceEmail(request, pdfBytes, invoiceNumber);

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
