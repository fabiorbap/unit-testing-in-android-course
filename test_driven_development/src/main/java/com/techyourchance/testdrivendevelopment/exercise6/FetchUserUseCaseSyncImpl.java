package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private UsersCache usersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        FetchUserHttpEndpointSync.EndpointResult result;

        User user;

        if (usersCache.getUser(userId) == null) {
            try {
                result = fetchUserHttpEndpointSync.fetchUserSync(userId);
            } catch (NetworkErrorException e) {
                return new UseCaseResult(Status.NETWORK_ERROR, null);
            }

            if (result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR
                    || result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR) {
                return new UseCaseResult(Status.FAILURE, null);
            } else {
                usersCache.cacheUser(new User(result.getUserId(), result.getUsername()));
                user = usersCache.getUser(result.getUserId());
            }
        } else {
            user = usersCache.getUser(userId);
        }

        return new UseCaseResult(Status.SUCCESS, user);
    }
}
