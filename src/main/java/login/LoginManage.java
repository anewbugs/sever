package login;

import conn.ConnService;
import core.db.ResultMapAnnotation;
import core.note.function.MsgHandle;
import core.req.Req;
import core.thread.Department;
import data.dbservice.AccountService;
import data.mapper.AccountMapper;
import proto.base.Escrow;
import proto.base.ConfigMsgName;
import proto.base.MsgBase;
import proto.net.MsgKick;
import proto.net.MsgLogin;

import java.util.Locale;

public class LoginManage {
    /**数据库**/
    private static AccountService accout = new AccountService();
    /**
     * 登入消息处理
     * @param escrow
     * @param msgParam
     */
    @MsgHandle(MsgID = ConfigMsgName.Login.MSG_LOGIN)
    public static void onMsgLogin(Escrow escrow, MsgLoginParam msgParam){
        try{
            //消息获取
            MsgLogin msgLogin = (MsgLogin) MsgBase.DecodeMsg(MsgLogin.class,escrow.msgByte);
            //密码验证
            if (!accout.checkLogin( msgLogin.id,msgLogin.pw )){
                msgLogin.result = 1;
                Department.getCurrent().returnMsg( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgLogin ) );
                return;
            }
            //不允许再次登入
            if (msgParam.loginAgain( msgLogin.id )){
                //发送踢下线协议
                MsgKick msgKick = new MsgKick();
                msgKick.reason = 0;
                Department.getCurrent().returnMsg( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgKick ) );
                return;
            }

            //掉线处理
            if (msgParam.isLost( msgLogin.id ) ){
                //todo
                return;
            }

            if (msgParam.applayLogin( msgLogin.id )){
                //更新各户端conn数据
                Department.getCurrent().returnMsg( ConnService.CONN_METHOD_UPDATE_STATUS_ID,new Object[]{msgLogin.id});
                //登入成功
                Department.getCurrent().returnMsg( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgLogin ) );
            }

        }catch(Throwable e){
            System.out.println(e);
        }


    }
}
