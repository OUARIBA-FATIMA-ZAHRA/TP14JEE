package com.example.springbash.controller;

import com.example.springbash.model.Commande;
import com.example.springbash.model.LigneCommande;
import com.example.springbash.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @GetMapping
    public ResponseEntity<Page<Commande>> getAllCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCommande").descending());
        return ResponseEntity.ok(commandeService.obtenirCommandesParPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.obtenirCommandeParId(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Commande>> getCommandesByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.obtenirCommandesParClient(clientId));
    }

    @PostMapping("/client/{clientId}")
    public ResponseEntity<Commande> createCommande(
            @PathVariable Long clientId,
            @RequestBody List<LigneCommande> lignes) {
        Commande nouvelleCommande = commandeService.creerCommande(clientId, lignes);
        return new ResponseEntity<>(nouvelleCommande, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Commande> updateCommandeStatut(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String nouveauStatut = payload.get("statut");
        return ResponseEntity.ok(commandeService.mettreAJourStatutCommande(id, nouveauStatut));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Commande>> getCommandesByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(commandeService.obtenirCommandesParStatut(statut));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<Commande>> getCommandesEntre(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(commandeService.obtenirCommandesEntre(debut, fin));
    }

    @GetMapping("/montant-superieur")
    public ResponseEntity<List<Commande>> getCommandesMontantSuperieur(@RequestParam BigDecimal montant) {
        return ResponseEntity.ok(commandeService.obtenirCommandesMontantSuperieur(montant));
    }

    @GetMapping("/client/{clientId}/nombre")
    public ResponseEntity<Map<String, Long>> getNombreCommandesClient(@PathVariable Long clientId) {
        Map<String, Long> response = Map.of("nombreCommandes", commandeService.compterCommandesClient(clientId));
        return ResponseEntity.ok(response);
    }
}