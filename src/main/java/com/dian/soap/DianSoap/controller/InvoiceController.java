package com.dian.soap.DianSoap.controller;

import com.dian.soap.DianSoap.entities.InvoiceRequest;
import com.dian.soap.DianSoap.entities.InvoiceResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public InvoiceResponse createInvoice(@RequestBody InvoiceRequest request) {
        return invoiceService.processInvoice(request);
    }
}
