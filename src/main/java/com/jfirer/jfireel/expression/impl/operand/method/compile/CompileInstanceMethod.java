package com.jfirer.jfireel.expression.impl.operand.method.compile;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class CompileInstanceMethod implements Operand
{
    private Operand operand;

    public CompileInstanceMethod(Operand instanceOperand, String methodName, Operand[] argOperands, String fragment, ELConfig elConfig)
    {
        operand = new OneshotAnalyseOperand(methodName, argOperands, fragment, instanceOperand, elConfig);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return operand.calculate(contextParam);
    }

    @Override
    public void clearFragment()
    {
        operand.clearFragment();
    }

    @Data
    @AllArgsConstructor
    class OneshotAnalyseOperand implements Operand
    {
        private String    methodName;
        private Operand[] argOperands;
        private String    fragment;
        private Operand   instanceOperand;
        private ELConfig  elConfig;

        @SneakyThrows
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object instance = instanceOperand.calculate(contextParam);
            if (instance == null)
            {
                throw new IllegalStateException("方法调用，但是调用对象为空，请检查是否变量名错误，异常位置为" + fragment);
            }
            Object[]    args       = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
            Method      executable = (Method) MethodInvoker.findExecutable(instance.getClass(), args, methodName);
            final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
            if (executable == null)
            {
                throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + methodName + "。异常解析位置为" + fragment);
            }
            CompileInstanceMethod.this.operand = MethodInvoker.make(instanceOperand, executable, argOperands, classIds, elConfig);
            executable.setAccessible(true);
            return executable.invoke(instance, MethodInvoker.compatibleValues(args, classIds));
        }

        @Override
        public void clearFragment()
        {
            for (Operand each : argOperands)
            {
                each.clearFragment();
            }
            fragment = null;
            instanceOperand.clearFragment();
        }
    }
}
