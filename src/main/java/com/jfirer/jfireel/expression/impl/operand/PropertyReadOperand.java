package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.baseutil.STR;
import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.ConstructorModel;
import com.jfirer.baseutil.smc.model.FieldModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfireel.expression.Operand;
import lombok.Data;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
public abstract class PropertyReadOperand implements Operand
{
    protected final Operand                              typeOperand;
    protected final VariableOperand                      propertyNameOperand;
    protected final String                               fragment;
    protected final Map<Field, Function<Object, Object>> propertyReadAccelerators;

    protected Field findField(Class<?> ckass, String fieldName, String fragment)
    {
        while (ckass != Object.class)
        {
            try
            {
                return ckass.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException e)
            {
                ckass = ckass.getSuperclass();
            }
        }
        throw new IllegalArgumentException("解析属性，未能发现属性，异常解析表达式位置为：" + fragment);
    }

    public static class StaticClassPropertyOperand extends PropertyReadOperand
    {
        private final Supplier<Object> propertySuppiler;

        public StaticClassPropertyOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
        {
            super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
            Class<?> ckass = ((ClassOperand) typeOperand).getCkass();
            Field    field = findField(ckass, propertyNameOperand.getVariable(), fragment);
            if (Modifier.isStatic(field.getModifiers()) == false)
            {
                throw new IllegalArgumentException("解析属性异常，非 static 属性不能获取。异常错误位置在:[" + fragment + "]");
            }
            field.setAccessible(true);
            Function<Object, Object> function = propertyReadAccelerators.get(field);
            if (function != null)
            {
                propertySuppiler = () -> function.apply(null);
            }
            else
            {
                propertySuppiler = () -> {
                    try
                    {
                        return field.get(null);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                };
            }
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return propertySuppiler.get();
        }
    }

    public static class InstancePropertyReadOperand extends PropertyReadOperand
    {
        private volatile boolean                  init = false;
        private          Function<Object, Object> propertyGetter;
        private          ValueAccessor            valueAccessor;

        public InstancePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
        {
            super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            if (init == false)
            {
                synchronized (this)
                {
                    if (init == false)
                    {
                        init = true;
                        Object                   instance = typeOperand.calculate(contextParam);
                        Field                    field    = findField(instance.getClass(), propertyNameOperand.getVariable(), fragment);
                        Function<Object, Object> function = propertyReadAccelerators.get(field);
                        if (function != null)
                        {
                            propertyGetter = function;
                            return function.apply(instance);
                        }
                        else
                        {
                            valueAccessor = ValueAccessor.compile(field);
                            return valueAccessor.get(instance);
                        }
                    }
                }
            }
            return valueAccessor != null ? valueAccessor.get(typeOperand.calculate(contextParam)) : propertyGetter.apply(typeOperand.calculate(contextParam));
        }
    }

    public static class CompilePropertyReadOperand extends PropertyReadOperand
    {
        private             String        fieldName;
        private volatile    Operand       handler;
        public final static AtomicInteger count          = new AtomicInteger();
        public static final CompileHelper COMPILE_HELPER = new CompileHelper();

        public CompilePropertyReadOperand(Operand typeOperand, VariableOperand propertyNameOperand, String fragment, Map<Field, Function<Object, Object>> propertyReadAccelerators)
        {
            super(typeOperand, propertyNameOperand, fragment, propertyReadAccelerators);
            fieldName = propertyNameOperand.getVariable();
            handler   = new Operand()
            {
                @Override
                public Object calculate(Map<String, Object> contextParam)
                {
                    try
                    {
                        Object instance = typeOperand.calculate(contextParam);
                        Field  field    = findField(instance.getClass(), fieldName, fragment);
                        field.setAccessible(true);
                        ClassModel classModel = new ClassModel("CompilePropertyReadOperand_" + fieldName + "_" + count.getAndIncrement());
                        classModel.addInterface(Operand.class);
                        classModel.addField(new FieldModel("typeOperand", Operand.class, classModel));
                        ConstructorModel constructorModel = new ConstructorModel(classModel);
                        constructorModel.setParamTypes(Operand.class);
                        constructorModel.setParamNames("typeOperand");
                        constructorModel.setBody("this.typeOperand = typeOperand;");
                        classModel.addConstructor(constructorModel);
                        MethodModel methodModel = new MethodModel(Operand.class.getDeclaredMethod("calculate", Map.class), classModel);
                        methodModel.setParamterNames("contextParam");
                        String referenceName = SmcHelper.getReferenceName(field.getDeclaringClass(), classModel);
                        String methodName    = field.getType() == boolean.class ? "is" + ValueAccessor.toMethodName(field) : "get" + ValueAccessor.toMethodName(field);
                        String format = STR.format("""
                                                           {} instance = ({})typeOperand.calculate(contextParam);
                                                           return instance.{}();""", referenceName, referenceName, methodName);
                        methodModel.setBody(format);
                        classModel.putMethodModel(methodModel);
                        System.out.println("生成");
                        Class<?> compile = COMPILE_HELPER.compile(classModel);
                        Operand  operand = (Operand) compile.getConstructor(Operand.class).newInstance(typeOperand);
                        CompilePropertyReadOperand.this.handler = operand;
                        return field.get(instance);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (NoSuchMethodException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (InstantiationException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            };
        }

        @Override
        public Object calculate(Map<String, Object> contextParam)
        {
            return handler.calculate(contextParam);
        }
    }
}
