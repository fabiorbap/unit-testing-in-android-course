package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class UpdateUsernameUseCaseSyncTest {

    UpdateUsernameUseCaseSync SUT;
    UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSync;
    UsersCache usersCache;
    EventBusPoster eventBusPoster;
    private final static String USER_NAME = "user_name";
    private final static String USER_ID = "user_id";

    @Before
    public void setup() throws NetworkErrorException {
        updateUsernameHttpEndpointSync = Mockito.mock(UpdateUsernameHttpEndpointSync.class);
        usersCache = Mockito.mock(UsersCache.class);
        eventBusPoster = Mockito.mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSync, usersCache, eventBusPoster);
        success();
    }


    // arguments passed correctly

    @Test
    public void updateUsername_success_argumentsPassed() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verify(updateUsernameHttpEndpointSync, times(1)).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USER_ID));
        assertThat(captures.get(1), is(USER_NAME));
    }

    // user cached successfully

    @Test
    public void updateUsername_success_authTokenCached() throws NetworkErrorException {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verify(usersCache).cacheUser(ac.capture());
        User capture = ac.getValue();
        assertThat(capture.getUserId(), is(USER_ID));
        assertThat(capture.getUsername(), is(USER_NAME));
    }

    // user not cached, auth error

    @Test
    public void updateUsername_authError_authTokenNotCached() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(usersCache);
    }

    // user not cached, general error

    @Test
    public void updateUsername_generalError_authTokenNotCached() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(usersCache);
    }

    // user not cached, network error

    @Test
    public void updateUsername_networkError_authTokenNotCached() throws NetworkErrorException {
        networkError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(usersCache);
    }

    // user not cached, server error

    @Test
    public void updateUsername_serverError_authTokenNotCached() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(usersCache);
    }

    // user case success

    @Test
    public void updateUsername_success_useCaseSuccessReturned() throws NetworkErrorException {
        success();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    // use case failure, general error

    @Test
    public void updateUsername_generalError_useCaseFailureReturned() throws NetworkErrorException {
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    // use case failure, auth error

    @Test
    public void updateUsername_authError_useCaseFailureReturned() throws NetworkErrorException {
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    // use case failure, server error

    @Test
    public void updateUsername_serverError_useCaseFailureReturned() throws NetworkErrorException {
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    // use case failure, network error

    @Test
    public void updateUsername_networkError_useCaseFailureReturned() throws NetworkErrorException {
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    // event bus posted

    @Test
    public void updateUsername_success_eventBusPosted() throws NetworkErrorException {
        success();
        ArgumentCaptor<UserDetailsChangedEvent> ac = ArgumentCaptor.forClass(UserDetailsChangedEvent.class);
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verify(eventBusPoster).postEvent(ac.capture());
        UserDetailsChangedEvent capture = ac.getValue();
        assertThat(capture, CoreMatchers.<UserDetailsChangedEvent>instanceOf(UserDetailsChangedEvent.class));
    }

    // event bus not posted, auth error

    @Test
    public void updateUsername_authError_eventNotPosted() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(eventBusPoster);
    }

    // event bus not posted, network error

    @Test
    public void updateUsername_networkError_eventNotPosted() throws NetworkErrorException {
        networkError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(eventBusPoster);
    }

    // event bus not posted, general error

    @Test
    public void updateUsername_generalError_eventNotPosted() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(eventBusPoster);
    }

    // event bus not posted, server error

    @Test
    public void updateUsername_serverError_eventNotPosted() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(eventBusPoster);
    }

    private void success() throws NetworkErrorException {
        Mockito.when(updateUsernameHttpEndpointSync.updateUsername(USER_ID, USER_NAME)).thenReturn(
                new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USER_ID,
                        USER_NAME));
    }

    private void generalError() throws NetworkErrorException {
        Mockito.when(updateUsernameHttpEndpointSync.updateUsername(USER_ID, USER_NAME)).thenReturn(
                new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "",
                        ""));
    }

    private void authError() throws NetworkErrorException {
        Mockito.when(updateUsernameHttpEndpointSync.updateUsername(USER_ID, USER_NAME)).thenReturn(
                new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, "",
                        ""));
    }

    private void serverError() throws NetworkErrorException {
        Mockito.when(updateUsernameHttpEndpointSync.updateUsername(USER_ID, USER_NAME)).thenReturn(
                new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "",
                        ""));
    }

    private void networkError() throws NetworkErrorException {
        Mockito.doThrow(new NetworkErrorException())
                .when(updateUsernameHttpEndpointSync).updateUsername(any(String.class), any(String.class));
    }

}