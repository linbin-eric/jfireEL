package com.jfirer.jfireel.expression.util;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.QuestionNode;
import com.jfirer.jfireel.expression.node.impl.*;
import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.token.Symbol;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OperatorResultUtil
{
    public static CalculateNode aggregate(List<CalculateNode> list, String el, int offset)
    {
        checkNonSymbol(list, el, offset);
        Deque<CalculateNode> result = processNonZeroPriorityOperator(list, el, offset);
        result = processQuestionOperator(result);
        if (result.size() != 1)
        {
            throw new IllegalStateException();
        }
        return result.getFirst();
    }

    private static Deque<CalculateNode> processQuestionOperator(Deque<CalculateNode> result)
    {
        Deque<CalculateNode> stack = new LinkedList<>();
        result.stream().forEach(node -> {
            if (stack.isEmpty())
            {
                stack.addLast(node);
            }
            else if (stack.peekLast().token() == Operator.COLON)
            {
                stack.pollLast();//弹出 ":"
                CalculateNode leftValue = stack.pollLast();
                if (stack.pollLast().token() != Operator.QUESTION)
                {
                    throw new IllegalStateException();
                }
                CalculateNode conditionNode = stack.pollLast();
                QuestionNode  questionNode  = new QuestionNodeImpl();
                questionNode.setConditionNode(conditionNode);
                questionNode.setLeftNode(leftValue);
                questionNode.setRightNode(node);
                stack.addLast(questionNode);
            }
            else
            {
                stack.addLast(node);
            }
        });
        return stack;
    }

    private static Deque<CalculateNode> processNonZeroPriorityOperator(List<CalculateNode> list, String el, int offset)
    {
        Deque<CalculateNode> result = new LinkedList<>(list);
        for (int i = 5; i >= 1; i--)
        {
            int                  priority = i;
            Deque<CalculateNode> stack    = new LinkedList<>();
            result.stream().forEach(node -> {
                if (stack.isEmpty())
                {
                    stack.addLast(node);
                }
                else if (Operator.isOperator(stack.peekLast().token()) && ((Operator) stack.peekLast().token()).getPriority() == priority)
                {
                    CalculateNode operator = stack.pollLast();
                    CalculateNode leftNode = stack.pollLast();
                    if (Operator.isOperator(leftNode.token()) == false && Operator.isOperator(node.token()) == false)
                    {
                        stack.addLast(buildOperatorResultNode(leftNode, operator, node));
                    }
                    else
                    {
                        throw new IllegalArgumentException(el.substring(0, offset));
                    }
                }
                else
                {
                    stack.addLast(node);
                }
            });
            result = stack;
        }
        return result;
    }

    /**
     * 在进行聚合的token流中不应该存在符号
     *
     * @param list
     * @param el
     * @param offset
     */
    private static void checkNonSymbol(List<CalculateNode> list, String el, int offset)
    {
        Optional<CalculateNode> any = list.stream().filter(node -> Symbol.isSymbol(node.token())).findAny();
        if (any.isPresent())
        {
            ReflectUtil.throwException(new IllegalArgumentException(el.substring(0, offset)));
        }
    }

    private static CalculateNode buildOperatorResultNode(CalculateNode leftNode, CalculateNode operatorNode, CalculateNode rightNode)
    {
        OperatorResultNode resultNode = null;
        switch ((Operator) operatorNode.token())
        {
            case PLUS:
                resultNode = new PlusNode();
                break;
            case MULTI:
                resultNode = new MutliNode();
                break;
            case EQ:
                resultNode = new EqualNode();
                break;
            case NOT_EQ:
                resultNode = new NotEqualNode();
                break;
            case MINUS:
                resultNode = new MinusNode();
                break;
            case DIVISION:
                resultNode = new DivisionNode();
                break;
            case GT:
                resultNode = new GtNode();
                break;
            case LT:
                resultNode = new LtNode();
                break;
            case PERCENT:
                resultNode = new PercentNode();
                break;
            case GT_EQ:
                resultNode = new GtEqNode();
                break;
            case LT_EQ:
                resultNode = new LtEqNode();
                break;
            case DOUBLE_AMP:
                resultNode = new DoubleAmpNode();
                break;
            case DOUBLE_BAR:
                resultNode = new DoubleBarNode();
                break;
            default:
                throw new UnsupportedOperationException(operatorNode.token().toString());
        }
        resultNode.setLeftOperand(leftNode);
        resultNode.setRightOperand(rightNode);
        return resultNode;
    }

    public static boolean trueOfFalse(Object value)
    {
        if (value == null)
        {
            return false;
        }
        if (value instanceof Boolean && ((Boolean) value).booleanValue() == false)
        {
            return false;
        }
        else
        {
            return !(value instanceof Number) || !(((Number) value).floatValue() < 0);
        }
    }
}
