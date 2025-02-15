package com.example.oliveback.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    // ✅ 사용자를 찾을 수 없는 예외
    public static class UserNotFoundException extends CustomException {
        public UserNotFoundException() {
            super("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // ✅ 주문 수량이 잘못된 경우 예외
    public static class InvalidOrderException extends CustomException {
        public InvalidOrderException() {
            super("주문 수량은 1개 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ 관리자가 아닌 사용자가 전체 주문 조회 시 예외
    public static class AccessDeniedException extends CustomException {
        public AccessDeniedException() {
            super("관리자만 주문 전체 조회가 가능합니다.", HttpStatus.FORBIDDEN);
        }
    }

    // ✅ 상품이 존재하지 않는 경우 예외
    public static class ProductNotFoundException extends CustomException {
        public ProductNotFoundException() {
            super("해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // ✅ 이메일이 중복된 경우 예외
    public static class DuplicateEmailException extends CustomException {
        public DuplicateEmailException() {
            super("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ 사용자명이 중복된 경우 예외 (🚀 추가됨)
    public static class DuplicateUserException extends CustomException {
        public DuplicateUserException() {
            super("이미 존재하는 사용자 이름입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ 비밀번호가 짧거나 유효하지 않은 경우 예외 (🚀 추가됨)
    public static class InvalidPasswordException extends CustomException {
        public InvalidPasswordException() {
            super("비밀번호는 최소 6자 이상이어야 합니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    // ✅ 로그인하지 않은 사용자가 요청하는 경우 예외
    public static class NotLoggedInException extends CustomException {
        public NotLoggedInException() {
            super("로그인하지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}

