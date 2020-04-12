package core.until;


public class TickTimer {
    /**check时间差**/
    public int lostTime = 2000;
    /**掉线确认时间**/
    private long checkLostTime;

    public TickTimer(int lostTime) {
        this.lostTime = lostTime;
        checkLostTime = System.currentTimeMillis() + lostTime;
    }

    public boolean isLost(){
        return System.currentTimeMillis() > checkLostTime;
    }

    public void reset(){
        checkLostTime = System.currentTimeMillis() + lostTime;
    }


}
