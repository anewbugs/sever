package core.note.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wu
 * 消息处理注解
 */
@Target( {ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
public @interface MsgHandle {
    String MsgID();
}
