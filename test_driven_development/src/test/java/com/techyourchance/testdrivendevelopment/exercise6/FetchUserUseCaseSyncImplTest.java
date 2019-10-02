package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImplTest {

    private static String USER_ID = "user_id";
    private static String USER_NAME = "user_name";
    private User user = new User(USER_NAME, USER_ID);

    private FetchUserUseCaseSync SUT;

    @Mock
    UsersCache usersCache;

    @Mock
    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;

    @Before
    public void setup() throws NetworkErrorException {
        SUT = new FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSync, usersCache);
        success();
    }

    // Passed parameters to function

    @Test
    public void fetchUser_passedParameters_success() {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        verify(SUT, times(1)).fetchUserSync(ac.capture());
        String captures = ac.getValue();
        assertThat(captures, is(USER_ID));
    }

    // fetch user success, user stored in cache
    @Test
    public void fetchUser_success_userCached() {
        dontCacheUser();
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(USER_ID);
        verify(usersCache, times(2)).cacheUser(ac.capture());
        User capture = ac.getValue();
        assertThat(capture.getUserId(), is(USER_ID));
        assertThat(capture.getUsername(), is(USER_NAME));
    }

    // fetch user success, user already in cache

    @Test
    public void fetchUser_success_userCorrectlyCached() {
        cacheUser();
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(any(String.class));
        verify(usersCache, times(0)).cacheUser(ac.capture());
    }

    // fetch user success, success returned

    @Test
    public void fetchUser_success_successReturned() throws NetworkErrorException {
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        verify(fetchUserHttpEndpointSync).fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
    }

    // fetch user auth error, server failure returned

    @Test
    public void fetchUser_authError_errorReturned() throws NetworkErrorException {
        authError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        verify(fetchUserHttpEndpointSync).fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
    }

    // fetch user network error, network error returned

    @Test
    public void fetchUser_networkError_errorReturned() throws NetworkErrorException {
        networkError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        verify(fetchUserHttpEndpointSync).fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.NETWORK_ERROR));
    }

    // fetch user general error, general error returned

    @Test
    public void fetchUser_generalError_errorReturned() throws NetworkErrorException {
        generalError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        verify(fetchUserHttpEndpointSync).fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
    }

    private void success() throws NetworkErrorException {
        when(fetchUserHttpEndpointSync.fetchUserSync(any(String.class))).thenReturn(
                new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.SUCCESS,
                USER_ID, USER_NAME));
    }

    private void generalError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSync.fetchUserSync(any(String.class))).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                USER_ID, USER_NAME));
    }

    private void authError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSync.fetchUserSync(any(String.class))).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR,
                USER_ID, USER_NAME));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException()).when(fetchUserHttpEndpointSync.fetchUserSync(any(String.class)));
    }

    private void cacheUser() {
        when(usersCache.getUser(any(String.class))).thenReturn(user);
    }

    private void dontCacheUser() {
        when(usersCache.getUser(any(String.class))).thenReturn(null);
    }

}