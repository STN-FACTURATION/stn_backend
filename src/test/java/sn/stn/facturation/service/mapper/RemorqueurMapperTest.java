package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.RemorqueurAsserts.*;
import static sn.stn.facturation.domain.RemorqueurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemorqueurMapperTest {

    private RemorqueurMapper remorqueurMapper;

    @BeforeEach
    void setUp() {
        remorqueurMapper = new RemorqueurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRemorqueurSample1();
        var actual = remorqueurMapper.toEntity(remorqueurMapper.toDto(expected));
        assertRemorqueurAllPropertiesEquals(expected, actual);
    }
}
