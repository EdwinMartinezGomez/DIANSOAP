package com.dian.soap.DianSoap.controller;

import com.dian.soap.DianSoap.entities.InvoiceRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(InvoiceRequest request, String invoiceNumber, BigDecimal total) {
        try {
            Document document = new Document();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, output);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            Paragraph logo = new Paragraph("DIAN", titleFont);
            logo.setAlignment(Element.ALIGN_RIGHT);
            document.add(logo);

            Paragraph title = new Paragraph("Factura Electronica", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Numero: " + invoiceNumber, bodyFont));
            document.add(new Paragraph(" ", bodyFont));

            document.add(new Paragraph("Datos del vendedor", sectionFont));
            document.add(new Paragraph("Identificacion: " + request.getIdentificacionVendedor(), bodyFont));
            document.add(new Paragraph("Nombre: " + request.getNombreVendedor(), bodyFont));
            document.add(new Paragraph("Actividad: " + request.getActividadProductiva(), bodyFont));
            document.add(new Paragraph("Telefono: " + request.getTelefonoVendedor(), bodyFont));
            document.add(new Paragraph("Correo: " + request.getCorreoVendedor(), bodyFont));
            document.add(new Paragraph(" ", bodyFont));

            document.add(new Paragraph("Datos del cliente", sectionFont));
            document.add(new Paragraph("Identificacion: " + request.getIdentificacionCliente(), bodyFont));
            document.add(new Paragraph("Nombre: " + request.getNombreCliente(), bodyFont));
            document.add(new Paragraph("Telefono: " + request.getTelefonoCliente(), bodyFont));
            document.add(new Paragraph("Correo: " + request.getCorreoCliente(), bodyFont));
            document.add(new Paragraph(" ", bodyFont));

            document.add(new Paragraph("Detalle del producto", sectionFont));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(headerCell("Producto"));
            table.addCell(headerCell("Codigo"));
            table.addCell(headerCell("Cantidad"));
            table.addCell(headerCell("Subtotal"));

            table.addCell(new Phrase(request.getProductoVendido(), bodyFont));
            table.addCell(new Phrase(request.getCodigoProducto(), bodyFont));
            table.addCell(new Phrase(String.valueOf(request.getCantidadVendida()), bodyFont));

            BigDecimal subtotal = request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidadVendida()));
            table.addCell(new Phrase(formatCurrency(subtotal), bodyFont));

            document.add(table);
            document.add(new Paragraph(" ", bodyFont));

            document.add(new Paragraph("Total: " + formatCurrency(total), sectionFont));

            document.close();
            return output.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible generar el PDF", ex);
        }
    }

    private PdfPCell headerCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private String formatCurrency(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return format.format(value);
    }
}
