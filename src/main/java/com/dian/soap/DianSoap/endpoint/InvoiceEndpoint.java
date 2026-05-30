package com.dian.soap.DianSoap.endpoint;

import com.dian.soap.DianSoap.controller.InvoiceService;
import com.dian.soap.DianSoap.entities.InvoiceRequest;
import com.dian.soap.DianSoap.entities.InvoiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class InvoiceEndpoint {

    private static final String NAMESPACE_URI = "http://dian.soap/Factura";
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEndpoint.class);

    private final InvoiceService invoiceService;

    public InvoiceEndpoint(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InvoiceRequest")
    @ResponsePayload
    public InvoiceResponse submitInvoice(@RequestPayload InvoiceRequest request) {
        try {
            return invoiceService.processInvoice(request);
        } catch (Exception ex) {
            logger.error("Error procesando factura SOAP", ex);
            throw ex;
        }
    }
}
