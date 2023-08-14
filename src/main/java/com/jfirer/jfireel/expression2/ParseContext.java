package com.jfirer.jfireel.expression2;

import java.util.Deque;
import java.util.LinkedList;

public class ParseContext
{
    private Deque<Operand>  operandStack  = new LinkedList<>();
    private Deque<Operator> operatorStack = new LinkedList<>();
    private Deque<Operand>  processStack  = new LinkedList<>();
}
