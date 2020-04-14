import core.boot.ServerApplication;
import core.note.clazz.ImportPackage;

/**
 * 程序总入口
 * @author wu
 */
@ImportPackage(packages ={"room","login","conn","game"})
public class MyApplication {
    public static void main(String[] args) {
        ServerApplication.run(MyApplication.class,args);
    }
}
