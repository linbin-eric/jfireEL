package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.FunctionTest;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 4)
@Measurement(iterations = 2, time = 4)
@Fork(2)
@State(Scope.Benchmark)
public class UnionBenchmark
{
    String  content                               = "person.setAge(person.getAge()-6);";
    Operand stand                                 = Expression.parse(content);
    Operand method                                = Expression.parse(content, new ELConfig().setMethodInvokeUseCompile(true));
    Operand methodWithoutCompatibility            = Expression.parse(content, new ELConfig().setMethodInvokeUseCompile(true).setDisableCompileMethodCompatibleValue(true));
    Operand methodWithoutCompatibilityAndProperty = Expression.parse(content, new ELConfig().setMethodInvokeUseCompile(true).setDisableCompileMethodCompatibleValue(true).setPropertyReadUseCompile(true));
    Operand math                                  = Expression.parse(content, new ELConfig().setMethodInvokeUseCompile(true).setDisableCompileMethodCompatibleValue(true).setPropertyReadUseCompile(true).setMathUseCompile(true));
    private HashMap<String, Object> map = new HashMap<>();

    @Setup
    public void before()
    {
        FunctionTest.Person person = new FunctionTest.Person(1);
        FunctionTest.Person p2     = new FunctionTest.Person(2);
        map.put("person", person);
        map.put("p2", p2);
    }

    @Benchmark
    public void stand(Blackhole blackhole)
    {
        blackhole.consume(stand.calculate(map));
    }

    @Benchmark
    public void method(Blackhole blackhole)
    {
        blackhole.consume(method.calculate(map));
    }

    @Benchmark
    public void methodWithoutCompatibility(Blackhole blackhole)
    {
        blackhole.consume(methodWithoutCompatibility.calculate(map));
    }

    @Benchmark
    public void methodWithoutCompatibilityAndProperty(Blackhole blackhole)
    {
        blackhole.consume(methodWithoutCompatibilityAndProperty.calculate(map));
    }

    @Benchmark
    public void math(Blackhole blackhole)
    {
        blackhole.consume(math.calculate(map));
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(UnionBenchmark.class.getSimpleName()).timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }
}
