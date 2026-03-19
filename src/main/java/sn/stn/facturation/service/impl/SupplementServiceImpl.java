package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Supplement;
import sn.stn.facturation.repository.SupplementRepository;
import sn.stn.facturation.service.SupplementService;
import sn.stn.facturation.service.dto.SupplementDTO;
import sn.stn.facturation.service.mapper.SupplementMapper;

/**
 * Service Implementation for managing {@link sn.stn.facturation.domain.Supplement}.
 */
@Service
@Transactional
public class SupplementServiceImpl implements SupplementService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplementServiceImpl.class);

    private final SupplementRepository supplementRepository;

    private final SupplementMapper supplementMapper;

    public SupplementServiceImpl(SupplementRepository supplementRepository, SupplementMapper supplementMapper) {
        this.supplementRepository = supplementRepository;
        this.supplementMapper = supplementMapper;
    }

    @Override
    public SupplementDTO save(SupplementDTO supplementDTO) {
        LOG.debug("Request to save Supplement : {}", supplementDTO);
        Supplement supplement = supplementMapper.toEntity(supplementDTO);
        supplement = supplementRepository.save(supplement);
        return supplementMapper.toDto(supplement);
    }

    @Override
    public SupplementDTO update(SupplementDTO supplementDTO) {
        LOG.debug("Request to update Supplement : {}", supplementDTO);
        Supplement supplement = supplementMapper.toEntity(supplementDTO);
        supplement = supplementRepository.save(supplement);
        return supplementMapper.toDto(supplement);
    }

    @Override
    public Optional<SupplementDTO> partialUpdate(SupplementDTO supplementDTO) {
        LOG.debug("Request to partially update Supplement : {}", supplementDTO);

        return supplementRepository
            .findById(supplementDTO.getId())
            .map(existingSupplement -> {
                supplementMapper.partialUpdate(existingSupplement, supplementDTO);

                return existingSupplement;
            })
            .map(supplementRepository::save)
            .map(supplementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplementDTO> findOne(Long id) {
        LOG.debug("Request to get Supplement : {}", id);
        return supplementRepository.findById(id).map(supplementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Supplement : {}", id);
        supplementRepository.deleteById(id);
    }
}
