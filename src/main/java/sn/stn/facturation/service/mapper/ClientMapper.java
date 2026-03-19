package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Client;
import sn.stn.facturation.service.dto.ClientDTO;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
