package it.ibl.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtenteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Utente getUtenteSample1() {
        return new Utente()
            .id(1L)
            .username("username1")
            .password("password1")
            .mail("mail1")
            .mobile("mobile1")
            .facebook("facebook1")
            .google("google1")
            .instangram("instangram1")
            .provider("provider1")
            .motivoBolocco("motivoBolocco1");
    }

    public static Utente getUtenteSample2() {
        return new Utente()
            .id(2L)
            .username("username2")
            .password("password2")
            .mail("mail2")
            .mobile("mobile2")
            .facebook("facebook2")
            .google("google2")
            .instangram("instangram2")
            .provider("provider2")
            .motivoBolocco("motivoBolocco2");
    }

    public static Utente getUtenteRandomSampleGenerator() {
        return new Utente()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .mail(UUID.randomUUID().toString())
            .mobile(UUID.randomUUID().toString())
            .facebook(UUID.randomUUID().toString())
            .google(UUID.randomUUID().toString())
            .instangram(UUID.randomUUID().toString())
            .provider(UUID.randomUUID().toString())
            .motivoBolocco(UUID.randomUUID().toString());
    }
}
