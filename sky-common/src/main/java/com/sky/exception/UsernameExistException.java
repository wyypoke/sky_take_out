package com.sky.exception;

public class UsernameExistException extends BaseException {
    UsernameExistException(String msg) { super(msg); }
    UsernameExistException() {}
}
