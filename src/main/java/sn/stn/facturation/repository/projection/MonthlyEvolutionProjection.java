package sn.stn.facturation.repository.projection;

public interface MonthlyEvolutionProjection {
    String getMonth();
    Double getBilled();
    Double getPaid();
    Long getCount();
}
