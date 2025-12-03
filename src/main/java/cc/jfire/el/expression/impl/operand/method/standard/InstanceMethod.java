package cc.jfire.el.expression.impl.operand.method.standard;

import cc.jfire.baseutil.reflect.ReflectUtil;
import cc.jfire.el.expression.Expression;
import cc.jfire.el.expression.Matrix;
import cc.jfire.el.expression.Operand;
import cc.jfire.el.expression.impl.operand.method.MethodInvokeOperand;
import cc.jfire.el.expression.impl.operand.method.MethodInvoker;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class InstanceMethod extends MethodInvokeOperand
{
    private Operand instanceOperand;

    public InstanceMethod(Operand instanceOperand, String methodName, Operand[] argOperands, String fragment, Matrix matrix)
    {
        super(methodName, argOperands, fragment, matrix);
        this.instanceOperand = instanceOperand;
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
                    Object instance = instanceOperand.calculate(contextParam);
                    if (instance == null)
                    {
                        throw new IllegalStateException("方法调用，但是调用对象为空，请检查是否变量名错误，异常位置为" + fragment);
                    }
                    Object[] args       = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray();
                    Method   executable = (Method) MethodInvoker.findExecutable(instance.getClass(), args, memberName);
                    if (executable == null)
                    {
                        throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + memberName + "。异常解析位置为" + fragment);
                    }
                    final int[] classIds = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
                    executable.setAccessible(true);
                    invoker = matrix.findAcceleratorForMethodInvoke(executable);
                    if (invoker == null)
                    {
                        invoker = Expression.SHARE_METHODINVOKER.computeIfAbsent(executable, m -> (Object obj, Operand[] argOperands, Map<String, Object> context) -> {
                            Object[] _args = new Object[argOperands.length];
                            for (int i = 0; i < _args.length; i++)
                            {
                                _args[i] = argOperands[i].calculate(context);
                            }
                            try
                            {
                                return executable.invoke(obj, MethodInvoker.compatibleValues(_args, classIds));
                            }
                            catch (IllegalAccessException | InvocationTargetException e)
                            {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    init = true;
                    return executable.invoke(instance, MethodInvoker.compatibleValues(args, classIds));
                }
            }
        }
        return invoker.invoke(instanceOperand.calculate(contextParam), argOperands, contextParam);
    }

    @Override
    public void clearFragment()
    {
        super.clearFragment();
        instanceOperand.clearFragment();
    }
}
