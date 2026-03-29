package com.example.springbash.repository;

import com.example.springbash.model.Commande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByClientId(Long clientId);

    List<Commande> findByStatut(String statut);

    @Query("SELECT c FROM Commande c WHERE c.dateCommande BETWEEN :debut AND :fin")
    List<Commande> findCommandesEntre(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT c FROM Commande c WHERE c.montantTotal > :montant")
    List<Commande> findCommandesMontantSuperieur(@Param("montant") BigDecimal montant);

    @Query("SELECT COUNT(c) FROM Commande c WHERE c.client.id = :clientId")
    long countByClientId(@Param("clientId") Long clientId);

    Page<Commande> findByStatut(String statut, Pageable pageable);
}