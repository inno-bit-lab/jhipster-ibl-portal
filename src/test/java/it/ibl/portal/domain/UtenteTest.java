package it.ibl.portal.domain;

import static it.ibl.portal.domain.RuoloTestSamples.*;
import static it.ibl.portal.domain.UtenteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.ibl.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtenteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utente.class);
        Utente utente1 = getUtenteSample1();
        Utente utente2 = new Utente();
        assertThat(utente1).isNotEqualTo(utente2);

        utente2.setId(utente1.getId());
        assertThat(utente1).isEqualTo(utente2);

        utente2 = getUtenteSample2();
        assertThat(utente1).isNotEqualTo(utente2);
    }

    @Test
    void ruoloTest() throws Exception {
        Utente utente = getUtenteRandomSampleGenerator();
        Ruolo ruoloBack = getRuoloRandomSampleGenerator();

        utente.setRuolo(ruoloBack);
        assertThat(utente.getRuolo()).isEqualTo(ruoloBack);

        utente.ruolo(null);
        assertThat(utente.getRuolo()).isNull();
    }
}
