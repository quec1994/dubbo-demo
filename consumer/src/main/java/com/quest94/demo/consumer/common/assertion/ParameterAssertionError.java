package com.quest94.demo.consumer.common.assertion;

public class ParameterAssertionError extends AssertionError {

    public ParameterAssertionError(String parameterName, String expectMessage, String hintMessage) {
        super("[parameter failed] - parameter " + parameterName + " must " + expectMessage + "; " + hintMessage);
    }

}
