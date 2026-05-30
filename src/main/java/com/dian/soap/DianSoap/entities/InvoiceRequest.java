package com.dian.soap.DianSoap.entities;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvoiceRequest")
@XmlRootElement(name = "InvoiceRequest", namespace = "http://dian.soap/Factura")
public class InvoiceRequest {

    @XmlElement(name = "IdentificacionVendedor", required = true)
    private String identificacionVendedor;

    @XmlElement(name = "NombreVendedor", required = true)
    private String nombreVendedor;

    @XmlElement(name = "ActividadProductiva", required = true)
    private String actividadProductiva;

    @XmlElement(name = "TelefonoVendedor", required = true)
    private String telefonoVendedor;

    @XmlElement(name = "CorreoVendedor", required = true)
    private String correoVendedor;

    @XmlElement(name = "ProductoVendido", required = true)
    private String productoVendido;

    @XmlElement(name = "CodigoProducto", required = true)
    private String codigoProducto;

    @XmlElement(name = "PrecioUnitario", required = true)
    private BigDecimal precioUnitario;

    @XmlElement(name = "CantidadVendida", required = true)
    private Integer cantidadVendida;

    @XmlElement(name = "NombreCliente", required = true)
    private String nombreCliente;

    @XmlElement(name = "IdentificacionCliente", required = true)
    private String identificacionCliente;

    @XmlElement(name = "TelefonoCliente", required = true)
    private String telefonoCliente;

    @XmlElement(name = "CorreoCliente", required = true)
    private String correoCliente;

    public String getIdentificacionVendedor() {
        return identificacionVendedor;
    }

    public void setIdentificacionVendedor(String identificacionVendedor) {
        this.identificacionVendedor = identificacionVendedor;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getActividadProductiva() {
        return actividadProductiva;
    }

    public void setActividadProductiva(String actividadProductiva) {
        this.actividadProductiva = actividadProductiva;
    }

    public String getTelefonoVendedor() {
        return telefonoVendedor;
    }

    public void setTelefonoVendedor(String telefonoVendedor) {
        this.telefonoVendedor = telefonoVendedor;
    }

    public String getCorreoVendedor() {
        return correoVendedor;
    }

    public void setCorreoVendedor(String correoVendedor) {
        this.correoVendedor = correoVendedor;
    }

    public String getProductoVendido() {
        return productoVendido;
    }

    public void setProductoVendido(String productoVendido) {
        this.productoVendido = productoVendido;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(Integer cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }
}
