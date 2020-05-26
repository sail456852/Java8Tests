package aop.jdk;


import java.lang.reflect.Method;

public class BeforeHandlerImpl extends BeforeHandler {

    @Override
    public void handlerBefore(Object proxy, Method method, Object[] args) {
        System.out.println("BeforeHandlerImpl.handlerAfter");
    }
}
