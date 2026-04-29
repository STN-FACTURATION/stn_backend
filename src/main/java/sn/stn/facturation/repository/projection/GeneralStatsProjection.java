package sn.stn.facturation.repository.projection;

public interface GeneralStatsProjection {
    Double getBilledThisMonth();
    Double getPaidThisMonth();
    Double getTotalVolume();
    Double getBilledLastMonth();
}
