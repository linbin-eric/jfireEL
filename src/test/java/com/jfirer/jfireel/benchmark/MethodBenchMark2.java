package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.ReferenceCall;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 3, time = 5)
@Fork(2)
@State(Scope.Benchmark)
public class MethodBenchMark2
{
    private Operand referenceCall;
    private Operand staticCall;
    private Map     map = Map.of();

    public static Integer plus(Object instance, Operand[] methodParams, Map<String, Object> contextParam)
    {
        Integer a = (Integer) methodParams[0].calculate(contextParam);
        Integer b = (Integer) methodParams[1].calculate(contextParam);
        return a + b;
    }

    @ReferenceCall
    public static Integer plus2(Integer a, Integer b)
    {
        return a + b;
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(MethodBenchMark2.class.getSimpleName()).timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }

    @Setup(Level.Trial)
    public void before()
    {
        Expression.scanForReferenceCall(MethodBenchMark2.class);
        Expression.registerClass("MethodBenchMark2", MethodBenchMark2.class);
        referenceCall = Expression.parse("plus2(1,2)");
        staticCall    = Expression.parse("MethodBenchMark2.plus2(1,2)", new ELConfig().setMethodInvokeUseCompile(true).setDisableCompileMethodCompatibleValue(true));
    }

    @Benchmark
    public void testReferenceCall()
    {
        referenceCall.calculate(map);
    }

    @Benchmark
    public void testStaticCall()
    {
        staticCall.calculate(map);
    }
}
