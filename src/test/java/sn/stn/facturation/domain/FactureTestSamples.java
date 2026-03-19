package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FactureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Facture getFactureSample1() {
        return new Facture().id(1L).numero("numero1").notes("notes1").cheminPdf("cheminPdf1").creeParLogin("creeParLogin1");
    }

    public static Facture getFactureSample2() {
        return new Facture().id(2L).numero("numero2").notes("notes2").cheminPdf("cheminPdf2").creeParLogin("creeParLogin2");
    }

    public static Facture getFactureRandomSampleGenerator() {
        return new Facture()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString())
            .cheminPdf(UUID.randomUUID().toString())
            .creeParLogin(UUID.randomUUID().toString());
    }
}
