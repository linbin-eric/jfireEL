package com.jfirer.jfireel.expression.util;

import com.jfirer.baseutil.StringUtil;
import com.jfirer.jfireel.expression.node.CalculateNode;
import com.jfirer.jfireel.expression.node.QuestionNode;
import com.jfirer.jfireel.expression.node.impl.*;
import com.jfirer.jfireel.expression.token.Operator;
import com.jfirer.jfireel.expression.token.TokenType;

import java.util.LinkedList;
import java.util.List;

import static com.jfirer.jfireel.expression.token.Operator.MULTI;

public class OperatorResultUtil
{
    public static CalculateNode aggregate(List<CalculateNode> list, String el, int offset)
    {
        // 只有一个参数的情况
        if (list.size() == 1)
        {
            return list.get(0);
        }
        // 只有一个操作符
        else if (list.size() == 3)
        {
            // 第二个node是操作符
            if (list.get(1).type() == TokenType.OPERATOR)
            {
                return buildOperatorResultNode(list.get(0), list.get(1), list.get(2));
            }
            // 否则是非法情况
            else
            {
                throw new IllegalArgumentException(el.substring(0, offset));
            }
        }
        // 具备至少2个操作符
        else if (list.size() >= 5)
        {
            // 优先操作乘法，除法，取余
            for (int i = 0; i < list.size(); )
            {
                CalculateNode calculateNode = list.get(i);
                if (calculateNode.token() == MULTI || calculateNode.token() == Operator.DIVISION || calculateNode.token() == Operator.PERCENT)
                {
                    if (i > 0 && list.size() > i + 1//
                            && list.get(i - 1).type() != TokenType.OPERATOR//
                            && list.get(i + 1).type() != TokenType.OPERATOR//
                    )
                    {
                        CalculateNode resultNode = buildOperatorResultNode(list.get(i - 1), list.get(i), list.get(i + 1));
                        list.remove(i - 1);
                        list.remove(i - 1);
                        list.remove(i - 1);
                        // 执行3次消除操作符和2个操作数
                        list.add(i - 1, resultNode);
                    }
                    else
                    {
                        throw new IllegalArgumentException(el.substring(0, offset));
                    }
                }
                else
                {
                    i++;
                }
            }
            for (int i = 0; i < list.size(); )
            {
                TokenType type = list.get(i).type();
                if (list.get(i).token() == Operator.QUESTION)
                {
                    if (i == 0)
                    {
                        throw new IllegalArgumentException(StringUtil.format("?操作符前面应该有操作数,问题区间:{}", el.substring(0, offset)));
                    }
                    CalculateNode pred = list.get(i - 1);
                    // 删除前置节点
                    list.remove(i - 1);
                    // 删除？节点
                    list.remove(i - 1);
                    list.add(i - 1, buildQuestionNode(pred));
                }
                else if (list.get(i).token() == Operator.COLON)
                {
                    list.remove(i);
                    boolean             find = false;
                    List<CalculateNode> tmp  = new LinkedList<CalculateNode>();
                    for (int index = i - 1; index >= 0; index--)
                    {
                        if (list.get(index).type() != TokenType.QUESTION)
                        {
                            tmp.add(0, list.get(index));
                            list.remove(index);
                        }
                        else
                        {
                            if (tmp.size() == 0)
                            {
                                throw new IllegalArgumentException("?和:之间缺少有效表达式,问题区间:" + el.substring(0, offset));
                            }
                            find = true;
                            CalculateNode leftNode = aggregate(tmp, el, offset);
                            ((QuestionNode) list.get(index)).setLeftNode(leftNode);
                            i = index + 1;
                            break;
                        }
                    }
                    if (find == false)
                    {
                        throw new IllegalArgumentException("不是有效的三元表达式：" + el.substring(0, offset));
                    }
                    if (i >= list.size())
                    {
                        throw new IllegalArgumentException("不是有效的三元表达式：" + el.substring(0, offset));
                    }
                    ((QuestionNode) list.get(i - 1)).setRightNode(list.get(i));
                    list.remove(i);
                }
                else if (type == TokenType.OPERATOR)
                {
                    if (i > 0 && list.size() > i + 1//
                            && list.get(i - 1).type() != TokenType.OPERATOR//
                            && list.get(i + 1).type() != TokenType.OPERATOR//
                    )
                    {
                        CalculateNode resultNode = buildOperatorResultNode(list.get(i - 1), list.get(i), list.get(i + 1));
                        list.remove(i - 1);
                        list.remove(i - 1);
                        list.remove(i - 1);
                        // 执行3次消除操作符和2个操作数
                        list.add(i - 1, resultNode);
                    }
                    else
                    {
                        throw new IllegalArgumentException(el.substring(0, offset));
                    }
                }
                else
                {
                    i++;
                }
            }
            if (list.size() != 1)
            {
                throw new IllegalArgumentException(el.substring(0, offset));
            }
            return list.get(0);
        }
        else
        {
            throw new IllegalArgumentException(el.substring(0, offset));
        }
    }

    private static CalculateNode buildOperatorResultNode(CalculateNode leftNode, CalculateNode operatorNode, CalculateNode rightNode)
    {
        OperatorResultNode resultNode = null;
        switch ((Operator)operatorNode.token())
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

    private static CalculateNode buildQuestionNode(CalculateNode conditionNode)
    {
        QuestionNode questionNode = new QuestionNodeImpl();
        questionNode.setConditionNode(conditionNode);
        return questionNode;
    }
}
