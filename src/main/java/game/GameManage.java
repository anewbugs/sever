package game;

import conn.ConnService;
import core.boot.config.Config;
import core.note.function.MsgHandle;
import core.thread.Department;
import proto.base.ConfigMsgName;
import proto.base.Escrow;
import proto.base.MsgBase;
import proto.net.*;
import room.RoomGlobalService;

public class GameManage {
    /**
     * 获取房间数据
     * @param escrow
     * @param roomObject
     */
    @MsgHandle( MsgID = ConfigMsgName.Room.MSG_GET_ROOM_INFO)
    public static void onMsgGetRoomInfo(Escrow escrow,RoomObject roomObject){
        MsgGetRoomInfo msgGetRoomInfo = MsgBase.DecodeMsg( MsgGetRoomInfo.class,escrow.msgByte );
        msgGetRoomInfo = roomObject.getRoomInfo( msgGetRoomInfo );
        Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgGetRoomInfo ) );


    }

    /**
     * 离开房间消息
     * @param escrow
     * @param roomObject
     */
    @MsgHandle( MsgID = ConfigMsgName.Room.MSG_LEAVE_ROOM)
    public static void onMsgLeaveRoom(Escrow escrow,RoomObject roomObject){
        MsgLeaveRoom msgLeaveRoom = MsgBase.DecodeMsg( MsgLeaveRoom.class,escrow.msgByte );


        if (! roomObject.removeTankObject( msgLeaveRoom.id )){
            msgLeaveRoom.result = 1;
            Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgLeaveRoom ) );
            return;
        }


        //连接信息更新
        Department.getCurrent().returns( ConnService.CONN_METHOD_UPDATE_STATUS_ID,new Object[]{null}  );
        // 大厅消息更新
        Department.getCurrent().req( Config.TO_HALL, RoomGlobalService.HALL_METHOD_LEAVE_ROOM, roomObject.getRoomId(),new Object[]{roomObject.getRoomId(),roomObject.getPlayers()});
        //消息返回
        Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgLeaveRoom ) );
        //组播
        roomObject.multicast(
                Escrow.escrowBuilder(
                        roomObject.getRoomInfo(
                                new MsgGetRoomInfo() ) ));


    }


    /**
     * 开战
     * @param escrow
     * @param roomObject
     */
    @MsgHandle( MsgID = ConfigMsgName.Room.MSG_START_BATTLE)
    public static void onMsgStartBattle(Escrow escrow,RoomObject roomObject){
        MsgStartBattle msgStartBattle = MsgBase.DecodeMsg(MsgStartBattle.class,escrow.msgByte   );

        MsgEnterBattle msgEnterBattle = roomObject.StartBattle();
        if (msgEnterBattle == null){
            msgStartBattle.result = 1;
            Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgStartBattle ) );
            return;
        }

        //组播
        roomObject.multicast(
                Escrow.escrowBuilder(
                        msgEnterBattle));
    }


    /**
     * 坦克移动消息
     * @param escrow
     * @param roomObject
     */
    @MsgHandle( MsgID = ConfigMsgName.Sync.MSG_SYNC_TANK)
    public static void onMsgSyncTank(Escrow escrow,RoomObject roomObject){
        MsgSyncTank msgSyncTank = MsgBase.DecodeMsg(MsgSyncTank.class,escrow.msgByte   );
        roomObject.moving(msgSyncTank);
    }

    /**
     * 坦克开火消息
     * @param escrow
     * @param roomObject
     */
    @MsgHandle( MsgID = ConfigMsgName.Sync.MSG_FIRE)
    public static void onMsgFire(Escrow escrow,RoomObject roomObject){
        MsgFire msgFire = MsgBase.DecodeMsg(MsgFire.class,escrow.msgByte   );
        roomObject.firing(msgFire);
    }

    /**
     * 坦克击中消息
     * @param escrow
     * @param roomObject
     */
    @MsgHandle( MsgID = ConfigMsgName.Sync.MSG_HIT)
    public static void onMsgHit(Escrow escrow,RoomObject roomObject){
        MsgHit msgHit = MsgBase.DecodeMsg(MsgHit.class,escrow.msgByte   );
        roomObject.hiting(msgHit);
    }

}
