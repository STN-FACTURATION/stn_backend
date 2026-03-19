package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.LigneFactureSupplementAsserts.*;
import static sn.stn.facturation.domain.LigneFactureSupplementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LigneFactureSupplementMapperTest {

    private LigneFactureSupplementMapper ligneFactureSupplementMapper;

    @BeforeEach
    void setUp() {
        ligneFactureSupplementMapper = new LigneFactureSupplementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLigneFactureSupplementSample1();
        var actual = ligneFactureSupplementMapper.toEntity(ligneFactureSupplementMapper.toDto(expected));
        assertLigneFactureSupplementAllPropertiesEquals(expected, actual);
    }
}
