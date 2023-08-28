package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.TestSupport;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.util.Functional;
import com.jfirer.jfireel.expression2.Expression2;
import com.jfirer.jfireel.expression2.Operand;
import com.jfirer.jfireel.expression2.ParseContext;
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
    public    Map<String, Object> vars = new HashMap<String, Object>();
    protected TestSupport.Person  person;
    Expression lexer   = Expression.parse("home.person.age", Functional.build().setRecognizeEveryTime(false).toFunction());
    Expression lexer_2 = Expression.parse("home.person.age", Functional.build().setRecognizeEveryTime(false).toFunction());
    Operand    lexer_3 = Expression2.parse("home.person.age");
    Operand    lexer_4 = new ParseContext("home.person.age").setPropertyReadUseLambda(true).parse();

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(PropertyBenchMark.class.getSimpleName()).warmupIterations(2)//
                                          .warmupTime(TimeValue.seconds(3)).forks(2)//
                                          .measurementIterations(5)//
                                          .measurementTime(TimeValue.seconds(2))//
                                          .timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }

    @Setup
    public void before()
    {
        TestSupport.Home home = new TestSupport.Home();
        person      = new TestSupport.Person();
        person.age  = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
    }

    @Benchmark
    public void test()
    {
        lexer.calculate(vars);
    }

        @Benchmark
    public void testLambda()
    {
        lexer_4.calculate(vars);
    }

    @Benchmark
    public void testNew()
    {
        lexer_3.calculate(vars);
    }
}
