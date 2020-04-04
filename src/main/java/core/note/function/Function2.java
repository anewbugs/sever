package core.note.function;

/**
 * 函数式编程方法接口
 * @param <R> 返回值设定
 * @param <P1> 参数1
 * @param <P2> 参数2
 */
public interface Function2 <R,P1,P2>{
    R apply(P1 p1, P2 p2);
}
