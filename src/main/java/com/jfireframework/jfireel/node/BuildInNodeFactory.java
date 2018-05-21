package com.jfireframework.jfireel.node;

import com.jfireframework.jfireel.node.impl.BracketNode;
import com.jfireframework.jfireel.node.impl.DynamicCompileMethodNode;
import com.jfireframework.jfireel.node.impl.DynamicDefaultMethodNode;
import com.jfireframework.jfireel.node.impl.DivisionNode;
import com.jfireframework.jfireel.node.impl.DoubleAmpNode;
import com.jfireframework.jfireel.node.impl.DoubleBarNode;
import com.jfireframework.jfireel.node.impl.EqualNode;
import com.jfireframework.jfireel.node.impl.GtEqNode;
import com.jfireframework.jfireel.node.impl.GtNode;
import com.jfireframework.jfireel.node.impl.KeywordNode;
import com.jfireframework.jfireel.node.impl.LtEqNode;
import com.jfireframework.jfireel.node.impl.LtNode;
import com.jfireframework.jfireel.node.impl.MinusNode;
import com.jfireframework.jfireel.node.impl.MutliNode;
import com.jfireframework.jfireel.node.impl.NotEqualNode;
import com.jfireframework.jfireel.node.impl.NumberNode;
import com.jfireframework.jfireel.node.impl.OperatorNode;
import com.jfireframework.jfireel.node.impl.OperatorResultNode;
import com.jfireframework.jfireel.node.impl.PercentNode;
import com.jfireframework.jfireel.node.impl.PlusNode;
import com.jfireframework.jfireel.node.impl.DynamicPropertyNode;
import com.jfireframework.jfireel.node.impl.QuestionNodeImpl;
import com.jfireframework.jfireel.node.impl.StringNode;
import com.jfireframework.jfireel.node.impl.SymBolNode;
import com.jfireframework.jfireel.node.impl.VariableNode;
import com.jfireframework.jfireel.token.Operator;
import com.jfireframework.jfireel.token.Symbol;
import com.jfireframework.jfireel.util.Functions;

public class BuildInNodeFactory implements NodeFactory
{
    
    @Override
    public CalculateNode parseKeyWord(String literals, int function)
    {
        return new KeywordNode(literals);
    }
    
    @Override
    public CalculateNode parseVariable(String literals, int function)
    {
        return new VariableNode(literals);
    }
    
    @Override
    public CalculateNode parseSymBol(Symbol symbol, int function)
    {
        return new SymBolNode(symbol);
    }
    
    @Override
    public CalculateNode buildOperatorResultNode(CalculateNode leftNode, CalculateNode operatorNode, CalculateNode rightNode, int function)
    {
        OperatorResultNode resultNode = null;
        switch ((Operator) operatorNode.type())
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
                throw new UnsupportedOperationException(((Operator) operatorNode.type()).toString());
        }
        resultNode.setLeftOperand(leftNode);
        resultNode.setRightOperand(rightNode);
        return resultNode;
    }
    
    @Override
    public CalculateNode buildMethodNode(CalculateNode beanNode, String literals, int function)
    {
        if (Functions.isMethodInvokeByCompile(function))
        {
            return new DynamicCompileMethodNode(literals, beanNode);
        }
        else
        {
            return new DynamicDefaultMethodNode(literals, beanNode);
        }
    }
    
    @Override
    public CalculateNode buildPropertyNode(CalculateNode beanNode, String literals, int function)
    {
        return new DynamicPropertyNode(literals, beanNode);
    }
    
    @Override
    public CalculateNode buildStringNode(String literals, int function)
    {
        return new StringNode(literals);
    }
    
    @Override
    public CalculateNode buildNumberNode(String literals, int function)
    {
        return new NumberNode(literals);
    }
    
    @Override
    public CalculateNode buildOperatorNode(Operator operator, int function)
    {
        return new OperatorNode(operator);
    }
    
    @Override
    public CalculateNode buildBracketNode(CalculateNode beanNode, CalculateNode valueNode)
    {
        return new BracketNode(beanNode, valueNode);
    }
    
    @Override
    public CalculateNode buildQuestionNode(CalculateNode conditionNode)
    {
        QuestionNode questionNode = new QuestionNodeImpl();
        questionNode.setConditionNode(conditionNode);
        return questionNode;
    }
    
}
