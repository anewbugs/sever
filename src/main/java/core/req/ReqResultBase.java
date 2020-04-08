package core.req;

import core.note.function.Function2;
import core.until.Params;

/**
 * 回调函数实体类
 * @author wu
 */
public class ReqResultBase {
    /**上下文*/
    private Params context;


    /**方法接口*/
    private Function2 function;

    public ReqResultBase(Params context, Function2 function) {
        context = context;
        this.function = function;

    }

    /**
     * 函数回掉
     * @param object
     */
    public void reqBack(Object object){
        function.apply(object,context);
    }

    public void listen(Req req){
        reqBack(req.returns);
    }

}

