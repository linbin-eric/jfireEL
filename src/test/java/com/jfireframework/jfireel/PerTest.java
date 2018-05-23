package com.jfireframework.jfireel;

import java.io.IOException;
import java.util.Map;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.exception.ScriptEvalError;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.baseutil.time.Timewatch;
import com.jfireframework.jfireel.Lexer;
import com.jfireframework.jfireel.util.Functional;

public class PerTest extends TestSupport
{
	@Test
	public void test() throws IOException, ScriptEvalError
	{
		String value = person.age + "12";
		vars.put("value", value);
		StandardEvaluationContext societyContext = new StandardEvaluationContext(this);
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("vars['home'].bool(vars['person'].getAge() + '12' != vars['value'])");
		Object springElResult = exp.getValue(societyContext);
		System.out.println(springElResult);
		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Map runScript = gt.runScript("return @home.bool(@person.getAge()+'12'!=value);", vars);
		System.out.println(runScript);
		
		Lexer lexer = Lexer.parse("home.bool(person.getAge() + '12' != value)");
		Lexer lexer2 = Lexer.parse("home.bool(person.getAge() + '12' != value)", Functional.build().setMethodInvokeByCompile(true).toFunction());
		int preheat = 100;
		int count = 10000000;
		for (int i = 0; i < preheat; i++)
		{
			lexer.calculate(vars);
			lexer2.calculate(vars);
			exp.getValue(societyContext);
			gt.runScript("return @home.bool(@person.getAge()+'12'!=value);", vars);
		}
		long t0 = System.nanoTime();
		for (int i = 0; i < count; i++)
		{
			lexer.calculate(vars);
		}
		long t1 = System.nanoTime();
		System.out.println("默认jfireEl计算" + (count / 10000) + "万次耗时:" + (t1 - t0) / 1000 / 1000 + "毫秒");
		t0 = System.nanoTime();
		for (int i = 0; i < count; i++)
		{
			lexer2.calculate(vars);
		}
		t1 = System.nanoTime();
		System.out.println("编译优化jfireEl计算" + (count / 10000) + "万次耗时:" + (t1 - t0) / 1000 / 1000 + "毫秒");
		t0 = System.nanoTime();
		for (int i = 0; i < count; i++)
		{
			gt.runScript("return @home.bool(@person.getAge()+'12'!=value);", vars);
		}
		t1 = System.nanoTime();
		System.out.println("BeetlEl计算" + (count / 10000) + "万次耗时:" + (t1 - t0) / 1000 / 1000 + "毫秒");
		t0 = System.nanoTime();
		for (int i = 0; i < count; i++)
		{
			exp.getValue(societyContext);
		}
		t1 = System.nanoTime();
		System.out.println("springEl计算" + (count / 10000) + "万次耗时:" + (t1 - t0) / 1000 / 1000 + "毫秒");
		t0 = System.nanoTime();
		for (int i = 0; i < count; i++)
		{
			home.bool((person.getAge() + "12").equals(value) == false);
		}
		t1 = System.nanoTime();
		System.out.println("源代码直接计算" + (count / 10000) + "万次耗时:" + (t1 - t0) / 1000 / 1000 + "毫秒");
	}
	
	@Test
	public void test2()
	{
		Lexer lexer = Lexer.parse("person.age");
		Lexer lexer2 = Lexer.parse("person.age", Functional.build().setPropertyFetchByUnsafe(true).toFunction());
		int preheat = 1000;
		int count = 100000000;
		for (int i = 0; i < preheat; i++)
		{
			lexer.calculate(vars);
			lexer2.calculate(vars);
		}
		Timewatch timewatch = new Timewatch();
		timewatch.start();
		for (int i = 0; i < count; i++)
		{
			lexer.calculate(vars);
		}
		timewatch.end();
		System.out.println(StringUtil.format("反射模式计算:{}W次耗时:{}", count/10000, timewatch.getTotal()));
		timewatch.start();
		for (int i = 0; i < count; i++)
		{
			lexer2.calculate(vars);
		}
		timewatch.end();
		System.out.println(StringUtil.format("Unsafe模式计算:{}W次耗时:{}", count/10000, timewatch.getTotal()));
		
	}
}
