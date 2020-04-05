import core.note.clazz.DisServer;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Set;

public class test {
    public static void main(String[] args) {
        Reflections reflections = new Reflections(/*new ConfigurationBuilder()
                //.forPackages("com.boothsun.reflections") // 指定路径URL
                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具*/
        );

        Set<Class<?>> ss = reflections.getTypesAnnotatedWith( DisServer.class );
        for (Class c: ss) {
            Method[] methods =c.getDeclaredMethods();
            for (Method m: methods) {
                System.out.println(m);
            }
        }
    }
}
