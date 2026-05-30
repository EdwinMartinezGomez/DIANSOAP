package com.dian.soap.DianSoap.controller;

import com.dian.soap.DianSoap.entities.InvoiceRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.getCorreoCliente());
            helper.setSubject("Confirmacion de factura " + invoiceNumber);
            helper.setText("Su factura electronica fue generada exitosamente.");
            helper.addAttachment("factura-" + invoiceNumber + ".pdf", new ByteArrayResource(pdfBytes));
            mailSender.send(message);
        } catch (MessagingException ex) {
            logger.error("Error enviando correo para la factura {}", invoiceNumber, ex);
        }
    }
}
