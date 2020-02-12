package com.jfireframework.jfireel.benchmark;

import com.jfireframework.jfireel.TestSupport;
import com.jfireframework.jfireel.expression.Expression;
import com.jfireframework.jfireel.expression.util.Functional;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.exception.ScriptEvalError;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class MethodMark
{

    public Expression lexer         = Expression.parse("home.bool(person.getAge() + '12' != value)");
    public Expression lexer_compile = Expression.parse("home.bool(person.getAge() + '12' != value)", Functional.build().setMethodInvokeByCompile(true).toFunction());
    org.springframework.expression.Expression exp;
    StandardEvaluationContext                 societyContext;
    GroupTemplate                             gt;
    String                                    key = "return @home.bool(@person.getAge()+'12'!=value);";
    public Map<String, Object> vars = new HashMap<String, Object>();
    public TestSupport.Person  person;
    public TestSupport.Home    home;

    @Setup(Level.Trial)
    public void before()
    {
        home = new TestSupport.Home();
        person = new TestSupport.Person();
        person.age = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
        String value = person.age + "12";
        vars.put("value", value);
        System.out.println("初始化一次");
        societyContext = new StandardEvaluationContext();
        societyContext.setVariable("vars", vars);
        ExpressionParser parser = new SpelExpressionParser();
        exp = parser.parseExpression("#vars['home'].bool(#vars['person'].getAge() + '12' != #vars['value'])");
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        try
        {
            gt = new GroupTemplate(resourceLoader, Configuration.defaultConfiguration());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void testJfireEl()
    {
        lexer.calculate(vars);
    }

    @Benchmark
    public void testJfireEl_compile()
    {
        lexer_compile.calculate(vars);
    }

//    @Benchmark
    public void testSpringEl()
    {
        exp.getValue(societyContext);
    }

//    @Benchmark
    public void testBeetlEl()
    {
        try
        {
            gt.runScript(key, vars);
        }
        catch (ScriptEvalError scriptEvalError)
        {
            scriptEvalError.printStackTrace();
        }
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(MethodMark.class.getSimpleName()).warmupIterations(2)//
                .warmupTime(TimeValue.seconds(2))
                .jvmArgs("-server")
                .forks(2)//
                .measurementIterations(3)//
                .measurementTime(TimeValue.seconds(2))//
                .timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }
}
