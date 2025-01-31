package proto.base;

import com.alibaba.fastjson.JSONObject;
import core.until.Log;
import io.netty.buffer.ByteBuf;

/**
 * 消息类基类
 * @author wu
 * @version 1.0
 */
public class MsgBase {
    public String protoName = null;



    public static ByteBuf Encode(ByteBuf byteBuf,MsgBase msgBase){
        byte[] protoNameLengthBytes = new byte[1];
        protoNameLengthBytes[0] = (byte)msgBase.protoName.length();
        byteBuf.writeBytes(protoNameLengthBytes);
        byteBuf.writeBytes(msgBase.protoName.getBytes());
        byteBuf.writeBytes(MsgBase.EncodeMsg(msgBase));
        return byteBuf;
    }

    /**
     * 解析ByteBuf
     * @param msg
     * @return MsgBase对象
     */
    public static MsgBase Decode(Object msg){
        ByteBuf requestByteBuf = (ByteBuf)msg;
        byte[] msgLengthBytes =new byte[2] ;
        requestByteBuf.readBytes(msgLengthBytes);
        int msgLength = (int)(((msgLengthBytes[0] << 8 ) & 0xff )|(msgLengthBytes[1] & 0xff));
        //解析协议名
        byte[] protoNameLengthBytes = new byte[1];
        requestByteBuf.readBytes(protoNameLengthBytes);
        int protoNameLength = (int)protoNameLengthBytes[0];
        byte[] protoNameBytes= new byte[protoNameLength];
        requestByteBuf.readBytes(protoNameBytes);
        String protoName = new String(protoNameBytes);
        //解析协议体
        byte[] jsonBytes = new byte[msgLength - protoNameLength - 1];
        requestByteBuf.readBytes(jsonBytes);
        MsgBase msgBase = MsgBase.DecodeMsg(protoName,jsonBytes);
        requestByteBuf.release();
        return msgBase;
    }

    /**编码
     * 将Object对象转化为JSON对象字节数组数组
     * @param msgBase
     * @return
     */
    public static byte[] EncodeMsg(MsgBase msgBase){
        //MsgBase对象转化为JSONObject对象
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(msgBase);
 //       LogUntil.logger.debug(" MsgBase Response: "+ jsonObject);
        //将JSONObject对象转化为字符串对象
        String jsonStr = jsonObject.toJSONString();
        //将JSON字符串转化为字节数组
        byte[] jsonByte = jsonStr.getBytes();
        return jsonByte;
    }

    /**解码
     * 将JSON字节数组转换成对应的javaBean对象
     * @param protoName
     * @param bytes
     * @return msgBase
     */
    public static  MsgBase DecodeMsg(String protoName, byte[] bytes){
        //字节数组转化为JSON对象
        try {
            Class<?> clazz = Class.forName("com.wu.server.proto.net."+protoName);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(bytes);
 //           LogUntil.logger.debug(" MsgBase Receive: "+ jsonObject);
            //将JSON对象转化为MsgaBase对象

            MsgBase msgBase = (MsgBase) JSONObject.toJavaObject(jsonObject, clazz);
            return msgBase;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对byte进行解码
     * @param clazz
     * @param <T>
     * @return
     */
//    public static <T>T DecodeMsg(Class<T> clazz, byte[] bytes){
//        try {
//            JSONObject jsonObject = (JSONObject) JSONObject.parse(bytes);
//            return JSONObject.toJavaObject(jsonObject, clazz);
//        }catch (Throwable e){
//            Log.msg.error( "解码错误，msg={}" , new String(bytes) ,e);
//        }finally {
//            return null;
//        }
//    }

    public static  <T>T DecodeMsg(Class<T> clazz, byte[] bytes){
        //字节数组转化为JSON对象
        try {
            //Class<?> clazz = Class.forName("com.wu.server.proto.net."+protoName);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(bytes);
            //           LogUntil.logger.debug(" MsgBase Receive: "+ jsonObject);
            //将JSON对象转化为MsgaBase对象

            T t = (T) JSONObject.toJavaObject(jsonObject, clazz);
            return t;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
