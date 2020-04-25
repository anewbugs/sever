package core.note.function;

/**
 * 函数式编程方法接口
 * @param <P1> 参数1
 * @param <P2> 参数2
 */
public interface Function2 <P1,P2>{
    void apply(P1 p1, P2 p2);
}
