package sn.stn.facturation.service.mapper;

import org.mapstruct.*;
import sn.stn.facturation.domain.Client;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.service.dto.ClientDTO;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.dto.NavireDTO;

/**
 * Mapper for the entity {@link Facture} and its DTO {@link FactureDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactureMapper extends EntityMapper<FactureDTO, Facture> {
    @Mapping(target = "navire", source = "navire", qualifiedByName = "navireNom")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientNom")
    FactureDTO toDto(Facture s);

    @Named("navireNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    NavireDTO toDtoNavireNom(Navire navire);

    @Named("clientNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    ClientDTO toDtoClientNom(Client client);
}
