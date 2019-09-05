package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;

public class FetchUserProfileUseCaseSyncTest {

    private FetchUserProfileUseCaseSync SUT;
    private UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    private UsersCacheTd usersCacheTd;

    @Before
    public void setup(){
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }

    // login successful and stored in cache
    // login successfully but user is not stored in cache
    // login fails due to username
    // login fails due to network

    private class UserProfileHttpEndpointSyncTd implements  UserProfileHttpEndpointSync {

        boolean isServerError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            if (isServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, userId, "", "");
            }
            return null;
        }
    }

    private class UsersCacheTd implements UsersCache {

        private User user;

        @Override
        public void cacheUser(User user) {
            this.user = user;
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return user;
        }
    }
}