package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }

    //interval1 before interval2

    @Test
    public void isAdjacent_interval1BeforeInterval2_returnFalse() {
        Interval interval1 = new Interval(0, 1);
        Interval interval2 = new Interval(2, 3);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }


    //interval1 adjacent interval2 before

    @Test
    public void isAdjacent_interval1AdjacentBeforeInterval2_returnTrue() {
        Interval interval1 = new Interval(0, 1);
        Interval interval2 = new Interval(1, 3);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(true));
    }

    //interval1 overlaps interval2 before

    @Test
    public void isAdjacent_interval1OverlapsBeforeInterval2_returnFalse() {
        Interval interval1 = new Interval(0, 1);
        Interval interval2 = new Interval(0, 3);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 is contained in interval2
    @Test
    public void isAdjacent_interval1IsContainedInInterval2_returnFalse() {
        Interval interval1 = new Interval(0, 1);
        Interval interval2 = new Interval(-1, 3);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 overlaps interval2 after

    @Test
    public void isAdjacent_interval1OverlapsAfterInterval2_returnFalse() {
        Interval interval1 = new Interval(0, 2);
        Interval interval2 = new Interval(-1, 1);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 adjacent interval2 after

    @Test
    public void isAdjacent_interval1AdjacentAfterInterval2_returnTrue() {
        Interval interval1 = new Interval(0, 2);
        Interval interval2 = new Interval(-1, 0);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(true));
    }

    //interval1 after interval2

    @Test
    public void isAdjacent_interval1AfterInterval2_returnFalse() {
        Interval interval1 = new Interval(2, 4);
        Interval interval2 = new Interval(-1, 0);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 contains interval2

    @Test
    public void isAdjacent_interval1ContainsInterval2_returnFalse() {
        Interval interval1 = new Interval(2, 6);
        Interval interval2 = new Interval(3, 4);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 equals interval2

    @Test
    public void isAdjacent_interval1EqualsInterval2_returnFalse() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(1, 2);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 adjacent before and contained in interval2

    @Test
    public void isAdjacent_interval1AdjacentBeforeAndContainedInInterval2_returnFalse() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(1, 5);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval1 adjacent after and contained in interval2

    @Test
    public void isAdjacent_interval1AdjacentAfterAndContainedInInterval2_returnFalse() {
        Interval interval1 = new Interval(3, 5);
        Interval interval2 = new Interval(1, 5);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval2 adjacent before and contained in interval1

    @Test
    public void isAdjacent_interval2AdjacentBeforeAndContainedInInterval1_returnFalse() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(1, 2);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }

    //interval2 adjacent after and contained in interval1

    @Test
    public void isAdjacent_interval2AdjacentAfterAndContainedInInterval1_returnFalse() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(3, 5);
        boolean isAdjacent = SUT.isAdjacent(interval1, interval2);
        assertThat(isAdjacent, is(false));
    }


}