package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.repository.NavireRepository;
import sn.stn.facturation.service.NavireService;
import sn.stn.facturation.service.dto.NavireDTO;
import sn.stn.facturation.service.mapper.NavireMapper;

/**
 * Service Implementation for managing {@link sn.stn.facturation.domain.Navire}.
 */
@Service
@Transactional
public class NavireServiceImpl implements NavireService {

    private static final Logger LOG = LoggerFactory.getLogger(NavireServiceImpl.class);

    private final NavireRepository navireRepository;

    private final NavireMapper navireMapper;

    public NavireServiceImpl(NavireRepository navireRepository, NavireMapper navireMapper) {
        this.navireRepository = navireRepository;
        this.navireMapper = navireMapper;
    }

    @Override
    public NavireDTO save(NavireDTO navireDTO) {
        LOG.debug("Request to save Navire : {}", navireDTO);
        Navire navire = navireMapper.toEntity(navireDTO);
        navire = navireRepository.save(navire);
        return navireMapper.toDto(navire);
    }

    @Override
    public NavireDTO update(NavireDTO navireDTO) {
        LOG.debug("Request to update Navire : {}", navireDTO);
        Navire navire = navireMapper.toEntity(navireDTO);
        navire = navireRepository.save(navire);
        return navireMapper.toDto(navire);
    }

    @Override
    public Optional<NavireDTO> partialUpdate(NavireDTO navireDTO) {
        LOG.debug("Request to partially update Navire : {}", navireDTO);

        return navireRepository
            .findById(navireDTO.getId())
            .map(existingNavire -> {
                navireMapper.partialUpdate(existingNavire, navireDTO);

                return existingNavire;
            })
            .map(navireRepository::save)
            .map(navireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NavireDTO> findOne(Long id) {
        LOG.debug("Request to get Navire : {}", id);
        return navireRepository.findById(id).map(navireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Navire : {}", id);
        navireRepository.deleteById(id);
    }
}
