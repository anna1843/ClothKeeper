package org.project.dev.member.exception;

public class RuntimeException extends Exception{
    // serialVersionUID 구하는 방법 : entity 클래스에서 MemberEntity implements Serializable 해서 MemberEntity에 마우스 오버하면 생성 가능
    static final long serialVersionUID = 6040416510588461198L;

    public RuntimeException(){
        super();
    }

    public RuntimeException(String message){
        super(message);
    }

    public RuntimeException(String message, Throwable cause){
        super(message, cause);
    }

    public RuntimeException(Throwable cause){
        super(cause);
    }

    protected RuntimeException(String message, Throwable casuse,
                               boolean enableSuppression,
                               boolean writableStackTrace){
        super(message, casuse, enableSuppression, writableStackTrace);
    }
}
