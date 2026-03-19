package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.TarifAsserts.*;
import static sn.stn.facturation.domain.TarifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TarifMapperTest {

    private TarifMapper tarifMapper;

    @BeforeEach
    void setUp() {
        tarifMapper = new TarifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTarifSample1();
        var actual = tarifMapper.toEntity(tarifMapper.toDto(expected));
        assertTarifAllPropertiesEquals(expected, actual);
    }
}
