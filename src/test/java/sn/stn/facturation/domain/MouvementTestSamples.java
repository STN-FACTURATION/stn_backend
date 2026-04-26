package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MouvementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Mouvement getMouvementSample1() {
        return new Mouvement().id(1L).posteA("posteA1").posteB("posteB1").libelle("libelle1");
    }

    public static Mouvement getMouvementSample2() {
        return new Mouvement().id(2L).posteA("posteA2").posteB("posteB2").libelle("libelle2");
    }

    public static Mouvement getMouvementRandomSampleGenerator() {
        return new Mouvement()
            .id(longCount.incrementAndGet())
            .posteA(UUID.randomUUID().toString())
            .posteB(UUID.randomUUID().toString())
            .libelle(UUID.randomUUID().toString());
    }
}
