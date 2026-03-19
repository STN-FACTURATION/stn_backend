package sn.stn.facturation.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .numero("numero1")
            .nom("nom1")
            .adresse("adresse1")
            .email("email1")
            .telephone("telephone1")
            .ville("ville1")
            .pays("pays1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .numero("numero2")
            .nom("nom2")
            .adresse("adresse2")
            .email("email2")
            .telephone("telephone2")
            .ville("ville2")
            .pays("pays2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .pays(UUID.randomUUID().toString());
    }
}
