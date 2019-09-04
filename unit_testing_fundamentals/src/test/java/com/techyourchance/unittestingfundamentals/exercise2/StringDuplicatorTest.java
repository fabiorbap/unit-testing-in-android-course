package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDuplicatorTest {

    private StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String duplicatedString = SUT.duplicate("");
        assertThat(duplicatedString, is(""));
    }

    @Test
    public void duplicate_singleCharacterString_duplicatedStringReturned() {
        String duplicatedString = SUT.duplicate("a");
        assertThat(duplicatedString, is("aa"));
    }

    @Test
    public void duplicate_multipleCharacterString_duplicatedStringReturned() {
        String duplicatedString = SUT.duplicate("ab");
        assertThat(duplicatedString, is("abab"));
    }
}