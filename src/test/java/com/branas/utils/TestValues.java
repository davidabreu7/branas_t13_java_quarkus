package com.branas.utils;

public enum TestValues {

    VALID_NAME("John Doe"),
    INVALID_NAME("John"),
    VALID_CPF("95818705552"),
    INVALID_CPF("95818705500"),
    INVALID_EMAIL("john.doe@gmail"),
    ACCOUNT_EXISTS("jonh.doe@gmail.com"),
    VALID_PLATE("ABC1234"),
    INVALID_PLATE("ABC12345");

    private final String value;

    TestValues(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
