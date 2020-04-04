package core.cause;

/**
 * @author
 * 系统级错误
 * 不可捕获处理直接终止程序
 */
public class SException extends RuntimeException{
    public SException(Throwable cause) {
        super(cause);
    }
}
