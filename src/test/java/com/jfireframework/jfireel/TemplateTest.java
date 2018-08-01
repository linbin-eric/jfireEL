package com.jfireframework.jfireel;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.jfireframework.jfireel.template.Template;

public class TemplateTest
{
    @Test
    public void test()
    {
        Template parse = Template.parse("hello ${name},my age is ${age+2}");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello ll,my age is 12", parse.render(params));
    }
    
    @Test
    public void test2()
    {
        Template parse = Template.parse("hello ${name}");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        assertEquals("hello ll", parse.render(params));
    }
    
    @Test
    public void test3()
    {
        Template syntax = Template.parse("hello,<%if(age>2){%> my name is ${name}<%}%>  ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello, my name is ll  ", syntax.render(params));
    }
    
    @Test
    public void test4()
    {
        Template syntax = Template.parse("hello,<%if(age>2){%> my name is <%if(name=='ll'){%>${name}<%}%><%}%>");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 10);
        assertEquals("hello, my name is ll", syntax.render(params));
    }
    
    @Test
    public void test5()
    {
        Template syntax = Template.parse("hello,<%if(age>2){%> ${name} <%}%><%else{%> ll<%}%>");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 1);
        assertEquals("hello, ll", syntax.render(params));
    }
    
    @Test
    public void test6()
    {
        Template syntax = Template.parse("hello,<%if(age>2){%> ${name} <% } else {%> ll<%}%>");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 1);
        assertEquals("hello, ll", syntax.render(params));
    }
    
    @Test
    public void test7()
    {
        Template syntax = Template.parse("hello,<%if(age>10){%> ${name} <%} else if(age>5){%> age >5 <%} else {%> age<5<%}%>");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ll");
        params.put("age", 1);
        assertEquals("hello, age<5", syntax.render(params));
    }
    
    @Test
    public void test8()
    {
        List<String> list = new LinkedList<String>();
        list.add("name1");
        list.add("name2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("list", list);
        Template syntax = Template.parse("hello,<% for (name in list) {%> ${name}<%}%>");
        assertEquals("hello, name1 name2", syntax.render(params));
    }
    
    @Test
    public void test9()
    {
        List<String> list = new LinkedList<String>();
        list.add("name1");
        list.add("name2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("list", list);
        Template syntax = Template.parse("hello,<% for (name in list) {%><% if(name=='name1'){%> ${name}<%}%><%}%>");
        assertEquals("hello, name1", syntax.render(params));
    }
    
    @Test
    public void test10()
    {
        Template template = Template.parse("<% if(age>10){ for(name in list){ %> ${name} <%}}%>");
        List<String> list = new LinkedList<String>();
        list.add("name1");
        list.add("name2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("list", list);
        params.put("age", 11);
        assertEquals(" name1  name2 ", template.render(params));
    }
}
