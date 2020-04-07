package core.cause;

/**
 * @author
 * 系统级错误
 * 不可捕获处理直接终止程序
 */
public class SException extends RuntimeException{
    /**
     * 系统错误
     * @param cause
     */
    public SException(Throwable cause) {
        super(cause);
    }

    /**
     * 配置信息错误
     * @param string
     */
    public SException(String string){
        super(string);
    }

    /**
     * 系统不捕获的错误当对当前系统有影响
     * @param message
     * @param cause
     */
    public SException(String message, Throwable cause) {
        super( message, cause );
    }
}
