package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.NetworkException;

class FetchReputationUseCaseImplementation {

    public enum Result {
        SUCCESS,
        NETWORK_FAILURE,
        GENERAL_ERROR
    }

    public class UseCaseResult {
        public Result result;
        public int reputation;

        UseCaseResult(Result result, int reputation) {
            this.result = result;
            this.reputation = reputation;
        }

    }

    private GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    FetchReputationUseCaseImplementation(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult fetchReputation() throws NetworkException{
        GetReputationHttpEndpointSync.EndpointResult result;
        try {
            result = getReputationHttpEndpointSync.getReputationSync();
        } catch (NetworkException e){
            return new UseCaseResult(Result.NETWORK_FAILURE, 0);
        }
        if (result.getReputation() == 0) {
            return new UseCaseResult(Result.GENERAL_ERROR, result.getReputation());
        }

        return new UseCaseResult(Result.SUCCESS, result.getReputation());
    }
}
