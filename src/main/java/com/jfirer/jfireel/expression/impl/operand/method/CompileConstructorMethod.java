package com.jfirer.jfireel.expression.impl.operand.method;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Operand;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

public class CompileConstructorMethod implements Operand
{
    private Operand operand;

    public CompileConstructorMethod(Class ckazz, Operand[] argOperands, String fragment, ELConfig elConfig)
    {
        operand = new AnalyseOperand(ckazz, ckazz.getName(), argOperands, fragment, elConfig);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        return operand.calculate(contextParam);
    }

    @AllArgsConstructor
    class AnalyseOperand implements Operand
    {
        private Class     ckazz;
        private String    methodName;
        private Operand[] argOperands;
        private String    fragment;
        private ELConfig  elConfig;

        @SneakyThrows
        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            Object[]    args       = Arrays.stream(argOperands).map(operand -> operand.calculate(contextParam)).toArray(Object[]::new);
            Constructor executable = (Constructor) MethodInvoker.findExecutable(ckazz, args, methodName);
            final int[] classIds   = Arrays.stream(executable.getParameterTypes()).mapToInt(ReflectUtil::getClassId).toArray();
            if (executable == null)
            {
                throw new IllegalArgumentException("解析过程中发现未能发现匹配的方法,方法名为:" + methodName + "。异常解析位置为" + fragment);
            }
            CompileConstructorMethod.this.operand = MethodInvoker.make(null, executable, argOperands, classIds, elConfig);
            executable.setAccessible(true);
            return executable.newInstance(MethodInvoker.compatibleValues(args, classIds));
        }
    }
}
