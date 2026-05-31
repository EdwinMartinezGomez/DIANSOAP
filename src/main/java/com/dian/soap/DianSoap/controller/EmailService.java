package com.dian.soap.DianSoap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dian.soap.DianSoap.entities.InvoiceRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.enabled:false}") boolean mailEnabled) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
    }

    public void sendInvoiceEmail(InvoiceRequest request, byte[] pdfBytes, String invoiceNumber) {
        if (!mailEnabled) {
            logger.info("Envio de correo deshabilitado. Factura {} no enviada.", invoiceNumber);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getCorreoVendedor());
            helper.setSubject("Confirmación de envío - Factura " + invoiceNumber);
            helper.setText(
                "<h2>Hola " + request.getNombreVendedor() + ",</h2>" +
                "<p>La factura electrónica fue generada y enviada exitosamente.</p>" +
                "<ul>" +
                "<li><b>Número de factura:</b> " + invoiceNumber + "</li>" +
                "<li><b>Cliente:</b> " + request.getNombreCliente() + "</li>" +
                "<li><b>Identificación cliente:</b> " + request.getIdentificacionCliente() + "</li>" +
                "<li><b>Producto:</b> " + request.getProductoVendido() + "</li>" +
                "<li><b>Cantidad:</b> " + request.getCantidadVendida() + "</li>" +
                "<li><b>Precio unitario:</b> $" + request.getPrecioUnitario() + "</li>" +
                "</ul>" +
                "<p>Encuentra la factura adjunta en este correo.</p>" +
                "<br><small>Este es un correo automático, por favor no responder.</small>",
                true
            );

            helper.addAttachment("factura-" + invoiceNumber + ".pdf", new ByteArrayResource(pdfBytes));
            mailSender.send(message);
            logger.info("Correo enviado al vendedor: {}", request.getCorreoVendedor());

        } catch (MessagingException ex) {
            logger.error("Error enviando correo para la factura {}", invoiceNumber, ex);
        }
    }
}
