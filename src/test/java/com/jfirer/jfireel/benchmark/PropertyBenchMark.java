package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.TestSupport;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.ParseContext;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
@Fork(3)
@State(Scope.Benchmark)
public class PropertyBenchMark
{
    public    Map<String, Object> vars = new HashMap<String, Object>();
    protected TestSupport.Person  person;
    Operand lexer_3     = Expression.parse("home.person");
    Operand lexer_accel;
    Operand compileRead = Expression.parse("home.person", new ELConfig().setPropertyReadUseCompile(true));
    Operand shareRead   = Expression.parse("home.person", new ELConfig().setSharePropertyRead(true));

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(PropertyBenchMark.class.getSimpleName()).timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }

    @Setup
    public void before()
    {
        ParseContext parseContext = new ParseContext("home.person");
        try
        {
            parseContext.registerPropertyReadAccelerator(TestSupport.Home.class.getDeclaredField("person"), v -> ((TestSupport.Home) v).getPerson());
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
        lexer_accel = parseContext.parse();
        TestSupport.Home home = new TestSupport.Home();
        person      = new TestSupport.Person();
        person.age  = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
    }

    @Benchmark
    public void test(Blackhole blackhole)
    {
        Object calculate = lexer_3.calculate(vars);
        blackhole.consume(calculate);
    }

    @Benchmark
    public void testAcc(Blackhole blackhole)
    {
        Object calculate = lexer_accel.calculate(vars);
        blackhole.consume(calculate);
    }

    @Benchmark
    public void testCompile(Blackhole blackhole)
    {
        Object calculate = compileRead.calculate(vars);
        blackhole.consume(calculate);
    }

    @Benchmark
    public void testShareRead(Blackhole blackhole)
    {
        Object calculate = shareRead.calculate(vars);
        blackhole.consume(calculate);
    }
}
