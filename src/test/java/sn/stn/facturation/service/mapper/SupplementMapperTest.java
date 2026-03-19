package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.SupplementAsserts.*;
import static sn.stn.facturation.domain.SupplementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplementMapperTest {

    private SupplementMapper supplementMapper;

    @BeforeEach
    void setUp() {
        supplementMapper = new SupplementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplementSample1();
        var actual = supplementMapper.toEntity(supplementMapper.toDto(expected));
        assertSupplementAllPropertiesEquals(expected, actual);
    }
}
