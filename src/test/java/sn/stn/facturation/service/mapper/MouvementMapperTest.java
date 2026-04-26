package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.MouvementAsserts.*;
import static sn.stn.facturation.domain.MouvementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MouvementMapperTest {

    private MouvementMapper mouvementMapper;

    @BeforeEach
    void setUp() {
        mouvementMapper = new MouvementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMouvementSample1();
        var actual = mouvementMapper.toEntity(mouvementMapper.toDto(expected));
        assertMouvementAllPropertiesEquals(expected, actual);
    }
}
