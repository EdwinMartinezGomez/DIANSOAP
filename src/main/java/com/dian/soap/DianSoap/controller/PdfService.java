package com.dian.soap.DianSoap.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.dian.soap.DianSoap.entities.InvoiceRequest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

    // Paleta DIAN oficial: verde + gris + blanco
    private static final BaseColor DIAN_GREEN  = new BaseColor(0, 121, 63);   // verde DIAN
    private static final BaseColor DIAN_GREEN2 = new BaseColor(0, 160, 80);   // verde claro acento
    private static final BaseColor BARRA_BG    = new BaseColor(0, 121, 63);   // fondo barras sección
    private static final BaseColor TABLA_HEAD  = new BaseColor(220, 240, 230);// verde muy suave tabla
    private static final BaseColor LINEA       = new BaseColor(0, 121, 63);   // borde verde fino
    private static final BaseColor LINEA_GRAY  = new BaseColor(200, 200, 200);// borde gris tabla
    private static final BaseColor GRIS_FONDO  = new BaseColor(248, 248, 248);
    private static final BaseColor DARK        = new BaseColor(20, 20, 20);
    private static final BaseColor WHITE       = BaseColor.WHITE;

    public byte[] generateInvoicePdf(InvoiceRequest request, String invoiceNumber, BigDecimal total) {
        try {
            Document doc = new Document(PageSize.A4, 40, 40, 30, 30);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, out);
            doc.open();

            String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            // ══════════════════════════════════════
            // 1. ENCABEZADO
            // ══════════════════════════════════════
            PdfPTable encabezado = new PdfPTable(new float[]{2.5f, 3.5f, 2f});
            encabezado.setWidthPercentage(100);
            encabezado.setSpacingAfter(4);

            // — Logo + datos emisor —
            PdfPCell cLogo = new PdfPCell();
            cLogo.setBorder(Rectangle.BOX);
            cLogo.setBorderColor(LINEA);
            cLogo.setBorderWidth(0.8f);
            cLogo.setPadding(8);
            cLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            try {
                InputStream is = getClass().getResourceAsStream("/static/logo-dian.jpeg");
                if (is == null) is = getClass().getResourceAsStream("/static/logo-dian.png");
                if (is != null) {
                    Image logo = Image.getInstance(is.readAllBytes());
                    logo.scaleToFit(110, 50);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    cLogo.addElement(logo);
                }
            } catch (Exception ignored) {}
            Font fEmpBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, DIAN_GREEN);
            Font fEmpNorm = FontFactory.getFont(FontFactory.HELVETICA, 7.5f, DARK);
            Paragraph pEmp = new Paragraph();
            pEmp.setSpacingBefore(4);
            pEmp.add(new Chunk(request.getNombreVendedor() + "\n", fEmpBold));
            pEmp.add(new Chunk("NIT: " + request.getIdentificacionVendedor() + "\n", fEmpNorm));
            pEmp.add(new Chunk(request.getActividadProductiva() + "\n", fEmpNorm));
            pEmp.add(new Chunk("Tel: " + request.getTelefonoVendedor() + "\n", fEmpNorm));
            pEmp.add(new Chunk(request.getCorreoVendedor(), fEmpNorm));
            cLogo.addElement(pEmp);
            encabezado.addCell(cLogo);

            // — Título (sin fondo azul, solo texto y línea verde) —
            PdfPCell cTitulo = new PdfPCell();
            cTitulo.setBackgroundColor(WHITE);
            cTitulo.setBorder(Rectangle.BOX);
            cTitulo.setBorderColor(LINEA);
            cTitulo.setBorderWidth(0.8f);
            cTitulo.setPadding(12);
            cTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Font fTit  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, DIAN_GREEN);
            Font fSub  = FontFactory.getFont(FontFactory.HELVETICA, 8, DARK);
            Font fRes  = FontFactory.getFont(FontFactory.HELVETICA, 7, new BaseColor(100, 100, 100));
            Paragraph pTit = new Paragraph();
            pTit.setAlignment(Element.ALIGN_CENTER);
            pTit.add(new Chunk("FACTURA ELECTRÓNICA DE VENTA\n", fTit));
            pTit.add(new Chunk("Documento equivalente tributario\n", fSub));
            pTit.add(new Chunk("Resolución DIAN No. 000042 de 2020", fRes));
            cTitulo.addElement(pTit);
            encabezado.addCell(cTitulo);

            // — Número y fecha —
            PdfPCell cNum = new PdfPCell();
            cNum.setBackgroundColor(WHITE);
            cNum.setBorder(Rectangle.BOX);
            cNum.setBorderColor(LINEA);
            cNum.setBorderWidth(0.8f);
            cNum.setPadding(8);
            cNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Font fNL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, DIAN_GREEN);
            Font fNV = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, DARK);
            Font fNS = FontFactory.getFont(FontFactory.HELVETICA, 7.5f, DARK);
            Paragraph pNum = new Paragraph();
            pNum.setAlignment(Element.ALIGN_CENTER);
            pNum.add(new Chunk("No. FACTURA\n", fNL));
            pNum.add(new Chunk(invoiceNumber + "\n\n", fNV));
            pNum.add(new Chunk("FECHA DE EMISIÓN\n", fNL));
            pNum.add(new Chunk(fecha + "\n\n", fNS));
            pNum.add(new Chunk("FORMA DE PAGO\n", fNL));
            pNum.add(new Chunk("Contado", fNS));
            cNum.addElement(pNum);
            encabezado.addCell(cNum);

            doc.add(encabezado);

            // ══════════════════════════════════════
            // 2. VENDEDOR Y ADQUIRIENTE
            // ══════════════════════════════════════
            PdfPTable partes = new PdfPTable(new float[]{1f, 1f});
            partes.setWidthPercentage(100);
            partes.setSpacingAfter(4);

            partes.addCell(buildCard("DATOS DEL VENDEDOR / EMISOR", new String[][]{
                {"Razón Social", request.getNombreVendedor()},
                {"NIT",          request.getIdentificacionVendedor()},
                {"Actividad",    request.getActividadProductiva()},
                {"Teléfono",     request.getTelefonoVendedor()},
                {"Correo",       request.getCorreoVendedor()}
            }));
            partes.addCell(buildCard("DATOS DEL ADQUIRIENTE / COMPRADOR", new String[][]{
                {"Nombre",    request.getNombreCliente()},
                {"NIT / CC",  request.getIdentificacionCliente()},
                {"Teléfono",  request.getTelefonoCliente()},
                {"Correo",    request.getCorreoCliente()},
                {"País",      "Colombia"}
            }));
            doc.add(partes);

            // ══════════════════════════════════════
            // 3. TABLA DE ÍTEMS
            // ══════════════════════════════════════
            doc.add(barraSeccionTabla("DESCRIPCIÓN DE BIENES Y/O SERVICIOS"));

            PdfPTable tablaItems = new PdfPTable(new float[]{0.4f, 2.6f, 1.1f, 0.6f, 1.2f, 0.7f, 1.0f, 1.2f});
            tablaItems.setWidthPercentage(100);
            tablaItems.setSpacingAfter(0);

            String[] colsH   = {"#", "Descripción", "Código", "Und.", "P. Unitario", "Dto.", "IVA", "Total"};
            int[]    colsAln = {
                Element.ALIGN_CENTER, Element.ALIGN_LEFT,  Element.ALIGN_CENTER,
                Element.ALIGN_CENTER, Element.ALIGN_RIGHT, Element.ALIGN_CENTER,
                Element.ALIGN_RIGHT,  Element.ALIGN_RIGHT
            };
            Font fTH = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, DARK);
            for (String col : colsH) {
                PdfPCell h = new PdfPCell(new Phrase(col, fTH));
                h.setBackgroundColor(TABLA_HEAD);
                h.setHorizontalAlignment(Element.ALIGN_CENTER);
                h.setPadding(5);
                h.setBorderColor(LINEA_GRAY);
                h.setBorderWidth(0.5f);
                tablaItems.addCell(h);
            }

            BigDecimal precioUnit   = request.getPrecioUnitario();
            int        cantidad     = request.getCantidadVendida();
            BigDecimal subtotalItem = precioUnit.multiply(BigDecimal.valueOf(cantidad));
            BigDecimal ivaItem      = subtotalItem.multiply(new BigDecimal("0.19"))
                                        .setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalItem    = subtotalItem.add(ivaItem);

            Font fTB = FontFactory.getFont(FontFactory.HELVETICA, 8, DARK);
            String[] filaData = {
                "1",
                request.getProductoVendido(),
                request.getCodigoProducto(),
                "UND",
                formatCurrency(precioUnit),
                "0%",
                formatCurrency(ivaItem),
                formatCurrency(totalItem)
            };
            for (int i = 0; i < filaData.length; i++) {
                PdfPCell c = new PdfPCell(new Phrase(filaData[i], fTB));
                c.setBackgroundColor(WHITE);
                c.setPadding(5);
                c.setHorizontalAlignment(colsAln[i]);
                c.setBorderColor(LINEA_GRAY);
                c.setBorderWidth(0.5f);
                tablaItems.addCell(c);
            }
            // Fila vacía visual
            for (int i = 0; i < 8; i++) {
                PdfPCell e = new PdfPCell(new Phrase(" ", fTB));
                e.setBackgroundColor(GRIS_FONDO);
                e.setPadding(4);
                e.setBorderColor(LINEA_GRAY);
                e.setBorderWidth(0.5f);
                tablaItems.addCell(e);
            }
            doc.add(tablaItems);

            // ══════════════════════════════════════
            // 4. TOTALES + MEDIO DE PAGO
            // ══════════════════════════════════════
            PdfPTable bloqueBottom = new PdfPTable(new float[]{1.1f, 0.9f});
            bloqueBottom.setWidthPercentage(100);
            bloqueBottom.setSpacingAfter(4);

            // Medio de pago
            partes.addCell(buildCard("MEDIO DE PAGO", new String[][]{
                {"Forma",    "Contado"},
                {"Método",   "Transferencia bancaria"},
                {"Vence",    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))},
                {"Moneda",   "COP — Peso colombiano"}
            }));

            bloqueBottom.addCell(buildCard("MEDIO DE PAGO", new String[][]{
                {"Forma de pago", "Contado"},
                {"Método",        "Transferencia bancaria"},
                {"Vencimiento",   LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))},
                {"Moneda",        "COP — Peso colombiano"}
            }));

            // Totales
            PdfPCell cTotales = new PdfPCell();
            cTotales.setBorder(Rectangle.BOX);
            cTotales.setBorderColor(LINEA);
            cTotales.setBorderWidth(0.8f);
            cTotales.setPadding(0);

            PdfPTable tTotales = new PdfPTable(1);
            tTotales.setWidthPercentage(100);
            tTotales.addCell(barraSeccionCelda("RESUMEN DE VALORES"));

            PdfPTable gridTot = new PdfPTable(new float[]{1.6f, 1f});
            gridTot.setWidthPercentage(100);

            addFilaTotal(gridTot, "Subtotal (sin IVA):", formatCurrency(subtotalItem), false);
            addFilaTotal(gridTot, "IVA 19%:",            formatCurrency(ivaItem),      false);
            addFilaTotal(gridTot, "Descuentos:",         "$ 0,00",                     false);
            addFilaTotal(gridTot, "Otros cargos:",       "$ 0,00",                     false);

            // Fila total verde
            Font fTotLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, WHITE);
            PdfPCell ctL = new PdfPCell(new Phrase("TOTAL A PAGAR:", fTotLabel));
            ctL.setBackgroundColor(DIAN_GREEN);
            ctL.setPadding(7); ctL.setBorderColor(LINEA_GRAY); ctL.setBorderWidth(0.5f);
            ctL.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell ctV = new PdfPCell(new Phrase(formatCurrency(totalItem), fTotLabel));
            ctV.setBackgroundColor(DIAN_GREEN);
            ctV.setPadding(7); ctV.setBorderColor(LINEA_GRAY); ctV.setBorderWidth(0.5f);
            ctV.setHorizontalAlignment(Element.ALIGN_RIGHT);
            gridTot.addCell(ctL);
            gridTot.addCell(ctV);

            PdfPCell wrapTot = new PdfPCell();
            wrapTot.setBorder(Rectangle.NO_BORDER);
            wrapTot.setPadding(0);
            wrapTot.addElement(gridTot);
            tTotales.addCell(wrapTot);
            cTotales.addElement(tTotales);
            bloqueBottom.addCell(cTotales);

            doc.add(bloqueBottom);

            // ══════════════════════════════════════
            // 5. CUFE
            // ══════════════════════════════════════
            String cufe = invoiceNumber.replace("FAC-", "") + "-COL-DIAN-2026";
            PdfPTable tCufe = new PdfPTable(1);
            tCufe.setWidthPercentage(100);
            tCufe.setSpacingAfter(4);
            PdfPCell cCufe = new PdfPCell();
            cCufe.setBorder(Rectangle.BOX);
            cCufe.setBorderColor(LINEA);
            cCufe.setBorderWidth(0.8f);
            cCufe.setBackgroundColor(GRIS_FONDO);
            cCufe.setPadding(6);
            Font fCL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, DIAN_GREEN);
            Font fCV = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, DARK);
            Paragraph pCufe = new Paragraph();
            pCufe.add(new Chunk("CUFE: ", fCL));
            pCufe.add(new Chunk(cufe + "\n", fCV));
            pCufe.add(new Chunk("Verificar en: ", fCL));
            pCufe.add(new Chunk("https://catalogo-vpfe.dian.gov.co/User/SearchDocument", fCV));
            cCufe.addElement(pCufe);
            tCufe.addCell(cCufe);
            doc.add(tCufe);

            // ══════════════════════════════════════
            // 6. OBSERVACIONES + FIRMA
            // ══════════════════════════════════════
            PdfPTable tObs = new PdfPTable(new float[]{1.5f, 1f});
            tObs.setWidthPercentage(100);
            tObs.setSpacingAfter(4);

            PdfPCell cObs = buildCard("OBSERVACIONES", new String[][]{});
            // Reemplazamos el contenido con texto libre
            PdfPCell cObsFull = new PdfPCell();
            cObsFull.setBorder(Rectangle.BOX);
            cObsFull.setBorderColor(LINEA);
            cObsFull.setBorderWidth(0.8f);
            cObsFull.setPadding(0);
            PdfPTable tObsT = new PdfPTable(1);
            tObsT.setWidthPercentage(100);
            tObsT.addCell(barraSeccionCelda("OBSERVACIONES"));
            PdfPCell bObs = new PdfPCell();
            bObs.setBorder(Rectangle.NO_BORDER);
            bObs.setPadding(7);
            bObs.addElement(new Paragraph(
                "Documento generado electrónicamente conforme al Decreto 358 de 2020. " +
                "Conserve esta factura para efectos tributarios. " +
                "El documento con plena validez legal es el XML firmado digitalmente y validado ante la DIAN.",
                FontFactory.getFont(FontFactory.HELVETICA, 7.5f, DARK)));
            tObsT.addCell(bObs);
            cObsFull.addElement(tObsT);
            tObs.addCell(cObsFull);

            PdfPCell cFirma = new PdfPCell();
            cFirma.setBorder(Rectangle.BOX);
            cFirma.setBorderColor(LINEA);
            cFirma.setBorderWidth(0.8f);
            cFirma.setPadding(0);
            PdfPTable tFirmaT = new PdfPTable(1);
            tFirmaT.setWidthPercentage(100);
            tFirmaT.addCell(barraSeccionCelda("FIRMA ELECTRÓNICA"));
            PdfPCell bFirma = new PdfPCell();
            bFirma.setBorder(Rectangle.NO_BORDER);
            bFirma.setPadding(7);
            Font fFB = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, DIAN_GREEN);
            Font fFN = FontFactory.getFont(FontFactory.HELVETICA, 7.5f, DARK);
            Paragraph pFirma = new Paragraph();
            pFirma.add(new Chunk("Documento firmado digitalmente\n", fFB));
            pFirma.add(new Chunk("Software: DIANSOAP v1.0\n", fFN));
            pFirma.add(new Chunk("Proveedor tecnológico: Sistema Propio\n", fFN));
            pFirma.add(new Chunk("NIT: " + request.getIdentificacionVendedor(), fFN));
            bFirma.addElement(pFirma);
            tFirmaT.addCell(bFirma);
            cFirma.addElement(tFirmaT);
            tObs.addCell(cFirma);

            doc.add(tObs);

            // ══════════════════════════════════════
            // 7. PIE DE PÁGINA
            // ══════════════════════════════════════
            PdfPTable pie = new PdfPTable(new float[]{1f, 2f, 1f});
            pie.setWidthPercentage(100);

            pie.addCell(pieCelda("DIAN\nDirección de Impuestos\ny Aduanas Nacionales\nwww.dian.gov.co", true));
            pie.addCell(pieCelda(
                "Este documento es la representación gráfica de una Factura Electrónica de Venta.\n" +
                "El valor legal recae en el XML firmado digitalmente.\n" +
                "Decreto 358/2020 · Resolución DIAN 000042/2020 · República de Colombia", false));
            pie.addCell(pieCelda("Generado: " + fecha + "\nSistema: DIANSOAP v1.0\nRepública de Colombia", true));

            doc.add(pie);
            doc.close();
            return out.toByteArray();

        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible generar el PDF", ex);
        }
    }

    // ── HELPERS ──────────────────────────────────────────────────────────

    private PdfPCell buildCard(String titulo, String[][] datos) {
        PdfPCell card = new PdfPCell();
        card.setBorder(Rectangle.BOX);
        card.setBorderColor(LINEA);
        card.setBorderWidth(0.8f);
        card.setPadding(0);

        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.addCell(barraSeccionCelda(titulo));

        PdfPCell body = new PdfPCell();
        body.setBorder(Rectangle.NO_BORDER);
        body.setPadding(7);

        Font fL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, DARK);
        Font fV = FontFactory.getFont(FontFactory.HELVETICA, 8, DARK);
        Paragraph p = new Paragraph();
        p.setLeading(13f);
        for (String[] fila : datos) {
            if (fila[0] != null && !fila[0].isEmpty()) {
                p.add(new Chunk(fila[0] + ": ", fL));
                p.add(new Chunk((fila[1] != null ? fila[1] : "") + "\n", fV));
            }
        }
        body.addElement(p);
        t.addCell(body);
        card.addElement(t);
        return card;
    }

    private PdfPCell barraSeccionCelda(String texto) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, WHITE);
        PdfPCell c = new PdfPCell(new Phrase(texto, f));
        c.setBackgroundColor(BARRA_BG);
        c.setPadding(4);
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private PdfPTable barraSeccionTabla(String texto) {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(4);
        t.setSpacingAfter(0);
        t.addCell(barraSeccionCelda(texto));
        return t;
    }

    private void addFilaTotal(PdfPTable table, String label, String value, boolean highlight) {
        Font lf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, highlight ? WHITE : DARK);
        Font vf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, highlight ? WHITE : DARK);
        BaseColor bg = highlight ? DIAN_GREEN : WHITE;

        PdfPCell lc = new PdfPCell(new Phrase(label, lf));
        lc.setBackgroundColor(bg);
        lc.setPadding(5);
        lc.setBorderColor(LINEA_GRAY);
        lc.setBorderWidth(0.5f);
        lc.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell vc = new PdfPCell(new Phrase(value, vf));
        vc.setBackgroundColor(bg);
        vc.setPadding(5);
        vc.setBorderColor(LINEA_GRAY);
        vc.setBorderWidth(0.5f);
        vc.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(lc);
        table.addCell(vc);
    }

    private PdfPCell pieCelda(String texto, boolean verde) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, verde ? WHITE : DARK);
        PdfPCell c = new PdfPCell(new Phrase(texto, f));
        c.setBackgroundColor(verde ? DIAN_GREEN : GRIS_FONDO);
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(7);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private String formatCurrency(BigDecimal value) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return fmt.format(value);
    }
}