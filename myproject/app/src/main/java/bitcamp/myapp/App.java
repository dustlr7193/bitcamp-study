package bitcamp.myapp;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication // Gradle에서 bootRun 태스크를 수행할 때 실행시킬 메인 클래스를 지정하는 애노테이션
@PropertySource("file:${user.home}/config/bitcamp-study.properties")

// DAO 구현체 자동 생성을 설정하는 방법 1 : 애노테이션으로 설정 하기
@MapperScan("bitcamp.myapp.dao")
//@Transactional 애노테이션 활성화
//@EnableTransactionManagement
public class App {

  //DAO 구현체 자동 생성을 설정하는 방법 2: 자바코드로 설정하기
  // - 다음 메서드를 통해서 DAO 인터페이스의 구현체를 자동으로 생성해주는 일을 하는 객체를 준비한다.
//  @Bean
//  public MapperScannerConfigurer mapperScannerConfigurer() {
//    MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//    configurer.setBasePackage("bitcamp.myapp.dao");  //dao 인터페이스 패키지 설정.
//    //단, sql 매퍼 파일(*Dao.xml)의 namespace가 인터페이스의 full-qualified name과 일치해야 한다.
//    //그리고 SQL id와 인터페이스의 메서드 이름과 일치 해야 한다.
//    //메소드의 파라미터 값이 두 개 이상일 경우, @Param 애노테이션으로 이름을 명확히 설정해야 한다.
//    //@Params 애노테이션으로 프로퍼티 이름을 명시해야 한다.
//    return configurer;
//  }

  public static void main(String[] args) {
    System.out.println("App 실행!");
    SpringApplication.run(App.class, args);
  }
}
