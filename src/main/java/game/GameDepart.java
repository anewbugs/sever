package game;

import core.thread.Department;

/**
 * @author wu
 * 游戏管理线程
 */
public class GameDepart extends Department {
    /**
     * 构造方法
     *
     * @param name
     */
    public GameDepart(String name) {
        super( name );
    }
}
