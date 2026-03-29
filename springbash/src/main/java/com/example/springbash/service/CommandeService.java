package com.example.springbash.service;

import com.example.springbash.model.Client;
import com.example.springbash.model.Commande;
import com.example.springbash.model.LigneCommande;
import com.example.springbash.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientService clientService;

    @Transactional
    public Commande creerCommande(Long clientId, List<LigneCommande> lignes) {
        System.out.println("Création d'une commande pour le client ID: " + clientId);

        Client client = clientService.obtenirClientParId(clientId);

        Commande commande = new Commande();
        commande.setClient(client);

        // Calculer le montant total et associer les lignes
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (LigneCommande ligne : lignes) {
            // S'assurer que le sous-total est calculé
            if (ligne.getSousTotal() == null) {
                ligne.calculateSubTotal();
            }
            montantTotal = montantTotal.add(ligne.getSousTotal());
            ligne.setCommande(commande);
        }
        commande.setMontantTotal(montantTotal);
        commande.setLignesCommande(lignes);

        return commandeRepository.save(commande);
    }

    public List<Commande> obtenirCommandesParClient(Long clientId) {
        System.out.println("Récupération des commandes du client ID: " + clientId);
        return commandeRepository.findByClientId(clientId);
    }

    public Commande obtenirCommandeParId(Long id) {
        System.out.println("Récupération de la commande ID: " + id);
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
    }

    @Transactional
    public Commande mettreAJourStatutCommande(Long id, String nouveauStatut) {
        System.out.println("Mise à jour du statut de la commande ID: " + id + " vers " + nouveauStatut);

        Commande commande = obtenirCommandeParId(id);
        commande.setStatut(nouveauStatut);

        return commandeRepository.save(commande);
    }

    public List<Commande> obtenirCommandesParStatut(String statut) {
        System.out.println("Récupération des commandes avec statut: " + statut);
        return commandeRepository.findByStatut(statut);
    }

    public Page<Commande> obtenirCommandesParPage(Pageable pageable) {
        System.out.println("Récupération des commandes paginées");
        return commandeRepository.findAll(pageable);
    }

    public List<Commande> obtenirCommandesEntre(LocalDateTime debut, LocalDateTime fin) {
        System.out.println("Récupération des commandes entre " + debut + " et " + fin);
        return commandeRepository.findCommandesEntre(debut, fin);
    }

    public List<Commande> obtenirCommandesMontantSuperieur(BigDecimal montant) {
        System.out.println("Récupération des commandes avec montant > " + montant);
        return commandeRepository.findCommandesMontantSuperieur(montant);
    }

    public long compterCommandesClient(Long clientId) {
        System.out.println("Comptage des commandes du client ID: " + clientId);
        return commandeRepository.countByClientId(clientId);
    }

    @Transactional
    public void supprimerCommande(Long id) {
        System.out.println("Suppression de la commande ID: " + id);
        Commande commande = obtenirCommandeParId(id);
        commandeRepository.delete(commande);
    }

    @Transactional
    public Commande annulerCommande(Long id) {
        System.out.println("Annulation de la commande ID: " + id);
        return mettreAJourStatutCommande(id, "ANNULEE");
    }

    @Transactional
    public Commande validerCommande(Long id) {
        System.out.println("Validation de la commande ID: " + id);
        return mettreAJourStatutCommande(id, "VALIDEE");
    }

    @Transactional
    public Commande livrerCommande(Long id) {
        System.out.println("Livraison de la commande ID: " + id);
        return mettreAJourStatutCommande(id, "LIVREE");
    }
}