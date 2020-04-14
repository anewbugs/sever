package core.req;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author wu
 * 请求点
 */
public class ReqTo {
    public ReqTo(String departmentId, String serviceId, String reqInfo) {
        this.departmentId = departmentId;
        this.serviceId = serviceId;
        this.reqInfo = reqInfo;
    }

    public ReqTo(ReqTo to) {
        this.departmentId = to.departmentId;
        this.serviceId = to.serviceId;
        this.reqInfo = to.reqInfo;
    }

    public void reSet(String departmentId, String serviceId, String reqInfo){
        this.departmentId = departmentId;
        this.serviceId = serviceId;
        this.reqInfo = reqInfo;
    }

    /**请求地址*/
    public String departmentId;
    public String serviceId;
    /**附加信息*/      //应用于日志
    public String reqInfo;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("departmentId",departmentId)
                .append("serviceId",serviceId)
                .append("reqInfo",reqInfo)
                .toString();
    }
}
