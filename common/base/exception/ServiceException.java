/*
 * @(#)ServiceException.java 1.0 2015/06/15
 * 
 * COPYRIGHT (C) 2013 WISEITECH CO., LTD. ALL RIGHTS RESERVED.
 */
package egovframework.common.base.exception;

/**
 * 서비스 오류 클래스이다.
 * 
 * @author 김은삼
 * @version 1.0 2015/06/15
 */
public class ServiceException extends BaseException {
    /**
     * 시리얼 버전 아이디
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 메시지를 인자로 가지는 생성자이다.
     * 
     * @param message 메시지
     */
    public ServiceException(String message) {
        super(message);
    }
    
    /**
     * 코드와 메시지를 인자로 가지는 생성자이다.
     * 
     * @param code 코드
     * @param message 메시지
     */
    public ServiceException(String code, String message) {
        super(code, message);
    }
}