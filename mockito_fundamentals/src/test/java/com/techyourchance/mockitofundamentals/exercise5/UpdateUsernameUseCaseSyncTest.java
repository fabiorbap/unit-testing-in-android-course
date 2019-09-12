package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UpdateUsernameUseCaseSyncTest {

    UpdateUsernameUseCaseSync SUT;
    UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSync;
    UsersCache usersCache;
    EventBusPoster eventBusPoster;
    private final static String USER_NAME = "user_name";
    private final static String USER_ID = "user_id";

    @Before
    private void setup(){
        updateUsernameHttpEndpointSync = Mockito.mock(UpdateUsernameHttpEndpointSync.class);
        usersCache = Mockito.mock(UsersCache.class);
        eventBusPoster = Mockito.mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSync, usersCache, eventBusPoster);
    }


    // arguments passed correctly
    // user cached successfully
    // user not cached, auth error
    // user not cached, general error
    // user not cached, network error
    // user not cached, server error
    // use case failure, general error
    // use case failure, auth error
    // use case failure, server error
    // use case failure, network error
    // event bus not posted, auth error
    // event bus not posted, network error
    // event bus not posted, general error
    // event bus not posted, server error

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
        Mockito.when(updateUsernameHttpEndpointSync.updateUsername(USER_ID, USER_NAME)).thenThrow(
                new NetworkErrorException());
    }

}