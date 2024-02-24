package it.ibl.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AzioneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Azione getAzioneSample1() {
        return new Azione().id(1L).nomeAzione("nomeAzione1").descrizione("descrizione1");
    }

    public static Azione getAzioneSample2() {
        return new Azione().id(2L).nomeAzione("nomeAzione2").descrizione("descrizione2");
    }

    public static Azione getAzioneRandomSampleGenerator() {
        return new Azione()
            .id(longCount.incrementAndGet())
            .nomeAzione(UUID.randomUUID().toString())
            .descrizione(UUID.randomUUID().toString());
    }
}
