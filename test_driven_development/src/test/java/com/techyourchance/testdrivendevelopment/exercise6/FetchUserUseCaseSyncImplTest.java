package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchUserUseCaseSyncImplTest {

    private static String USER_ID = "user_id";
    private static String USER_NAME = "user_name";

    private FetchUserUseCaseSync SUT;

    @Mock
    UsersCache usersCache;

    @Mock
    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;

    @Before
    void setup() {
        SUT = new FetchUserUseCaseSyncImpl();
    }

    /*1) If the user with given user ID is not in the cache then it should be fetched from the server.
            2) If the user fetched from the server then it should be stored in the cache before returning to the caller.
            3) If the user is in the cache then cached record should be returned without polling the server.*/

    // Passed parameters to function

    @Before
    void fetchUser_passedParameters_success() {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        verify(SUT, times(1)).fetchUserSync(ac.capture());
        String captures = ac.getValue();
        assertThat(captures, is(USER_ID));
    }

    // fetch user success, user stored in cache
    @Before
    void fetchUser_success_userCached() {

    }

    // fetch user success, user already in cache
    // fetch user success, success returned
    // fetch user server error, server failure returned
    // fetch user network error, network error returned
    // fetch user general error, general error returned

    private void success() throws NetworkErrorException {
        when(fetchUserHttpEndpointSync.fetchUserSync(USER_ID)).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.SUCCESS,
                USER_ID, USER_NAME));
    }

    private void generalError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSync.fetchUserSync(USER_ID)).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                USER_ID, USER_NAME));
    }

    private void authError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSync.fetchUserSync(USER_ID)).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR,
                USER_ID, USER_NAME));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException()).when(fetchUserHttpEndpointSync.fetchUserSync(any(String.class)));
    }

}