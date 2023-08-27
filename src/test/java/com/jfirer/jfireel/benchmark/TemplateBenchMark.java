package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.template2.Template2;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;

@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 3)
@Fork(2)
@Measurement(iterations = 3, time = 3)
public class TemplateBenchMark
{
    private Map<String, Object> vars             = new HashMap<>();
    private Template2           directMethodMode = Template2.parse("hello ${name},my age is ${age+2}");
    private Template2           innerCallMode    = Template2.parseUseInnerCall("hello ${name},my age is ${age+2}");
    private StringBuilder       builder          = new StringBuilder();

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(TemplateBenchMark.class.getSimpleName()).build();
        new Runner(opt).run();
    }

    @Setup
    public void before()
    {
        vars.put("name", "ll");
        vars.put("age", 10);
    }

    @Benchmark
    public void directMethod()
    {
        directMethodMode.render(vars, builder);
        builder.setLength(0);
    }

    @Benchmark
    public void innerCall()
    {
        innerCallMode.render(vars, builder);
        builder.setLength(0);
    }
}
