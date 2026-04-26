package sn.stn.facturation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class RemorqueurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RemorqueurDTO.class);
        RemorqueurDTO remorqueurDTO1 = new RemorqueurDTO();
        remorqueurDTO1.setId(1L);
        RemorqueurDTO remorqueurDTO2 = new RemorqueurDTO();
        assertThat(remorqueurDTO1).isNotEqualTo(remorqueurDTO2);
        remorqueurDTO2.setId(remorqueurDTO1.getId());
        assertThat(remorqueurDTO1).isEqualTo(remorqueurDTO2);
        remorqueurDTO2.setId(2L);
        assertThat(remorqueurDTO1).isNotEqualTo(remorqueurDTO2);
        remorqueurDTO1.setId(null);
        assertThat(remorqueurDTO1).isNotEqualTo(remorqueurDTO2);
    }
}
