package core.until;

import java.util.HashMap;

/**
 * 参数包装函数
 * @author wu
 */
public class Params {
    private HashMap<String,Object> params = new HashMap();

    public Params(Object...param) {
        for (int i =0 ; i < param.length / 2; i++){
            params.put((String) param[i],param[i+1]);
        }
    }
}
