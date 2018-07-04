package bean;

import java.sql.Date;

/**
 * Created by tianzhongqiu on 2018/7/3.
 */
public class PermissionLog {

    private Date dateOperation;
    private String httpAndTelnum;
    private String logType;
    private String API;
    private String traitName;
    private String traitFeatures;
    private String erroReason;
    private String inParameter;
    private String outParameter;

    public Date getDateOperation() {
        return dateOperation;
    }
    public String getTraitFeatures() {
        return traitFeatures;
    }
    public String getErroReason() {
        return erroReason;
    }

    public String getHttpAndTelnum() {
        return httpAndTelnum;
    }

    public String getLogType() {
        return logType;
    }

    public String getAPI() {
        return API;
    }

    public String getTraitName() {
        return traitName;
    }

    public String getInParameter() {
        return inParameter;
    }

    public String getOutParameter() {
        return outParameter;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    public void setTraitFeatures(String traitFeatures) {
        this.traitFeatures = traitFeatures;
    }

    public void setErroReason(String erroReason) {
        this.erroReason = erroReason;
    }

    public void setHttpAndTelnum(String httpAndTelnum) {
        this.httpAndTelnum = httpAndTelnum;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    public void setInParameter(String inParameter) {
        this.inParameter = inParameter;
    }

    public void setOutParameter(String outParameter) {
        this.outParameter = outParameter;
    }

    @Override
    public String toString() {
        return "PermissionLog{" +
                "dateOperation=" + dateOperation +
                ", httpAndTelnum='" + httpAndTelnum + '\'' +
                ", logType='" + logType + '\'' +
                ", API='" + API + '\'' +
                ", traitName='" + traitName + '\'' +
                ", traitFeatures='" + traitFeatures + '\'' +
                ", erroReason='" + erroReason + '\'' +
                ", inParameter='" + inParameter + '\'' +
                ", outParameter='" + outParameter + '\'' +
                '}';
    }
}
