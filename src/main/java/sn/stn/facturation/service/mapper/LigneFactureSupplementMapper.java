package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.LigneFactureSupplement;
import sn.stn.facturation.domain.Supplement;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;
import sn.stn.facturation.service.dto.SupplementDTO;

/**
 * Mapper for the entity {@link LigneFactureSupplement} and its DTO {@link LigneFactureSupplementDTO}.
 */
@Mapper(componentModel = "spring")
public interface LigneFactureSupplementMapper extends EntityMapper<LigneFactureSupplementDTO, LigneFactureSupplement> {
    @Mapping(target = "supplement", source = "supplement", qualifiedByName = "supplementLibelle")
    @Mapping(target = "facture", source = "facture", qualifiedByName = "factureId")
    LigneFactureSupplementDTO toDto(LigneFactureSupplement s);

    @Named("supplementLibelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    SupplementDTO toDtoSupplementLibelle(Supplement supplement);

    @Named("factureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactureDTO toDtoFactureId(Facture facture);
}
