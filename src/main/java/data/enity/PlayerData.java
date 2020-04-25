package data.enity;


import core.cause.SException;

public class PlayerData implements Cloneable{
    public PlayerData(String iduser, int coin, String text, int win, int lost) {
        this.iduser = iduser;
        this.coin = coin;
        this.text = text;
        this.win = win;
        this.lost = lost;
    }

    public String iduser;
    //金币
    public int coin = 0;
    //记事本
    public String text = "new text";
    //胜利数
    public int win = 0;
    //失败数
    public int lost = 0;

    @Override
    public String toString() {
        return "PlayerData{" +
                "iduser='" + iduser + '\'' +
                ", coin=" + coin +
                ", text='" + text + '\'' +
                ", win=" + win +
                ", lost=" + lost +
                '}';
    }

    @Override
    public  Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
           throw new SException("克隆出错",e);
        }
    }
}
