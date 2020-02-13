package com.jfirer.jfireel;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public abstract class TestSupport
{
    public static class Home
    {
        public Person person;

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
        public int age;

        public int getAge()
        {
            return age;
        }
    }

    protected Person              person;
    protected Home                home;
    public    Map<String, Object> vars = new HashMap<String, Object>();

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
