package com.movie.data.domain;


import com.movie.common.core.domain.BaseEntity;

public class AliPay extends BaseEntity {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAlipayTraceNo() {
        return alipayTraceNo;
    }

    public void setAlipayTraceNo(String alipayTraceNo) {
        this.alipayTraceNo = alipayTraceNo;
    }

    @Override
    public String toString() {
        return "AliPay{" +
                "traceNo='" + traceNo + '\'' +
                ", totalAmount=" + totalAmount +
                ", subject='" + subject + '\'' +
                ", alipayTraceNo='" + alipayTraceNo + '\'' +
                '}';
    }
}
