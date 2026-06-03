package com.dian.soap.DianSoap.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "facturas")
public class InvoiceSaved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFactura;
    private LocalDateTime fechaEmision;

    // Vendedor
    private String identificacionVendedor;
    private String nombreVendedor;
    private String actividadProductiva;
    private String telefonoVendedor;
    private String correoVendedor;

    // Producto
    private String productoVendido;
    private String codigoProducto;
    private BigDecimal precioUnitario;
    private Integer cantidadVendida;

    // Cliente
    private String nombreCliente;
    private String identificacionCliente;
    private String telefonoCliente;
    private String correoCliente;

    // Totales calculados
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;

    private String estado;

    // ── Getters y Setters ──────────────────────────

    public Long getId() { return id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getIdentificacionVendedor() { return identificacionVendedor; }
    public void setIdentificacionVendedor(String v) { this.identificacionVendedor = v; }

    public String getNombreVendedor() { return nombreVendedor; }
    public void setNombreVendedor(String v) { this.nombreVendedor = v; }

    public String getActividadProductiva() { return actividadProductiva; }
    public void setActividadProductiva(String v) { this.actividadProductiva = v; }

    public String getTelefonoVendedor() { return telefonoVendedor; }
    public void setTelefonoVendedor(String v) { this.telefonoVendedor = v; }

    public String getCorreoVendedor() { return correoVendedor; }
    public void setCorreoVendedor(String v) { this.correoVendedor = v; }

    public String getProductoVendido() { return productoVendido; }
    public void setProductoVendido(String v) { this.productoVendido = v; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String v) { this.codigoProducto = v; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal v) { this.precioUnitario = v; }

    public Integer getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Integer v) { this.cantidadVendida = v; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String v) { this.nombreCliente = v; }

    public String getIdentificacionCliente() { return identificacionCliente; }
    public void setIdentificacionCliente(String v) { this.identificacionCliente = v; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String v) { this.telefonoCliente = v; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String v) { this.correoCliente = v; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal v) { this.subtotal = v; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal v) { this.iva = v; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal v) { this.total = v; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
