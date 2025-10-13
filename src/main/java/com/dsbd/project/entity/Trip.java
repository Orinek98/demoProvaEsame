package com.dsbd.project.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String origin;

    @NotNull
    private String destination;

    @NotNull
    private LocalDateTime departureTime;

    @DecimalMin(value = "0.00", message = "Il prezzo non può essere negativo.")
    private BigDecimal price;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getOrigin() {return origin;}
    public void setOrigin(String origin) {this.origin = origin;}

    public String getDestination() {return destination;}
    public void setDestination(String destination) {this.destination = destination;}

    public LocalDateTime getDepartureTime() {return departureTime;}


    public BigDecimal getPrice() {return price;}
    public BigDecimal setPrice(BigDecimal value) {return this.price = value;}
}



