package com.oip.helpdesk.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reporte_area_status")
public class DataReporte {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "parameter_name")
    private String parameter_name;
    @Column(name = "status_name")
    private String status_name;
    @Column(name = "cantidad")
    private Integer cantidad;

    public String getParameter() {
        return parameter_name;
    }

    public void setParameter(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    public String getStatus() {
        return status_name;
    }

    public void setStatus(String status_name) {
        this.status_name = status_name;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
