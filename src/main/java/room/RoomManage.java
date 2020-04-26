package room;

import conn.ConnService;
import core.boot.config.Config;
import core.note.function.Function2;
import core.note.function.MsgHandle;
import core.req.ReqResultBase;
import core.req.ReqTo;
import core.thread.Department;
import core.until.Params;
import data.enity.PlayerData;
import game.GameService;
import proto.base.ConfigMsgName;
import proto.base.Escrow;
import proto.base.MsgBase;
import proto.net.MsgCreateRoom;
import proto.net.MsgEnterRoom;
import proto.net.MsgGetRoomList;

public class RoomManage {

    @MsgHandle(MsgID = ConfigMsgName.Room.MSG_CREATE_ROOM)
    public static void onMsgCreateRoom(Escrow escrow,RoomList roomList){
        MsgCreateRoom msgCreateRoom = MsgBase.DecodeMsg( MsgCreateRoom.class,escrow.msgByte );
        RoomLocation roomLocation = roomList.applayRoomLocation();
        if (roomLocation == null){
            msgCreateRoom.result = 1;//设置失败
            Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgCreateRoom ) );
            return;

        }

        PlayerData playerData = (PlayerData) escrow.context.get("playerData");
        if (playerData == null){
            msgCreateRoom.result = 1;//设置失败
            Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgCreateRoom ) );
            return;
        }
        Department depart = Config.SERVER_WORD_HEAD.getDeparts( roomLocation.getRommTo().departmentId );
        GameService service = new GameService( depart, roomLocation.getRoomId());
        service.initRoomObject(
                playerData,
                new ReqTo(  (String)escrow.context.get("departId"), (String)escrow.context.get("srvId"),"game中链接点" )
               );
        depart.addService( service );
        //本地保存房间信息
        roomList.addRoomLocation( roomLocation );
        //更新各户端conn数据
        Department.getCurrent().returns( ConnService.CONN_METHOD_UPDATE_STATUS_ROOM,new Object[]{new ReqTo( roomLocation.getRommTo() )});
        //登入成功
        Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgCreateRoom ) );
    }


    @MsgHandle(MsgID = ConfigMsgName.Room.MSG_ENTER_ROOM)
    public static void onMsgEnterRoom(Escrow escrow ,RoomList roomList){
        MsgEnterRoom msgEnterRoom = MsgBase.DecodeMsg( MsgEnterRoom.class,escrow.msgByte );
        PlayerData data = (PlayerData) escrow.context.get("playerData");
        String departId = (String) escrow.context.get( "departId" );
        String srvId =(String)escrow.context.get( "srvId" );
        RoomLocation roomLocation = roomList.getRoom( msgEnterRoom.roomId +"");
        Department.getCurrent().getReturn(
                roomLocation.getRommTo(),
                GameService.GAME_METHOD_TADD_TANK,
                roomLocation.getRoomId(),
                new ReqResultBase(new Params( "escrow" ,escrow,"roomLocation",roomLocation),(Function2<Params,Params>) RoomManage::enterRoom ),
                new Object[]{data,roomLocation.getReqto()}
        );
    }
    //方法反回调用
    public static void enterRoom(Params returns, Params Context){
        Escrow escrow = (Escrow) Context.get("escrow");
        MsgEnterRoom msgEnterRoom =MsgBase.DecodeMsg( MsgEnterRoom.class,escrow.msgByte ) ;
        //加入房间失败
        if (!(boolean)returns.get()){
            msgEnterRoom.result = 1;
            Department.getCurrent().req(
                    new ReqTo( (String) escrow.context.get( "departId" ),(String)escrow.context.get( "srvId" ),""),
                    ConnService.CONN_METHOD_SEND_MSG ,
                    Config.DEPART_ROOM_LIST_NAME,
                    Escrow.escrowBuilder( msgEnterRoom )
            );
            return;
        }
        RoomLocation roomLocation = (RoomLocation) Context.get("roomLocation");
        roomLocation.addPlays( 1 );
        Department.getCurrent().req(
                new ReqTo( (String) escrow.context.get( "departId" ),(String)escrow.context.get( "srvId" ),""),
                ConnService.CONN_METHOD_UPDATE_STATUS_ROOM ,
                Config.DEPART_ROOM_LIST_NAME,
                new Object[]{new ReqTo( roomLocation.getRommTo() )}
        );

        Department.getCurrent().req(
                new ReqTo( (String) escrow.context.get( "departId" ),(String)escrow.context.get( "srvId" ),""),
                ConnService.CONN_METHOD_SEND_MSG ,
                Config.DEPART_ROOM_LIST_NAME,
                Escrow.escrowBuilder( msgEnterRoom )
        );



    }

    @MsgHandle( MsgID = ConfigMsgName.Room.MSG_GET_ROOM_LIST)
    public static void onMsgGetRoomList(Escrow escrow,RoomList roomList){
        MsgGetRoomList msgGetRoomList = MsgBase.DecodeMsg(MsgGetRoomList.class,escrow.msgByte  );
        msgGetRoomList.rooms = roomList.getList();
        Department.getCurrent().returns( ConnService.CONN_METHOD_SEND_MSG,Escrow.escrowBuilder( msgGetRoomList ) );

    }


}
