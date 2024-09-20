package com.jfirer.jfireel.benchmark;

import com.jfirer.jfireel.TestSupport;
import com.jfirer.jfireel.expression.ELConfig;
import com.jfirer.jfireel.expression.Expression;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.ParseContext;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.exception.ScriptEvalError;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
@Fork(1)
@State(Scope.Benchmark)
public class MethodBenchMark
{
    public Operand             lexer_new                          = Expression.parse("home.personAge2(5)");
    public Operand             lexer_new_lambda;
    public Operand             operandUseCompile                  = Expression.parse("home.personAge2(5)", new ELConfig().setMethodInvokeUseCompile(true));
    public Operand             operandUseCompileDisableCompatible = Expression.parse("home.personAge2(5)", new ELConfig().setMethodInvokeUseCompile(true).setDisableCompileMethodCompatibleValue(true));
    public Map<String, Object> vars                               = new HashMap<String, Object>();
    public TestSupport.Person  person;
    public TestSupport.Home    home;
    org.springframework.expression.Expression exp;
    StandardEvaluationContext                 societyContext;
    GroupTemplate                             gt;
    String                                    key = "return @person.getAge();";

    public MethodBenchMark()
    {
        ParseContext parseContext = new ParseContext("home.personAge2(5)");
        Method       personAge2   = null;
        try
        {
            personAge2 = TestSupport.Home.class.getDeclaredMethod("personAge2", int.class);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        parseContext.registerMethodInvokeAccelerator(personAge2, (instance, operands, map) -> ((TestSupport.Home) instance).personAge2((Integer) operands[0].calculate(map)));
        lexer_new_lambda = parseContext.parse();
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().include(MethodBenchMark.class.getSimpleName()).timeUnit(TimeUnit.SECONDS).build();
        new Runner(opt).run();
    }

    @Setup(Level.Trial)
    public void before()
    {
        home        = new TestSupport.Home();
        person      = new TestSupport.Person();
        person.age  = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
        String value = person.age + "12";
        vars.put("value", value);
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
    public void testJfireEl_new()
    {
        lexer_new.calculate(vars);
    }

    @Benchmark
    public void testJfireEL_new_lambda()
    {
        lexer_new_lambda.calculate(vars);
    }

    @Benchmark
    public void testMethodCompile()
    {
        operandUseCompile.calculate(vars);
    }

    @Benchmark
    public void testMethodCompileDisableCompatible()
    {
        operandUseCompileDisableCompatible.calculate(vars);
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
}
