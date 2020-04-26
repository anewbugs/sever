package core.until;

import java.util.HashMap;

/**
 * 参数包装函数
 * @author wu
 */
public class Params {
    private HashMap<String,Object> params = new HashMap();

    public Params(Object...param) {
        if (param.length == 1){
            params.put( null,param[0] );
        }else {
            for (int i =0 ; i < param.length / 2; i++){
                params.put((String) param[2 * i],param[2 * i + 1]);
            }
        }

    }

    public Object get(String key){
        return params.get( key );
    }

    public Object get(){
        return params.get( null );
    }


}
