package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LigneFactureSupplementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LigneFactureSupplement getLigneFactureSupplementSample1() {
        return new LigneFactureSupplement().id(1L).description("description1");
    }

    public static LigneFactureSupplement getLigneFactureSupplementSample2() {
        return new LigneFactureSupplement().id(2L).description("description2");
    }

    public static LigneFactureSupplement getLigneFactureSupplementRandomSampleGenerator() {
        return new LigneFactureSupplement().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
