package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.HistoriqueTarifAsserts.*;
import static sn.stn.facturation.domain.HistoriqueTarifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueTarifMapperTest {

    private HistoriqueTarifMapper historiqueTarifMapper;

    @BeforeEach
    void setUp() {
        historiqueTarifMapper = new HistoriqueTarifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueTarifSample1();
        var actual = historiqueTarifMapper.toEntity(historiqueTarifMapper.toDto(expected));
        assertHistoriqueTarifAllPropertiesEquals(expected, actual);
    }
}
