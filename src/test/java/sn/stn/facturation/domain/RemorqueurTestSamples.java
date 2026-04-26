package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RemorqueurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Remorqueur getRemorqueurSample1() {
        return new Remorqueur().id(1L).code("code1").nom("nom1").statut("statut1").observation("observation1");
    }

    public static Remorqueur getRemorqueurSample2() {
        return new Remorqueur().id(2L).code("code2").nom("nom2").statut("statut2").observation("observation2");
    }

    public static Remorqueur getRemorqueurRandomSampleGenerator() {
        return new Remorqueur()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString())
            .observation(UUID.randomUUID().toString());
    }
}
