package com.jfireframework.jfireel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ PlusTest.class, //
        PropertyTest.class, //
        MethodTest.class, //
        MinusTest.class, //
        DivisionTest.class, //
        BracketTest.class, //
        CompareTest.class, //
        TypeTest.class, //
        QuestionTest.class//
})
@RunWith(value = org.junit.runners.Suite.class)
public class SuiteTest
{
	
}
