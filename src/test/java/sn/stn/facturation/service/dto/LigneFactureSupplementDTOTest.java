package sn.stn.facturation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class LigneFactureSupplementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneFactureSupplementDTO.class);
        LigneFactureSupplementDTO ligneFactureSupplementDTO1 = new LigneFactureSupplementDTO();
        ligneFactureSupplementDTO1.setId(1L);
        LigneFactureSupplementDTO ligneFactureSupplementDTO2 = new LigneFactureSupplementDTO();
        assertThat(ligneFactureSupplementDTO1).isNotEqualTo(ligneFactureSupplementDTO2);
        ligneFactureSupplementDTO2.setId(ligneFactureSupplementDTO1.getId());
        assertThat(ligneFactureSupplementDTO1).isEqualTo(ligneFactureSupplementDTO2);
        ligneFactureSupplementDTO2.setId(2L);
        assertThat(ligneFactureSupplementDTO1).isNotEqualTo(ligneFactureSupplementDTO2);
        ligneFactureSupplementDTO1.setId(null);
        assertThat(ligneFactureSupplementDTO1).isNotEqualTo(ligneFactureSupplementDTO2);
    }
}
