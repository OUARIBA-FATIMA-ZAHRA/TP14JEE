package com.example.springbash.controller;

import com.example.springbash.model.Client;
import com.example.springbash.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<Client>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(clientService.obtenirClientsParPage(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClientsList() {
        return ResponseEntity.ok(clientService.obtenirTousLesClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.obtenirClientParId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.obtenirClientParEmail(email));
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        Client nouveauClient = clientService.creerClient(client);
        return new ResponseEntity<>(nouveauClient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.mettreAJourClient(id, client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteClient(@PathVariable Long id) {
        clientService.supprimerClient(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("supprime", true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Client> activerClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.activerClient(id));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Client> desactiverClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.desactiverClient(id));
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<Client>> rechercherClients(@RequestParam String terme) {
        return ResponseEntity.ok(clientService.rechercherClients(terme));
    }

    @GetMapping("/statistiques/actifs")
    public ResponseEntity<Map<String, Long>> getStatistiquesClientsActifs() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("clientsActifs", clientService.compterClientsActifs());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/inscriptions")
    public ResponseEntity<List<Client>> getClientsInscritsEntre(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(clientService.obtenirClientsInscritsEntre(debut, fin));
    }
}