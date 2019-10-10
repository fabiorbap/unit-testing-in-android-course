package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.NetworkException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseImplementationTest {

    private final static int REPUTATION = 10;

    FetchReputationUseCaseImplementation SUT;
    @Mock
    GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    @Before
    public void setup(){
        SUT = new FetchReputationUseCaseImplementation(getReputationHttpEndpointSync);
    }

//      1) If the server request completes successfully, then use case should indicate successful completion of the flow.
//      2) If the server request completes successfully, then the fetched reputation should be returned
//      3) If the server request fails for any reason, the use case should indicate that the flow failed.
//      4) If the server request fails for any reason, the returned reputation should be 0.

    // server completes successfully, returns SUCCESS

    @Test
    public void fetchReputation_success_successReturned() throws NetworkException {
        success();
        FetchReputationUseCaseImplementation.Result result = SUT.fetchReputation().result;
        assertThat(result, is(FetchReputationUseCaseImplementation.Result.SUCCESS));
    }

    // server completes successfully, returns reputation

    @Test
    public void fetchReputation_success_reputationReturned() throws NetworkException {
        success();
        int reputation = SUT.fetchReputation().reputation;
        assertThat(reputation, is(REPUTATION));
    }

    // network failure, returns NETWORK FAILURE

    @Test
    public void fetchReputation_networkFailure_networkErrorReturned() throws NetworkException {
        networkError();
        FetchReputationUseCaseImplementation.UseCaseResult result = SUT.fetchReputation();
        assertThat(result.result, is(FetchReputationUseCaseImplementation.Result.NETWORK_FAILURE));
    }

    // network failure, returns reputation 0

    @Test
    public void fetchReputation_networkFailure_noReputationReturned() throws NetworkException {
        networkError();
        FetchReputationUseCaseImplementation.UseCaseResult result = SUT.fetchReputation();
        assertThat(result.reputation, is(0));
    }

    // general error, returns GENERAL ERROR

    @Test
    public void fetchReputation_generalFailure_generalErrorReturned() throws NetworkException {
        generalError();
        FetchReputationUseCaseImplementation.UseCaseResult result = SUT.fetchReputation();
        assertThat(result.result, is(FetchReputationUseCaseImplementation.Result.GENERAL_ERROR));
    }

    // general error, returns reputation 0

    @Test
    public void fetchReputation_generalFailure_noReputationReturned() throws NetworkException {
        generalError();
        FetchReputationUseCaseImplementation.UseCaseResult result = SUT.fetchReputation();
        assertThat(result.reputation, is(0));
    }

    private void success() throws NetworkException {
        Mockito.when(getReputationHttpEndpointSync.getReputationSync()).thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS,
                REPUTATION));
    }

    private void networkError() throws NetworkException {
        Mockito.when(getReputationHttpEndpointSync.getReputationSync()).thenThrow(new NetworkException());
    }

    private void generalError() throws NetworkException {
        Mockito.when(getReputationHttpEndpointSync.getReputationSync()).thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                0));
    }

}
