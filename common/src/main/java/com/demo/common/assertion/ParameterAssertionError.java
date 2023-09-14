package com.demo.common.assertion;

public class ParameterAssertionError extends java.lang.AssertionError {

    public ParameterAssertionError(String parameterName, String expectMessage, String hintMessage) {
        super("[parameter failed] - parameter " + parameterName + " must " + expectMessage + "; " + hintMessage);
    }

}
