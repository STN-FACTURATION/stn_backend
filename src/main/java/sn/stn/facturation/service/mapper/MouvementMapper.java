package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.Mouvement;
import sn.stn.facturation.domain.Remorqueur;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.dto.MouvementDTO;
import sn.stn.facturation.service.dto.RemorqueurDTO;

/**
 * Mapper for the entity {@link Mouvement} and its DTO {@link MouvementDTO}.
 */
@Mapper(componentModel = "spring")
public interface MouvementMapper extends EntityMapper<MouvementDTO, Mouvement> {
    @Mapping(target = "remorqueur", source = "remorqueur", qualifiedByName = "remorqueurNom")
    @Mapping(target = "facture", source = "facture", qualifiedByName = "factureNumero")
    MouvementDTO toDto(Mouvement s);

    @Named("remorqueurNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    RemorqueurDTO toDtoRemorqueurNom(Remorqueur remorqueur);

    @Named("factureNumero")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    FactureDTO toDtoFactureNumero(Facture facture);
}
