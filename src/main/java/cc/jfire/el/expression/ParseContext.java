package cc.jfire.el.expression;

import cc.jfire.el.PlaceHolder;
import cc.jfire.el.expression.format.FormatToken;
import cc.jfire.el.expression.impl.operand.MethodStructureOperand;
import cc.jfire.el.expression.parse.TokenParser;
import cc.jfire.el.expression.parse.impl.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ParseContext
{
    private static TokenParser[]     parsers        = new TokenParser[]{//
            new SkipIgnoreToken(),//
            new DocTokenParser(),//
            new NumberParser(),//
            new BooleanParser(),//
            new NullParser(),//
            new ExtraExecuteParser(),//
            new CallOperandParser(),//
            new StaticClassParser(),//
            new VariableParser(),//
            new LiteralParser(),//
            new BasicOperatorParser(),//
            new LeftParenParser(),//
            new RightParenParser(),//
    };
    private final  String            el;
    private        Deque<Operand>    operandStack   = new LinkedList<>();
    private        Deque<Operator>   operatorStack  = new LinkedList<>();
    private        Deque<Operand>    processStack   = new LinkedList<>();
    /**
     * 只增加，每次识别到一个内容就往里添加
     */
    @Setter(AccessLevel.NONE)
    private        Deque<Object>     recognizeToken = new LinkedList<>();
    private        int               index;
    private        Matrix            matrix;
    private        boolean           hasReturnToken = false;
    private        ELConfig          config;
    private        List<FormatToken> formatTokens   = new LinkedList<>();

    public ParseContext(String el, Matrix matrix)
    {
        this(el, matrix, ELConfig.DEFAULT_CONFIG);
    }

    public ParseContext(String el, Matrix matrix, ELConfig elConfig)
    {
        this.el     = el;
        this.matrix = matrix;
        this.config = elConfig;
    }

    public Operand parse()
    {
        try
        {
            int length = el.length();
            while (index != length)
            {
                int oldVersionOfIndex = index;
                for (TokenParser each : parsers)
                {
                    if (each.parse(this))
                    {
                        break;
                    }
                }
                if (oldVersionOfIndex == index)
                {
                    throw new IllegalStateException("无法解析表达式，当前解析进度为:" + el.substring(0, oldVersionOfIndex));
                }
                String trim = el.substring(oldVersionOfIndex, index).trim();
                if (trim.equals("") == false)
                {
                    formatTokens.add(FormatToken.of(trim));
                }
            }
            while (operatorStack.isEmpty() == false)
            {
                operatorStack.pop().onPop(this);
            }
        }
        catch (Throwable e)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index) + ".[详细异常信息为:" + e.getMessage() + "]", e);
        }
        if (processStack.isEmpty() == false)
        {
            throw new IllegalStateException("当前表达式解析出现异常，异常位置为" + el.substring(0, index) + "。[详细异常信息为:表达式的解析不完整]");
        }
        while (operandStack.isEmpty() == false)
        {
            processStack.push(operandStack.pop());
        }
        if (processStack.stream().filter(operand -> operand == PlaceHolder.LEFT_BRACE).findAny().isPresent())
        {
            throw new IllegalStateException("当前表达式解析出现异常，代码中{}没有完全配对");
        }
        if (processStack.size() == 1 && hasReturnToken == false)
        {
            return processStack.pop();
        }
        else
        {
            return new MethodStructureOperand(processStack.toArray(Operand[]::new), true);
        }
    }
}


