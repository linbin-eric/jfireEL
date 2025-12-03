package cc.jfire.el.expression.impl.operand.method.standard;

import cc.jfire.baseutil.reflect.ReflectUtil;
import cc.jfire.el.expression.Expression;
import cc.jfire.el.expression.Matrix;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.impl.operand.method.MethodInvokeOperand;
import cc.jfire.el.expression.impl.operand.method.MethodInvoker;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class ConstructorMethod extends MethodInvokeOperand
{
    private Class ckass;

    public ConstructorMethod(Class<?> ckass, Operand[] argOperands, String fragment, Matrix matrix)
    {
        super(ckass.getName(), argOperands, fragment, matrix);
        this.ckass = ckass;
    }

    @SneakyThrows
    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        if (!init)
        {
            synchronized (this)
            {
                if (!init)
                {
                    Object[]    args       = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray();
                    Executable  executable = MethodInvoker.findExecutable(ckass, args, memberName);
                    final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                    if (executable == null)
                    {
                        throw new IllegalArgumentException("解析过程中发现未能发现匹配的构造方法。异常解析位置为" + fragment);
                    }
                    Constructor constructor = (Constructor) executable;
                    constructor.setAccessible(true);
                    invoker = matrix.findAcceleratorForMethodInvoke(executable);
                    if (invoker == null)
                    {
                        invoker = Expression.SHARE_METHODINVOKER.computeIfAbsent(executable, c -> (Object obj, Operand[] argOperands, Map<String, Object> context) -> {
                            Object[] _args = new Object[argOperands.length];
                            for (int i = 0; i < _args.length; i++)
                            {
                                _args[i] = argOperands[i].calculate(context);
                            }
                            try
                            {
                                return constructor.newInstance(MethodInvoker.compatibleValues(_args, classIds));
                            }
                            catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
                            {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    init = true;
                    return constructor.newInstance(MethodInvoker.compatibleValues(args, classIds));
                }
            }
        }
        return invoker.invoke(null, argOperands, contextParam);
    }
}
