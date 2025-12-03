package cc.jfire.el;

import lombok.Data;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public abstract class TestSupport
{
    public    Map<String, Object> vars = new HashMap<String, Object>();
    protected Person              person;
    protected Home                home;

    @Before
    public void before()
    {
        home        = new Home();
        person      = new Person();
        person.age  = 14;
        home.person = person;
        vars.put("person", person);
        vars.put("home", home);
        vars.put("l", Long.valueOf(5));
    }

    @Data
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

        public void setPerson(Person person)
        {
            this.person = person;
        }
    }

    @Data
    public static class Person
    {
        public int age;

        public Person(int age)
        {
            this.age = age;
        }

        public Person()
        {

        }

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
        {
            this.age = age;
        }

        public String reFirst(String a, String b)
        {
            return a;
        }
    }
}
