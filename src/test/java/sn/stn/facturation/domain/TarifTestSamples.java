package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TarifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tarif getTarifSample1() {
        return new Tarif().id(1L).description("description1");
    }

    public static Tarif getTarifSample2() {
        return new Tarif().id(2L).description("description2");
    }

    public static Tarif getTarifRandomSampleGenerator() {
        return new Tarif().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
