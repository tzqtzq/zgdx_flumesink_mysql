package bean;

/**
 * Created by tianzhongqiu on 2018/7/3.
 */
public class InParameter {

    private String webAccountName;
    private String provinceCode;
    private String provinceName;
    private String organizationName;
    private String ESType;

    public String getWebAccountName() {
        return webAccountName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getESType() {
        return ESType;
    }

    public void setWebAccountName(String webAccountName) {
        this.webAccountName = webAccountName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setESType(String ESType) {
        this.ESType = ESType;
    }
}
