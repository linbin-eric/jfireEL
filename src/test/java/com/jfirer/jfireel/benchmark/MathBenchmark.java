package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
@Fork(1)
@State(Scope.Benchmark)
public class MathBenchmark
{
    private Operand             operand      = Expression.parse("5-1");
    private Operand             operand2     = Expression.parse("5-1", new ELConfig().setMinusUseCompile(true));
    private Map<String, Object> contextParam = new HashMap<>();

    @Benchmark
    public void standard()
    {
        operand.calculate(contextParam);
    }

    @Benchmark
    public void compile()
    {
        operand2.calculate(contextParam);
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(MathBenchmark.class.getSimpleName()).timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }
}
