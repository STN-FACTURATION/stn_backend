package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Remorqueur;
import sn.stn.facturation.service.dto.RemorqueurDTO;

/**
 * Mapper for the entity {@link Remorqueur} and its DTO {@link RemorqueurDTO}.
 */
@Mapper(componentModel = "spring")
public interface RemorqueurMapper extends EntityMapper<RemorqueurDTO, Remorqueur> {}
