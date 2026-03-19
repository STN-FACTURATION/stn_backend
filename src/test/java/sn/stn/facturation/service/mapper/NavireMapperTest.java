package sn.stn.facturation.service.mapper;

import static sn.stn.facturation.domain.NavireAsserts.*;
import static sn.stn.facturation.domain.NavireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NavireMapperTest {

    private NavireMapper navireMapper;

    @BeforeEach
    void setUp() {
        navireMapper = new NavireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNavireSample1();
        var actual = navireMapper.toEntity(navireMapper.toDto(expected));
        assertNavireAllPropertiesEquals(expected, actual);
    }
}
