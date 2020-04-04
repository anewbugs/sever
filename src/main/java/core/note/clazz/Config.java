package core.note.clazz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wu
 * 环境配置
 */
@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface Config {
    String path() default "";
}
