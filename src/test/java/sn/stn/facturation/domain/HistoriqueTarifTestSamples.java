package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriqueTarifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoriqueTarif getHistoriqueTarifSample1() {
        return new HistoriqueTarif().id(1L).commentaire("commentaire1").modifieParLogin("modifieParLogin1");
    }

    public static HistoriqueTarif getHistoriqueTarifSample2() {
        return new HistoriqueTarif().id(2L).commentaire("commentaire2").modifieParLogin("modifieParLogin2");
    }

    public static HistoriqueTarif getHistoriqueTarifRandomSampleGenerator() {
        return new HistoriqueTarif()
            .id(longCount.incrementAndGet())
            .commentaire(UUID.randomUUID().toString())
            .modifieParLogin(UUID.randomUUID().toString());
    }
}
