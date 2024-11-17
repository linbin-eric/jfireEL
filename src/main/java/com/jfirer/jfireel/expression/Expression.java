package com.jfirer.jfireel.expression;

import com.jfirer.baseutil.reflect.valueaccessor.ValueAccessor;
import com.jfirer.jfireel.expression.format.FormatContext;
import com.jfirer.jfireel.expression.format.FormatToken;
import com.jfirer.jfireel.expression.impl.operand.method.MethodInvoker;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Expression
{
    public static final Map<Field, ValueAccessor>            SHARE_VALUEACCESSOR_CACHE = new ConcurrentHashMap<>();
    public static final Map<Executable, MethodInvoker>       SHARE_METHODINVOKER       = new ConcurrentHashMap<>();
    /**
     * 提前注册的类的简单名，可以在 EL 表达式中被识别
     */
    private static      Map<String, Class<?>>                className                 = new ConcurrentHashMap<>();
    /**
     * 提前注册的EL 内部调用，可以在 EL 表达式中被识别
     */
    private static      Map<String, MethodInvoker>           innerCalls                = new ConcurrentHashMap<>();
    /**
     * 加速方法调用的实现。对应 method 不采取反射方式调用，使用对应的MethodInvokeHelper进行调用。
     */
    private static      Map<Method, MethodInvoker>           methodInvokeAccelerators  = new ConcurrentHashMap<>();
    /**
     * 加速属性的读取，对应属性的读取不采用反射的方式，采用对应的Function<Object, Object>来返回属性的值
     */
    private static      Map<Field, Function<Object, Object>> propertyReadAccelerators  = new ConcurrentHashMap<>();

    public record Tuper(Class ckass, String extendMethodName)
    {
    }

    public static void registerClass(String name, Class<?> ckass)
    {
        className.put(name, ckass);
    }

    public static void registerInnerCall(String name, MethodInvoker function)
    {
        innerCalls.put(name, function);
    }

    public static void registerPropertyReadAccelerator(Field field, Function<Object, Object> accelerator)
    {
        propertyReadAccelerators.put(field, accelerator);
    }

    public static void registerAccelerateInvoker(Method method, MethodInvoker methodInvoker)
    {
        methodInvokeAccelerators.put(method, methodInvoker);
    }

    public static Operand parse(String el)
    {
        return new ParseContext(ELConfig.DEFAULT_CONFIG, el, className, innerCalls, methodInvokeAccelerators, propertyReadAccelerators).parse();
    }

    public static Operand parse(String el, ELConfig config)
    {
        return new ParseContext(config, el, className, innerCalls, methodInvokeAccelerators, propertyReadAccelerators).parse();
    }

    /**
     * 对表达式内容进行格式化并且返回。
     * 格式化的要点有：
     * 1. 遇到{进行换行，该符号独占一行。并且下一行对比该行缩进 4 个空格。
     * 2. 遇到;进行换行。
     * 3. 遇到}进行换行，该符号独占一行。并且下一行对比改行取消缩进 4 个空格。
     *
     * @param content
     * @return
     */
    public static String format(String content)
    {
        ParseContext parseContext = new ParseContext(ELConfig.DEFAULT_CONFIG, content, className, innerCalls, methodInvokeAccelerators, propertyReadAccelerators);
        parseContext.parse();
        List<FormatToken> formatTokens = parseContext.getFormatTokens();
        StringBuilder     builder      = new StringBuilder();
        FormatContext     context      = new FormatContext();
        formatTokens.forEach(token -> token.out(builder, context));
        return builder.toString().trim();
    }

    public static void main(String[] args)
    {
        System.out.println(format("""
                                          if(
                                          diagnoseInfos.hasDiagnose('A52.705+K77.0*,B65.202+K77.0*,F06.800x008,K70.200,K70.300,K70.301+I98.2*,K70.302+I98.3*,K70.303+I98.2*,K70.304+I98.2*,K70.305+I98.3*,K70.306+I98.3*,K71.700,K71.701,K71.702,K74.100,K74.200,K74.300,K74.300x005+I98.2*,K74.300x006+I98.3*,K74.300x007+I98.2*,K74.300x008+I98.3*,K74.301+I98.2*,K74.302+I98.3*,K74.400,K74.500,K74.600x002,K74.600x003,K74.600x010,K74.600x021,K74.600x025,K74.600x027,K74.600x029,K74.600x030,K74.600x031,K74.600x034,K74.600x036,K74.600x041,K74.600x042,K74.601,K74.602,K74.603,K74.604,K74.605,K74.606,K74.607,K74.608,K74.610,K74.611,K74.612,K74.613,K74.614,K74.615+I98.3*,K74.616+I98.2*,K74.617+I98.3*,K74.618+I98.3*,K74.619+I98.2*,K74.620+I98.2*,K76.101,K76.600x007,O26.604,P78.803') &&
                                          diagnoseInfos.hasDiagnose('B15.002,B15.003,B16.000x001,B16.202,B16.203,B17.102,B17.103,B17.200x005,B17.204,B17.205,B17.800x001,B17.800x002,B17.800x003,B17.803,B17.900x005,B17.900x006,B18.004,B18.106,B18.204,B18.800x001,B18.800x002,B18.800x003,B18.800x004,B18.801,B18.802,B18.803,B18.805,B18.903,B19.001,B19.002,D61.800x002,K72.000x013,K73.901,O98.406') &&
                                          diagnoseInfos.hasDiagnose('B15.000,B15.001,B15.002,B15.003,B15.900,B16.000,B16.001,B16.100,B16.101,B16.200,B16.201,B16.202,B16.203,B16.204,B16.900,B19.000,B19.001,B19.002,B19.900,B25.101+K77.0*,K70.403')
                                          ){return 0;}
                                          return 2;
                                          """));
    }
}
