package sn.stn.facturation.service.dto;

import java.io.Serializable;
import java.util.List;

public class DashboardStatsDTO implements Serializable {

    private Double billedThisMonth;
    private Double paidThisMonth;
    private Double totalVolume;
    private Double billedLastMonth;
    private Double recoveryRate;
    private List<ClientStat> topClients;
    private List<MonthlyStat> monthlyEvolution;

    // Getters and Setters
    public Double getBilledThisMonth() { return billedThisMonth; }
    public void setBilledThisMonth(Double billedThisMonth) { this.billedThisMonth = billedThisMonth; }

    public Double getPaidThisMonth() { return paidThisMonth; }
    public void setPaidThisMonth(Double paidThisMonth) { this.paidThisMonth = paidThisMonth; }

    public Double getTotalVolume() { return totalVolume; }
    public void setTotalVolume(Double totalVolume) { this.totalVolume = totalVolume; }

    public Double getBilledLastMonth() { return billedLastMonth; }
    public void setBilledLastMonth(Double billedLastMonth) { this.billedLastMonth = billedLastMonth; }

    public Double getRecoveryRate() { return recoveryRate; }
    public void setRecoveryRate(Double recoveryRate) { this.recoveryRate = recoveryRate; }

    public List<ClientStat> getTopClients() { return topClients; }
    public void setTopClients(List<ClientStat> topClients) { this.topClients = topClients; }

    public List<MonthlyStat> getMonthlyEvolution() { return monthlyEvolution; }
    public void setMonthlyEvolution(List<MonthlyStat> monthlyEvolution) { this.monthlyEvolution = monthlyEvolution; }

    public static class ClientStat implements Serializable {
        private String name;
        private Double amount;
        private Long count;

        public ClientStat(String name, Double amount, Long count) {
            this.name = name;
            this.amount = amount;
            this.count = count;
        }

        public String getName() { return name; }
        public Double getAmount() { return amount; }
        public Long getCount() { return count; }
    }

    public static class MonthlyStat implements Serializable {
        private String month;
        private Double billed;
        private Double paid;
        private Long count;

        public MonthlyStat(String month, Double billed, Double paid, Long count) {
            this.month = month;
            this.billed = billed;
            this.paid = paid;
            this.count = count;
        }

        public String getMonth() { return month; }
        public Double getBilled() { return billed; }
        public Double getPaid() { return paid; }
        public Long getCount() { return count; }
    }
}
