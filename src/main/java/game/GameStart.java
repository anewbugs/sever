package game;

import core.boot.config.Config;
import core.thread.Department;
import core.thread.Headquarters;

public class GameStart {
    /**
     * 游戏线程初始化
     * @param head
     */
    public static void init(Headquarters head){
        for (int i = 0; i < Config.DEPART_GAME_SIZE; i++) {
            Department depart = new GameDepart(Config.DEPART_GAME_NAME + i);
            depart.start(head);
        }
    }
}
