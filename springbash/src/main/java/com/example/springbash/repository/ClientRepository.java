package com.example.springbash.repository;

import com.example.springbash.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByStatut(String statut);

    List<Client> findByTypeClient(String typeClient);

    @Query("SELECT c FROM Client c WHERE c.nom LIKE %:nom% OR c.prenom LIKE %:nom%")
    List<Client> rechercherParNom(@Param("nom") String nom);

    @Query("SELECT c FROM Client c WHERE c.dateInscription BETWEEN :debut AND :fin")
    List<Client> findClientsInscritsEntre(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    Page<Client> findByStatut(String statut, Pageable pageable);

    long countByStatut(String statut);

    boolean existsByEmail(String email);
}