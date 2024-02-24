package it.ibl.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RuoloTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ruolo getRuoloSample1() {
        return new Ruolo().id(1L).nomeAzione("nomeAzione1");
    }

    public static Ruolo getRuoloSample2() {
        return new Ruolo().id(2L).nomeAzione("nomeAzione2");
    }

    public static Ruolo getRuoloRandomSampleGenerator() {
        return new Ruolo().id(longCount.incrementAndGet()).nomeAzione(UUID.randomUUID().toString());
    }
}
