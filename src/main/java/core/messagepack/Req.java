package core.messagepack;


import core.until.Params;

public class Req {
    /**
     * 请求类型枚举类
     */
    public  enum Req_Type {
        RPC,
        RETURN,
        PING;

    }
    /**请求类型*/
    public Req_Type type ;

    /**请求id */
    public long  id;

    /**请求方地址**/
    public String fromHeadId;
    public String fromDepartId;

    /**接收方的具体信息*/
    public ReqTo reqTo;

    /**精准到处理函数*/
    public int methodKey ;

    /**消息具体数据*/
    public Object[] methodParam;

    /**返回值*/
    public Params returns ;


}
