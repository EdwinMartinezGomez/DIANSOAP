package com.dian.soap.DianSoap.entities;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvoiceResponse")
@XmlRootElement(name = "InvoiceResponse", namespace = "http://dian.soap/Factura")
public class InvoiceResponse {

    @XmlElement(name = "Status", required = true)
    private String status;

    @XmlElement(name = "Message", required = true)
    private String message;

    @XmlElement(name = "InvoiceNumber", required = true)
    private String invoiceNumber;

    @XmlElement(name = "PdfBase64", required = true)
    private String pdfBase64;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPdfBase64() {
        return pdfBase64;
    }

    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }
}
