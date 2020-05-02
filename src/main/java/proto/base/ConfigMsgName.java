package proto.base;

import game.GameMsgExtend;
import login.LoginMsgExtend;
import room.RoomMsgExtend;

import java.lang.reflect.Field;
import java.util.HashSet;

public class ConfigMsgName {
    public static final class Battle {
        public static final String MSG_ENTER_BATTLE = "MsgEnterBattle";
        public static final String MSG_BATTLE_RESULT = "MsgBattleResult";
        public static final String MSG_LEAVE_BATTLE = "MsgLeaveBattle";

    }
    public static final class Login {
        public static final String MSG_REGISTER = "MsgRegister";
        public static final String MSG_LOGIN = "MsgLogin";
        public static final String MSG_KICK = "MsgKick";
        public static final String MSG_RECONNECT = "MsgReconnect";
        public static final String MSG_OFF_LINE = "MsgOffline"; //掉线消息


    }
    public static final class User{
        public static final String MSG_GET_ACHIEVE = "MsgGetAchieve";
    }

    public static final class Notepad {

        public static final String MSG_GET_TEXT = "MsgGetText";
        public static final String MSG_SAVE_TEXT = "MsgSaveText";
    }

    public static final class Room {

        public static final String MSG_GET_ROOM_LIST = "MsgGetRoomList";
        public static final String MSG_CREATE_ROOM = "MsgCreateRoom";
        public static final String MSG_ENTER_ROOM = "MsgEnterRoom";
        public static final String MSG_GET_ROOM_INFO = "MsgGetRoomInfo";
        public static final String MSG_LEAVE_ROOM = "MsgLeaveRoom";
        public static final String MSG_START_BATTLE = "MsgStartBattle";

    }
    public static final class Sync {
        public static final String MSG_SYNC_TANK = "MsgSyncTank";
        public static final String MSG_FIRE = "MsgFire";
        public static final String MSG_HIT = "MsgHit";
    }
    public static final class SysMsg {
        public static final String MSG_PING = "MsgPing";
        //public static final String MSG_PONG = "MsgPong";
    }

    private  final static HashSet<String> proto = new HashSet();

    //消息初始化
    static {
        //所有协议初始化名称
        initAllProtoName();

        //初始化账户有关的协议
        initAllAcountProtoName();

        //初始化房间有关的协议名称
        initAllRoomProtoName();

        //初始化游戏有关的协议名称
        initAllGameProtoName();
    }

    public static void msgLimitInit(){
        //所有协议初始化名称
        initAllProtoName();

        //初始化账户有关的协议
        initAllAcountProtoName();

        //初始化房间有关的协议名称
        initAllRoomProtoName();

        //初始化游戏有关的协议名称
        initAllGameProtoName();
    }


    /**
     * 游戏
     */
    private static void initAllGameProtoName() {
        GameMsgExtend.reg(
                Room.MSG_LEAVE_ROOM,
                Room.MSG_GET_ROOM_INFO,
                Room.MSG_START_BATTLE,
                //Room.MSG_ENTER_ROOM,
                //战斗数据
                Sync.MSG_FIRE,
                Sync.MSG_SYNC_TANK,
                Sync.MSG_HIT

        );
    }

    /**
     * 房间
     */
    private static void initAllRoomProtoName() {
        RoomMsgExtend.reg(
                Room.MSG_CREATE_ROOM,
                Room.MSG_ENTER_ROOM,
                Room.MSG_GET_ROOM_LIST
        );
    }

    /**
     * 账户
     */
    private static void initAllAcountProtoName() {
        LoginMsgExtend.reg(
                Login.MSG_LOGIN,
                Login.MSG_REGISTER
                );

    }


    /**
     * 所有协议初始化
     */
    private static void initAllProtoName() {
        Class innerClazz[] = ConfigMsgName.class.getClasses();
        for (Class clazz : innerClazz) {
           Field field[] = clazz.getFields();
            for (Field field1 : field) {

                try {
                    proto.add((String) field1.get(clazz));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean isProto(String msgId){
        return proto.contains(msgId);
    }


//    public static void main(String[] args) {
//        for(Map.Entry entry : instance.proto.entrySet()){
//            System.out.println(entry.getKey());
//        }
//    }
}
