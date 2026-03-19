package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Client;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.service.dto.ClientDTO;
import sn.stn.facturation.service.dto.NavireDTO;

/**
 * Mapper for the entity {@link Navire} and its DTO {@link NavireDTO}.
 */
@Mapper(componentModel = "spring")
public interface NavireMapper extends EntityMapper<NavireDTO, Navire> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientNom")
    NavireDTO toDto(Navire s);

    @Named("clientNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    ClientDTO toDtoClientNom(Client client);
}
