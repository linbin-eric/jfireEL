package com.jfireframework.jfireel.exception;

public class IllegalFormatException extends RuntimeException
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1549862548075188968L;
    
    public IllegalFormatException(String msg, String area)
    {
        super(msg + ",问题区间:" + area);
    }
}
