package com.mtsealove.github.ohbody.Database;

import java.io.Serializable;

public class HealthFood implements Serializable {
    String PRMS_DT; //인정일자
    String BSSH_NM; //업체명
    String APLC_RAWMTRL_NM; //신청원료명
    String HF_FNCLTY_MTRAL_RCOGN_NO;    //인정번호
    String INDUTY_NM;   //업종
    String IFTKN_ATNT_MATR_CN;  //섭취시 주의사항
    String ADDR;    //주소
    String FNCLTY_CN;   //기능성 내용
    String DAY_INTK_CN; //1일 섭취량
    String Date;

    public HealthFood(String PRMS_DT, String BSSH_NM, String APLC_RAWMTRL_NM, String HF_FNCLTY_MTRAL_RCOGN_NO, String INDUTY_NM, String IFTKN_ATNT_MATR_CN, String ADDR, String FNCLTY_CN, String DAY_INTK_CN, String date) {
        this.PRMS_DT = PRMS_DT.replace("'", "");
        this.BSSH_NM = BSSH_NM.replace("'", "");
        this.APLC_RAWMTRL_NM = APLC_RAWMTRL_NM.replace("'", "");
        this.HF_FNCLTY_MTRAL_RCOGN_NO = HF_FNCLTY_MTRAL_RCOGN_NO.replace("'", "");
        this.INDUTY_NM = INDUTY_NM.replace("'", "");
        this.IFTKN_ATNT_MATR_CN = IFTKN_ATNT_MATR_CN.replace("'", "");
        this.ADDR = ADDR.replace("'", "");
        this.FNCLTY_CN = FNCLTY_CN.replace("'", "");
        this.DAY_INTK_CN = DAY_INTK_CN.replace("'", "");
        this.Date = date;

    }

    public HealthFood() {
    }

    public String getPRMS_DT() {
        return PRMS_DT;
    }

    public void setPRMS_DT(String PRMS_DT) {
        this.PRMS_DT = PRMS_DT;
    }

    public String getBSSH_NM() {
        return BSSH_NM;
    }

    public void setBSSH_NM(String BSSH_NM) {
        this.BSSH_NM = BSSH_NM;
    }

    public String getAPLC_RAWMTRL_NM() {
        return APLC_RAWMTRL_NM;
    }

    public void setAPLC_RAWMTRL_NM(String APLC_RAWMTRL_NM) {
        this.APLC_RAWMTRL_NM = APLC_RAWMTRL_NM;
    }

    public String getHF_FNCLTY_MTRAL_RCOGN_NO() {
        return HF_FNCLTY_MTRAL_RCOGN_NO;
    }

    public void setHF_FNCLTY_MTRAL_RCOGN_NO(String HF_FNCLTY_MTRAL_RCOGN_NO) {
        this.HF_FNCLTY_MTRAL_RCOGN_NO = HF_FNCLTY_MTRAL_RCOGN_NO;
    }

    public String getINDUTY_NM() {
        return INDUTY_NM;
    }

    public void setINDUTY_NM(String INDUTY_NM) {
        this.INDUTY_NM = INDUTY_NM;
    }

    public String getIFTKN_ATNT_MATR_CN() {
        return IFTKN_ATNT_MATR_CN;
    }

    public void setIFTKN_ATNT_MATR_CN(String IFTKN_ATNT_MATR_CN) {
        this.IFTKN_ATNT_MATR_CN = IFTKN_ATNT_MATR_CN;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public String getFNCLTY_CN() {
        return FNCLTY_CN;
    }

    public void setFNCLTY_CN(String FNCLTY_CN) {
        this.FNCLTY_CN = FNCLTY_CN;
    }

    public String getDAY_INTK_CN() {
        return DAY_INTK_CN;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setDAY_INTK_CN(String DAY_INTK_CN) {
        this.DAY_INTK_CN = DAY_INTK_CN;
    }

    @Override
    public String toString() {
        return "HealthFood{" +
                "PRMS_DT='" + PRMS_DT + '\'' +
                ", BSSH_NM='" + BSSH_NM + '\'' +
                ", APLC_RAWMTRL_NM='" + APLC_RAWMTRL_NM + '\'' +
                ", HF_FNCLTY_MTRAL_RCOGN_NO='" + HF_FNCLTY_MTRAL_RCOGN_NO + '\'' +
                ", INDUTY_NM='" + INDUTY_NM + '\'' +
                ", IFTKN_ATNT_MATR_CN='" + IFTKN_ATNT_MATR_CN + '\'' +
                ", ADDR='" + ADDR + '\'' +
                ", FNCLTY_CN='" + FNCLTY_CN + '\'' +
                ", DAY_INTK_CN='" + DAY_INTK_CN + '\'' +
                '}';
    }
}
