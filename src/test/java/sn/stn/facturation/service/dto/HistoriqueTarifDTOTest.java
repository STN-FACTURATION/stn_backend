package sn.stn.facturation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class HistoriqueTarifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueTarifDTO.class);
        HistoriqueTarifDTO historiqueTarifDTO1 = new HistoriqueTarifDTO();
        historiqueTarifDTO1.setId(1L);
        HistoriqueTarifDTO historiqueTarifDTO2 = new HistoriqueTarifDTO();
        assertThat(historiqueTarifDTO1).isNotEqualTo(historiqueTarifDTO2);
        historiqueTarifDTO2.setId(historiqueTarifDTO1.getId());
        assertThat(historiqueTarifDTO1).isEqualTo(historiqueTarifDTO2);
        historiqueTarifDTO2.setId(2L);
        assertThat(historiqueTarifDTO1).isNotEqualTo(historiqueTarifDTO2);
        historiqueTarifDTO1.setId(null);
        assertThat(historiqueTarifDTO1).isNotEqualTo(historiqueTarifDTO2);
    }
}
