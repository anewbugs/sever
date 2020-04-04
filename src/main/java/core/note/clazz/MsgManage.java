package core.note.clazz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wu
 * 该注解用于消息处理类
 */
@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface MsgManage{
}
