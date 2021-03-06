package com.jfireframework.jfireel;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;

public abstract class TestSupport
{
    public static class Home
    {
        Person person;
        
        public Person getPerson()
        {
            return person;
        }
        
        public int personAge(Person person)
        {
            return person.getAge();
        }
        
        public int personAge2(int age)
        {
            return age;
        }
        
        public String name(String name)
        {
            return name;
        }
        
        public boolean bool(boolean b)
        {
            return b;
        }
        
        public int plus(int a, int b)
        {
            return a + b;
        }
    }
    
    public static class Person
    {
        int age;
        
        public int getAge()
        {
            return age;
        }
    }
    
    protected Person           person;
    protected Home             home;
    public Map<String, Object> vars = new HashMap<String, Object>();
    
    @Before
    public void before()
    {
        home = new Home();
        person = new Person();
        person.age = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
    }
}
