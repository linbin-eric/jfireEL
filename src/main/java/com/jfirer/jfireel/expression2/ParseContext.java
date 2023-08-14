package com.jfirer.jfireel.expression2;

import lombok.Data;

import java.util.Deque;
import java.util.LinkedList;

@Data
public class ParseContext
{
    private       Deque<Operand>  operandStack  = new LinkedList<>();
    private       Deque<Operator> operatorStack = new LinkedList<>();
    private       Deque<Operand>  processStack  = new LinkedList<>();
    private final String          el;
    private       int             index;

    public ParseContext(String el)
    {
        this.el = el;
    }
}
