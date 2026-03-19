package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Supplement getSupplementSample1() {
        return new Supplement().id(1L).code("code1").libelle("libelle1");
    }

    public static Supplement getSupplementSample2() {
        return new Supplement().id(2L).code("code2").libelle("libelle2");
    }

    public static Supplement getSupplementRandomSampleGenerator() {
        return new Supplement().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
