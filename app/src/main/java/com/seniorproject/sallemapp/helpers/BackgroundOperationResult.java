package com.seniorproject.sallemapp.helpers;

/**
 * Created by abdul on 23-Apr-2017.
 */

public class BackgroundOperationResult {
    private boolean mSucceed;
    public boolean getSucceed(){
        return mSucceed;
    }
    public BackgroundOperationResult(boolean succeed){
        this.mSucceed = succeed;
    }
}
