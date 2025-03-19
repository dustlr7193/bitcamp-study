package bitcamp.myapp.listener;

import bitcamp.myapp.dao.MySQLBoardDao;
import bitcamp.myapp.dao.MySQLBoardFileDao;
import bitcamp.myapp.dao.MySQLMemberDao;
import bitcamp.myapp.service.*;
import bitcamp.transaction.TransactionProxyFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

    private Connection con;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 1. JDBC Driver 로딩(java.sql.Driver 구현체 로딩)
//      Class.forName("com.mysql.jdbc.Driver");

            // 2. Driver 구현 객체 생성
//      Driver driver = new com.mysql.jdbc.Driver();

            // 3. Driver 객체를 JDBC 드라이버 관리자에 등록
//      DriverManager.registerDriver(driver);

            // 4. DB에 연결


            String userHome = System.getProperty("user.home");
            Properties appProps = new Properties();
            appProps.load(new FileReader(userHome + "/config/bitcamp-study.properties"));

            System.out.println(appProps.getProperty("ncp.end-point"));

            con = DriverManager.getConnection(
                    appProps.getProperty("jdbc.url"),
                    appProps.getProperty("jdbc.username"),
                    appProps.getProperty("jdbc.password"));

            ServletContext ctx = sce.getServletContext();

            MySQLMemberDao memberDao = new MySQLMemberDao(con);
            MySQLBoardDao boardDao = new MySQLBoardDao(con);
            MySQLBoardFileDao boardFileDao = new MySQLBoardFileDao(con);

            //서비스 객체의 트랜젝션을 처리할 프로시 객체 생성기
            TransactionProxyFactory transactionProxyFactory = new TransactionProxyFactory(con);

            DefaultMemberService memberService = new DefaultMemberService(memberDao);
            ctx.setAttribute("memberService",
                    transactionProxyFactory.createTransactionProxy(memberService, MemberService.class));

            DefaultBoardService boardService = new DefaultBoardService(boardDao, boardFileDao,con);
            ctx.setAttribute("boardService",
                    transactionProxyFactory.createTransactionProxy(boardService, BoardService.class));

            NCPObjectStorageService storageService = new NCPObjectStorageService(appProps);
            ctx.setAttribute("storageService", storageService);

            System.out.println("웹애플리케이션 실행 환경 준비!");

        } catch (Exception e) {
            System.out.println("웹애플리케이션 실행 환경 준비 중 오류 발생!");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("웹애플리케이션 실행 환경 해제 중 오류 발생!");
            e.printStackTrace();
        }
    }
}
