package proto.base;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;


import static javafx.scene.input.KeyCode.T;

public class Escrow {
    public String msgName;
    public byte[] msgByte;

    public Escrow(String msgName, byte[] msgByte) {
        this.msgName = msgName;
        this.msgByte = msgByte;
    }

    @Override
    public String toString() {
       return new ToStringBuilder( this )
               .append( new String(msgByte) )
               .toString();
    }

    /**
     * 将数据转换成协议主体
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends MsgBase> T getMsg(Class<T> clazz){
        return ((JSONObject) JSONObject.parse(msgByte)).toJavaObject(clazz);
    }


    public static Escrow escrowBuilder(MsgBase msgBase){
       byte[] jsonByte = ((JSONObject)JSONObject.toJSON(msgBase)).toJSONString().getBytes();

       return new Escrow(msgBase.protoName,jsonByte);
    }


}
