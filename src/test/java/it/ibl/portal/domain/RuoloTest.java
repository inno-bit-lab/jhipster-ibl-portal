package it.ibl.portal.domain;

import static it.ibl.portal.domain.AzioneTestSamples.*;
import static it.ibl.portal.domain.RuoloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.ibl.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RuoloTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ruolo.class);
        Ruolo ruolo1 = getRuoloSample1();
        Ruolo ruolo2 = new Ruolo();
        assertThat(ruolo1).isNotEqualTo(ruolo2);

        ruolo2.setId(ruolo1.getId());
        assertThat(ruolo1).isEqualTo(ruolo2);

        ruolo2 = getRuoloSample2();
        assertThat(ruolo1).isNotEqualTo(ruolo2);
    }

    @Test
    void azioniTest() throws Exception {
        Ruolo ruolo = getRuoloRandomSampleGenerator();
        Azione azioneBack = getAzioneRandomSampleGenerator();

        ruolo.setAzioni(azioneBack);
        assertThat(ruolo.getAzioni()).isEqualTo(azioneBack);

        ruolo.azioni(null);
        assertThat(ruolo.getAzioni()).isNull();
    }
}
