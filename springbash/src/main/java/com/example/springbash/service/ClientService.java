package com.example.springbash.service;

import com.example.springbash.model.Client;
import com.example.springbash.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Client creerClient(Client client) {
        System.out.println("Création d'un nouveau client: " + client.getEmail());

        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà");
        }

        return clientRepository.save(client);
    }

    public List<Client> obtenirTousLesClients() {
        return clientRepository.findAll();
    }

    public Page<Client> obtenirClientsParPage(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Client obtenirClientParId(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
    }

    public Client obtenirClientParEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'email: " + email));
    }

    @Transactional
    public Client mettreAJourClient(Long id, Client clientDetails) {
        System.out.println("Mise à jour du client ID: " + id);

        Client client = obtenirClientParId(id);
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setTelephone(clientDetails.getTelephone());
        client.setAdresse(clientDetails.getAdresse());
        client.setStatut(clientDetails.getStatut());
        client.setTypeClient(clientDetails.getTypeClient());

        return clientRepository.save(client);
    }

    @Transactional
    public void supprimerClient(Long id) {
        System.out.println("Suppression du client ID: " + id);

        Client client = obtenirClientParId(id);
        clientRepository.delete(client);
    }

    @Transactional
    public Client activerClient(Long id) {
        Client client = obtenirClientParId(id);
        client.setStatut("ACTIF");
        return clientRepository.save(client);
    }

    @Transactional
    public Client desactiverClient(Long id) {
        Client client = obtenirClientParId(id);
        client.setStatut("INACTIF");
        return clientRepository.save(client);
    }

    public List<Client> rechercherClients(String terme) {
        return clientRepository.rechercherParNom(terme);
    }

    public List<Client> obtenirClientsInscritsEntre(LocalDateTime debut, LocalDateTime fin) {
        return clientRepository.findClientsInscritsEntre(debut, fin);
    }

    public long compterClientsActifs() {
        return clientRepository.countByStatut("ACTIF");
    }
}