package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.service.dto.TarifDTO;

/**
 * Mapper for the entity {@link Tarif} and its DTO {@link TarifDTO}.
 */
@Mapper(componentModel = "spring")
public interface TarifMapper extends EntityMapper<TarifDTO, Tarif> {}
