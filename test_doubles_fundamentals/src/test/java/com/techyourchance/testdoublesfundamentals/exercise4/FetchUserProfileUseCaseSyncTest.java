package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;

public class FetchUserProfileUseCaseSyncTest {

    private FetchUserProfileUseCaseSync SUT;
    private UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    private UsersCacheTd usersCacheTd;
    private final static String FULL_NAME = "full_name";
    private final static String IMAGE_URL= "image_url";
    private final static String USER_ID = "user_id";

    @Before
    public void setup(){
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }

    // userId is passed into getUserProfile

    @Test
    public void getUserProfile_success_userIdPassedToEndpoint(){
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertThat(userProfileHttpEndpointSyncTd.userId, is(USER_ID));
    }

    // login successful and stored in cache

    @Test
    public void getUserProfile_success_userStoredInCache(){
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getFullName(), is(FULL_NAME));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getImageUrl(), is(IMAGE_URL));
    }

    // login is successful and returns success

    @Test
    public void getUserProfile_success_successReturned(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        Assert.assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
    }

    // login fails due to auth error

    @Test
    public void getUserProfile_failure_authError(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isAuthError = true;
        Assert.assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    // login fails due to generic error

    @Test
    public void getUserProfile_failure_genericError(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isGeneralError = true;
        Assert.assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    // login fails due to server error

    @Test
    public void getUserProfile_failure_serverError(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isServerError = true;
        Assert.assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    // login fails due to network

    @Test
    public void getUserProfile_failure_networkError(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isNetworkError = true;
        Assert.assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    // login fails due to auth error

    @Test
    public void getUserProfile_failure_userNotCachedAuthError(){
        SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isAuthError = true;
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getFullName(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getImageUrl(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getUserId(), is(""));
    }

    // login fails due to generic error

    @Test
    public void getUserProfile_failure_userNotCachedGeneralError(){
        SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isGeneralError = true;
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getFullName(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getImageUrl(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getUserId(), is(""));
    }

    // login fails due to server error

    @Test
    public void getUserProfile_failure_userNotCachedServerError(){
        SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isServerError = true;
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getFullName(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getImageUrl(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getUserId(), is(""));
    }

    // login fails due to network

    @Test
    public void getUserProfile_failure_userNotCachedNetworkError(){
        SUT.fetchUserProfileSync(USER_ID);
        userProfileHttpEndpointSyncTd.isNetworkError = true;
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getFullName(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getImageUrl(), is(""));
        Assert.assertThat(Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getUserId(), is(""));
    }



    private class UserProfileHttpEndpointSyncTd implements  UserProfileHttpEndpointSync {

        boolean isServerError;
        boolean isGeneralError;
        boolean isAuthError;
        boolean isNetworkError;
        String userId;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.userId = userId;
            if (isServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, this.userId, "", "");
            } else if (isGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, this.userId, "", "");
            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, this.userId, "", "");
            } else if (isNetworkError){
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, this.userId, FULL_NAME, IMAGE_URL);
            }
        }
    }

    private class UsersCacheTd implements UsersCache {

        private User user = new User("", "", "");

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