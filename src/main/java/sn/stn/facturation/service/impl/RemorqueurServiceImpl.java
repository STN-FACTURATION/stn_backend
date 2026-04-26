package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Remorqueur;
import sn.stn.facturation.repository.RemorqueurRepository;
import sn.stn.facturation.service.RemorqueurService;
import sn.stn.facturation.service.dto.RemorqueurDTO;
import sn.stn.facturation.service.mapper.RemorqueurMapper;

/**
 * Service Implementation for managing {@link sn.stn.facturation.domain.Remorqueur}.
 */
@Service
@Transactional
public class RemorqueurServiceImpl implements RemorqueurService {

    private static final Logger LOG = LoggerFactory.getLogger(RemorqueurServiceImpl.class);

    private final RemorqueurRepository remorqueurRepository;

    private final RemorqueurMapper remorqueurMapper;

    public RemorqueurServiceImpl(RemorqueurRepository remorqueurRepository, RemorqueurMapper remorqueurMapper) {
        this.remorqueurRepository = remorqueurRepository;
        this.remorqueurMapper = remorqueurMapper;
    }

    @Override
    public RemorqueurDTO save(RemorqueurDTO remorqueurDTO) {
        LOG.debug("Request to save Remorqueur : {}", remorqueurDTO);
        Remorqueur remorqueur = remorqueurMapper.toEntity(remorqueurDTO);
        remorqueur = remorqueurRepository.save(remorqueur);
        return remorqueurMapper.toDto(remorqueur);
    }

    @Override
    public RemorqueurDTO update(RemorqueurDTO remorqueurDTO) {
        LOG.debug("Request to update Remorqueur : {}", remorqueurDTO);
        Remorqueur remorqueur = remorqueurMapper.toEntity(remorqueurDTO);
        remorqueur = remorqueurRepository.save(remorqueur);
        return remorqueurMapper.toDto(remorqueur);
    }

    @Override
    public Optional<RemorqueurDTO> partialUpdate(RemorqueurDTO remorqueurDTO) {
        LOG.debug("Request to partially update Remorqueur : {}", remorqueurDTO);

        return remorqueurRepository
            .findById(remorqueurDTO.getId())
            .map(existingRemorqueur -> {
                remorqueurMapper.partialUpdate(existingRemorqueur, remorqueurDTO);

                return existingRemorqueur;
            })
            .map(remorqueurRepository::save)
            .map(remorqueurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RemorqueurDTO> findOne(Long id) {
        LOG.debug("Request to get Remorqueur : {}", id);
        return remorqueurRepository.findById(id).map(remorqueurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Remorqueur : {}", id);
        remorqueurRepository.deleteById(id);
    }
}
