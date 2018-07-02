package com.pend.interfaces;

import com.pend.models.ExitPollVoteResponseModel;

public interface IExitPollVotingDialogCallBack {
    void onVotingOrUnVotingClick(ExitPollVoteResponseModel.ExitPollVoteDetails exitPollVoteDetails);
}
