package com.jfireframework.jfireel.template.execution;

import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;

public interface Execution
{
    
    /**
     * 语句是否已经执行
     * 
     * @param variables
     * @param cache
     * @return
     */
    boolean execute(Map<String, Object> variables, StringCache cache);
    
    /**
     * 在所有的执行语句都生成完毕后会执行一次校验
     */
    void check();
}
