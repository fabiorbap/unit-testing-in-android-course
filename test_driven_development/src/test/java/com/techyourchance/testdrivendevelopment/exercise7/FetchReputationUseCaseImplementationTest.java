package com.techyourchance.testdrivendevelopment.exercise7;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseImplementationTest {

    FetchReputationUseCaseImplementation SUT;

    @Before
    public void setup(){
        SUT = new FetchReputationUseCaseImplementation();
    }

//      1) If the server request completes successfully, then use case should indicate successful completion of the flow.
//      2) If the server request completes successfully, then the fetched reputation should be returned
//      3) If the server request fails for any reason, the use case should indicate that the flow failed.
//      4) If the server request fails for any reason, the returned reputation should be 0.

    // server completes successfully, returns SUCCESS
    // server completes successfully, returns reputation
    // network failure, returns NETWORK FAILURE
    // network failure, returns reputation 0
    // general error, returns GENERAL ERROR
    // general error, returns reputation 0

}
