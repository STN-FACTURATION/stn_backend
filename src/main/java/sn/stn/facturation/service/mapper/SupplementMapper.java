package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Supplement;
import sn.stn.facturation.service.dto.SupplementDTO;

/**
 * Mapper for the entity {@link Supplement} and its DTO {@link SupplementDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplementMapper extends EntityMapper<SupplementDTO, Supplement> {}
