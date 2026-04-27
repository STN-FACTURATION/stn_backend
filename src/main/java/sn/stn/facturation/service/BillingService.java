package sn.stn.facturation.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.Mouvement;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.domain.enumeration.TypeOperation;
import sn.stn.facturation.repository.TarifRepository;
import sn.stn.facturation.domain.LigneFactureSupplement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class BillingService {

    private final Logger log = LoggerFactory.getLogger(BillingService.class);

    private final TarifRepository tarifRepository;

    public BillingService(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    /**
     * Calcule le montant HT pour un mouvement donné
     */
    public Double calculateAmount(Mouvement mouvement, Navire navire) {
        if (mouvement.getType() == TypeOperation.MISE_A_DISPOSITION) {
            double duree = mouvement.getDuree() != null ? mouvement.getDuree() : 0.0;
            return 880.0 * duree;
        }

        Optional<Tarif> tarifOpt = tarifRepository.findActiveTarifForVolume(navire.getJaugeBrute());
        if (tarifOpt.isEmpty()) {
            return 0.0;
        }

        double baseTarif = tarifOpt.orElseThrow().getPrixEuro();

        switch (mouvement.getType()) {
            case DEPLACEMENT_UNITAIRE:
                return baseTarif / 2.0;
            case ACCOSTAGE:
            case DEHALAGE:
            case SORTIE:
                return baseTarif * 1.0;
            case LARGAGE:
            case AMARRAGE:
                return baseTarif * 3.0;
            default:
                return 0.0;
        }
    }

    /**
     * Génère le libellé automatique pour un mouvement
     */
    public String generateLibelle(Mouvement mouvement, Navire navire) {
        String posteA = mouvement.getPosteA() != null ? mouvement.getPosteA() : "";
        String posteB = mouvement.getPosteB() != null ? mouvement.getPosteB() : "";
        String remorqueurNom = (mouvement.getRemorqueur() != null) ? mouvement.getRemorqueur().getNom()
                : "{Remorqueur}";
        String navireNom = (navire != null) ? navire.getNom() : "{Nom du navire}";
        Double duree = mouvement.getDuree() != null ? mouvement.getDuree() : 0.0;

        switch (mouvement.getType()) {
            case ACCOSTAGE:
                return "Accostage au Poste " + posteA;
            case SORTIE:
                return "Sortie au Poste " + posteA;
            case LARGAGE:
                return "Largage au Poste " + posteA;
            case AMARRAGE:
                return "Amarrage au Poste " + posteA;
            case DEHALAGE:
                return "Déhalage du Poste " + posteA + " au Poste " + posteB;
            case MISE_A_DISPOSITION:
                return String.format(
                        "Mise à disposition du Remorqueur %s pour le compte du navire %s pour la durée de %.2f",
                        remorqueurNom,
                        navireNom,
                        duree);
            case DEPLACEMENT_UNITAIRE:
                return "Déplacement unitaire du Remorqueur " + remorqueurNom;
            default:
                return "";
        }
    }

    /**
     * Recalcule tous les totaux d'une facture
     */
    public void recalculateTotals(Facture facture) {
        log.info("Recalculating totals for facture: {}, movements size: {}, supplements size: {}",
                facture.getNumero(),
                facture.getMouvements() != null ? facture.getMouvements().size() : "null",
                facture.getSupplements() != null ? facture.getSupplements().size() : "null");

        double totalMouvements = 0.0;
        if (facture.getMouvements() != null && !facture.getMouvements().isEmpty()) {
            totalMouvements = facture.getMouvements().stream()
                    .filter(m -> m.getMontantCalcule() != null)
                    .mapToDouble(Mouvement::getMontantCalcule)
                    .sum();
        }

        double totalSupplements = 0.0;
        if (facture.getSupplements() != null && !facture.getSupplements().isEmpty()) {
            totalSupplements = facture.getSupplements().stream()
                    .filter(s -> s.getMontantCalcule() != null)
                    .mapToDouble(LigneFactureSupplement::getMontantCalcule)
                    .sum();
        }

        log.info("Total Mouvements: {}, Total Supplements: {}", totalMouvements, totalSupplements);

        double baseTarif = 0.0;
        if (facture.getNavire() != null && facture.getNavire().getJaugeBrute() != null) {
            Optional<Tarif> tarifOpt = tarifRepository.findActiveTarifForVolume(facture.getNavire().getJaugeBrute());
            if (tarifOpt.isPresent()) {
                baseTarif = tarifOpt.orElseThrow().getPrixEuro();
            }
        }

        facture.setMontantBaseHt(baseTarif);
        facture.setMontantSupplementsHt(totalSupplements);
        facture.setMontantHt(totalMouvements + totalSupplements);

        double tauxTva = facture.getTauxTva() != null ? facture.getTauxTva() : 0.0;
        facture.setMontantTva(facture.getMontantHt() * (tauxTva / 100.0));
        facture.setMontantTtc(facture.getMontantHt() + facture.getMontantTva());
    }
}
