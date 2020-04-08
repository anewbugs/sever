package core.req;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author wu
 * 请求点
 */
public class ReqTo {
    /**请求地址*/
    public String headId;
    public String departmentId;
    public String serviceId;
    /**附加信息*/      //应用于日志
    public String reqInfo;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("headId",headId)
                .append("departmentId",departmentId)
                .append("serviceId",serviceId)
                .append("reqInfo",reqInfo)
                .toString();
    }
}
