package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class NegativeNumberValidatorTest {

    private NegativeNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void testNegativeNumbers(){
        boolean isNegative = SUT.isNegative(-1);
        Assert.assertThat(isNegative, is(true));
    }

    @Test
    public void testPositiveNumbers(){
        boolean isNegative = SUT.isNegative(1);
        Assert.assertThat(isNegative, is(false));
    }

    @Test
    public void testZero(){
        boolean isNegative = SUT.isNegative(0);
        Assert.assertThat(isNegative, is(false));
    }

}