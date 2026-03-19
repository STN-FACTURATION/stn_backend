package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.HistoriqueTarif;
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.service.dto.HistoriqueTarifDTO;
import sn.stn.facturation.service.dto.TarifDTO;

/**
 * Mapper for the entity {@link HistoriqueTarif} and its DTO {@link HistoriqueTarifDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueTarifMapper extends EntityMapper<HistoriqueTarifDTO, HistoriqueTarif> {
    @Mapping(target = "tarif", source = "tarif", qualifiedByName = "tarifId")
    HistoriqueTarifDTO toDto(HistoriqueTarif s);

    @Named("tarifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TarifDTO toDtoTarifId(Tarif tarif);
}
