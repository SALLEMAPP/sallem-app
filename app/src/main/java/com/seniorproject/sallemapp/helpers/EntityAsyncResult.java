package com.seniorproject.sallemapp.helpers;

/**
 * Created by abdul on 09-Mar-2017.
 */

public interface EntityAsyncResult<T> {
    void processFinish(T result);
}
