package sn.stn.facturation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class NavireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NavireDTO.class);
        NavireDTO navireDTO1 = new NavireDTO();
        navireDTO1.setId(1L);
        NavireDTO navireDTO2 = new NavireDTO();
        assertThat(navireDTO1).isNotEqualTo(navireDTO2);
        navireDTO2.setId(navireDTO1.getId());
        assertThat(navireDTO1).isEqualTo(navireDTO2);
        navireDTO2.setId(2L);
        assertThat(navireDTO1).isNotEqualTo(navireDTO2);
        navireDTO1.setId(null);
        assertThat(navireDTO1).isNotEqualTo(navireDTO2);
    }
}
