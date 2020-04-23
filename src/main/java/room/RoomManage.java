package room;

import core.note.function.MsgHandle;
import proto.base.ConfigMsgName;
import proto.base.Escrow;

public class RoomManage {
    /**房间初始ID**/

    @MsgHandle(MsgID = ConfigMsgName.Room.MSG_CREATE_ROOM)
    public static void onMsgCreateRoom(Escrow escrow,RoomList roomList){


    }

    @MsgHandle(MsgID = ConfigMsgName.Room.MSG_ENTER_ROOM)
    public static void onMsgEnterRoom(Escrow escrow ,RoomList roomList){
        //todo
    }

    @MsgHandle( MsgID = ConfigMsgName.Room.MSG_GET_ROOM_LIST)
    public static void onMsgGetRoomList(Escrow escrow,RoomList roomList){
        //todo
    }


}
