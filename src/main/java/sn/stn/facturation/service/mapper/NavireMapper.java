package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.service.dto.NavireDTO;

/**
 * Mapper for the entity {@link Navire} and its DTO {@link NavireDTO}.
 */
@Mapper(componentModel = "spring")
public interface NavireMapper extends EntityMapper<NavireDTO, Navire> {}
