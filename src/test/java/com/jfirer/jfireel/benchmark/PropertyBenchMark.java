package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.TestSupport;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.util.Functional;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class PropertyBenchMark
{
    Expression lexer   = Expression.parse("home.person.age",Functional.build().setRecognizeEveryTime(false).toFunction());
    Expression lexer_2 = Expression.parse("home.person.age", Functional.build().setRecognizeEveryTime(false).toFunction());
    protected TestSupport.Person  person;
    public    Map<String, Object> vars = new HashMap<String, Object>();

    @Setup
    public void before()
    {
        person = new TestSupport.Person();
        person.age = 14;
        vars.put("person", person);
    }

    @Benchmark
    public void test()
    {
        lexer.calculate(vars);
    }

    @Benchmark
    public void testUnsafe()
    {
        lexer_2.calculate(vars);
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(PropertyBenchMark.class.getSimpleName()).warmupIterations(2)//
                .warmupTime(TimeValue.seconds(3)).forks(2)//
                .measurementIterations(5)//
                .measurementTime(TimeValue.seconds(2))//
                .timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }
}
