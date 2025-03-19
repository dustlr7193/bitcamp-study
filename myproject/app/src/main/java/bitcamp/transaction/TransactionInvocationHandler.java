package bitcamp.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

// 타겟 클레스의 메소드가 호출되면 메소드가 호출되면 대신 작업을 처리한다.
public class TransactionInvocationHandler implements InvocationHandler {

    private Object target;
    private Connection con;

    public TransactionInvocationHandler(Object target, Connection con) {
        this.target = target;
        this.con=con;
    }

    //타겟 객체에 대해 메소드를 호출하면 이 메소드가 호출된다.
    @Override
    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
        System.out.println("TransactionInvocationHandler.invoke() : "+ method.getName());

        Class<?> clazz = target.getClass();
        Method originMethod = clazz.getMethod(method.getName(),method.getParameterTypes());
        Transactional anno = originMethod.getAnnotation(Transactional.class);

        if (anno == null) {
            return method.invoke(target, args);
        }

        con.setAutoCommit(false);

        try {
            Object returnValue = method.invoke(target, args);
            con.commit();
            return returnValue;
        }catch (Exception e){
            con.rollback();
            throw e;
        }finally {
            con.setAutoCommit(true);
        }
    }
}
