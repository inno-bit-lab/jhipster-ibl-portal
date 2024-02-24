package it.ibl.portal.domain;

import static it.ibl.portal.domain.AzioneTestSamples.*;
import static it.ibl.portal.domain.RuoloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.ibl.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AzioneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Azione.class);
        Azione azione1 = getAzioneSample1();
        Azione azione2 = new Azione();
        assertThat(azione1).isNotEqualTo(azione2);

        azione2.setId(azione1.getId());
        assertThat(azione1).isEqualTo(azione2);

        azione2 = getAzioneSample2();
        assertThat(azione1).isNotEqualTo(azione2);
    }

    @Test
    void ruoloTest() throws Exception {
        Azione azione = getAzioneRandomSampleGenerator();
        Ruolo ruoloBack = getRuoloRandomSampleGenerator();

        azione.addRuolo(ruoloBack);
        assertThat(azione.getRuolos()).containsOnly(ruoloBack);
        assertThat(ruoloBack.getAzioni()).isEqualTo(azione);

        azione.removeRuolo(ruoloBack);
        assertThat(azione.getRuolos()).doesNotContain(ruoloBack);
        assertThat(ruoloBack.getAzioni()).isNull();

        azione.ruolos(new HashSet<>(Set.of(ruoloBack)));
        assertThat(azione.getRuolos()).containsOnly(ruoloBack);
        assertThat(ruoloBack.getAzioni()).isEqualTo(azione);

        azione.setRuolos(new HashSet<>());
        assertThat(azione.getRuolos()).doesNotContain(ruoloBack);
        assertThat(ruoloBack.getAzioni()).isNull();
    }
}
