package core.req;


import core.boot.config.Config;
import core.until.Params;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Req {


    /**
     * 请求类型枚举类
     */
    public  enum Req_Type {
        RPC,
        RETURN

    }
    /**请求类型*/
    public Req_Type type ;

    /**请求id */
    public long  id;

    /**请求方地址**/
    public String fromDepartId;
    /**srvid**/
    public String fromrSvId;
    /**接收方的具体信息*/
    public ReqTo reqTo;

    /**精准到处理函数*/
    public int methodKey ;

    /**消息具体数据*/
    public Object[] methodParam;

    /**返回值*/
    public Params returns ;

    /**
     * 请求返回重建
     * @return
     */
    public Req returnNew() {
        Req req = new Req();
        req.id = this.id;
        req.type = Req_Type.RETURN;
        req.fromDepartId = this.reqTo.departmentId;
        req.fromrSvId = this.reqTo.serviceId;
        req.reqTo = new ReqTo(this.fromDepartId,this.fromrSvId,"消息返回");
        return req;
    }

    public void send(){
        Config.SERVER_WORD_HEAD.handleReq( this );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type",type)
                .append("fromDepartId",fromDepartId)
                .append("fromrSvId",fromrSvId)
                .append("reqTo",reqTo)
                .append("methodKey",methodKey)
                .append("methodParam",methodParam)
                .append("returns",returns)
                .toString();
    }
}
