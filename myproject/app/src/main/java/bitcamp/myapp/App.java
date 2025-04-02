package bitcamp.myapp;



import bitcamp.myapp.common.LoginUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;


@SpringBootApplication // Gradle에서 bootRun 태스크를 수행할 때 실행시킬 메인 클래스를 지정하는 애노테이션
@PropertySource("file:${user.home}/config/bitcamp-study.properties")
public class App implements WebMvcConfigurer {
    // Web mvc 관련 추가 설정은 WebMvcConfigurer 구현체의 메서드를 통해 설정한다.
    //예) HandlerInterceptor, HandlerMethodArgumentResolver 등



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
  @Value("${jdbc.driver}")
  private String driver;
  @Value("${jdbc.url}")
  private String url;
  @Value("${jdbc.username}")
  private String username;
  @Value("${jdbc.password}")
  private String password;

  // 스프링부트의 프로퍼터 값(application.properties,yaml) 의 값을 커스터마이징 할 때 사용 하는 객체
  private final ConfigurableEnvironment configurableEnvironment;

    public App(ConfigurableEnvironment configurableEnvironment) {
        this.configurableEnvironment = configurableEnvironment;
    }

  @PostConstruct    // 생성자 호출 후에 스프링 IoC 컨테이너가 호출하는 메서드
  public void init() throws Exception {
      HashMap<String, Object> datasourceProperties = new HashMap<>();
      datasourceProperties.put("spring.datasource.driver-class-name", driver);
      datasourceProperties.put("spring.datasource.url", url);
      datasourceProperties.put("spring.datasource.username", username);
      datasourceProperties.put("spring.datasource.password", password);

      MapPropertySource propertySource =new MapPropertySource("dynamicProperties",datasourceProperties);
      configurableEnvironment.getPropertySources().addFirst(propertySource);
  }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // request handler의 특정 파라미터 값을 처리하기 위해 개발자가 만든 객체를 등록한다.
        resolvers.add(new LoginUserArgumentResolver());
    }

    public static void main(String[] args) {
    System.out.println("App 실행!");
    SpringApplication.run(App.class, args);
  }
}
