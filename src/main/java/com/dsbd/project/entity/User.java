package com.dsbd.project.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "The email parameter must not be blank!")
    @Column(unique = true)
    private String email;

    @NotNull(message = "The password parameter must not be blank!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private BigDecimal credit;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public List<String> getRoles() {
        return roles;
    }



    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setCredit(BigDecimal credit){
        this.credit = credit;
        return this;
    }

    public User setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_purchased_trips", // Nome della tabella di giunzione
            joinColumns = @JoinColumn(name = "user_id"), // Colonna che punta all'utente
            inverseJoinColumns = @JoinColumn(name = "trip_id") // Colonna che punta al viaggio
    )
    private Set<Trip> purchasedTrips = new HashSet<>();

    // Getter e Setter per purchasedTrips (NECESSARI)
    public Set<Trip> getPurchasedTrips() {
        return purchasedTrips;
    }
    public void setPurchasedTrips(Set<Trip> purchasedTrips) {
        this.purchasedTrips = purchasedTrips;
    }

}
