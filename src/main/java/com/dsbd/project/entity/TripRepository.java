package com.dsbd.project.entity;

import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface TripRepository  extends CrudRepository<Trip, Integer> {
    Optional<Trip> findById(Integer id);
    // Metodo per trovare i viaggi con prezzo massimo (e ordinare)
// 1. Filtro solo per Destinazione (e Ordinamento)
    List<Trip> findByDestination(String destination, Sort sort);

    // 2. Filtro solo per Prezzo Massimo (e Ordinamento)
    // Usiamo LessThanEqual sul campo 'price' che è un BigDecimal
    List<Trip> findByPriceLessThanEqual(BigDecimal maxPrice, Sort sort);

    // 3. Filtro per Destinazione E Prezzo Massimo (e Ordinamento)
    List<Trip> findByDestinationAndPriceLessThanEqual(String destination, BigDecimal maxPrice, Sort sort);

    // 4. Filtro per Origine E Destinazione
    List<Trip> findByOriginAndDestination(String origin, String destination, Sort sort);

    // 5. Metodo base per recuperare tutti i viaggi con ordinamento
    Iterable<Trip> findAll(Sort sort);
}
