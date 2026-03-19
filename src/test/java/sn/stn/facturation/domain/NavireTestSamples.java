package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NavireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Navire getNavireSample1() {
        return new Navire().id(1L).nom("nom1").numeroImo("numeroImo1").pavillon("pavillon1");
    }

    public static Navire getNavireSample2() {
        return new Navire().id(2L).nom("nom2").numeroImo("numeroImo2").pavillon("pavillon2");
    }

    public static Navire getNavireRandomSampleGenerator() {
        return new Navire()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .numeroImo(UUID.randomUUID().toString())
            .pavillon(UUID.randomUUID().toString());
    }
}
