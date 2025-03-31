package bitcamp.myapp.config;


import bitcamp.myapp.member.Member;
import bitcamp.myapp.member.MemberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// 학습 목표:
// - 로그인 페이지 커스텀 마이징
//  - formLogin() 변경
//     -loginPage() 추가
//     -loginProcessUrl() 추가
//     -successForwardUrl() 변경
//     -usernameParameter() 변경 "username"-> "email"
//     -passwordParameter() 변경 "password" -> "password" 나중에 파라미터 명을 변경할 경우를 대비해서
@Configuration
public class SecurityConfig {

    private static final Log log = LogFactory.getLog(SecurityConfig.class);


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("SecurityFilterChain 준비!");
        return http
                // 1) 요청 URL의 접근권한 설정
                .authorizeHttpRequests()
                // 정규 표현식으로 패턴을 설절
                // 예) .*\.html
                .regexMatchers(".*\\.html").permitAll() // 주어진 정규표현식과 일치하는 요청에 대해서 인증을 검사하지 않는다.
                    .mvcMatchers("/css/**","/js/**","/auth/**").permitAll() // 지정된 URL 요청은 인증을 검사하지 않는다.
                    .anyRequest().authenticated() // 나머지 요청 URL은 인증을 검사한다.
                    .and() // 접근제어권한설정 등록기를 만든 HttpSecurity 객체를 리턴한다.
                // 2) 인가되지 않은 요청인 경우 Spring Security 기본 로그인 화면으로 보내기
                .formLogin()
                    .loginPage("/auth/login-form")    // 커스터마이징 로그인 폼 등록
                    .loginProcessingUrl("/auth/login")// 로그인 폼의 action 값 설정. 이 요청은 Spring Security에서 처리한다.
                    .successForwardUrl("/auth/success") // 로그인 성공 후 페이지 컨트롤러로 포워딩
                    .usernameParameter("email")         // 클라이언트가 보내는 이메일의 파라미터 명을 "username" 에서 email로 바꾼다.
                    .passwordParameter("password")      // 암호를 보내는 파라미터 이름을 설정한다.
                    .permitAll()
                    .and()

                // 3) 로그아웃 설정 - formLogin()을 커스터마이징 한다면, /logout 경로가 비활성화 된다.
                // - 따라서 다음과 같이 명시적으로 설정되야 한다.
                .logout()
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET")) //
                    .logoutUrl("/logout")           // 로그앙웃 URL 설정. 기본은 POST 요청에만 동작한다.
                    .logoutSuccessUrl("/home")       // 로그아웃 성공 후 이동 페이지
                    .invalidateHttpSession(true)    // 세션 무효화 설정
                    .deleteCookies("JSESSIONID")    // 톰캣 서버에서 세션 ID를 전달할 때 사용하는 쿠키
                    .permitAll()
                    .and()
                // 4) CSRF(Cross-Site Request Forgery)
                // - 로그아웃 할 때 세션을 무효화 시키면 세션에 보관된 CSRF 토큰도 함께 삭제된다.
                // - 클라이언트가 요청했을 때 그 클라이언트에세 발급된 유효한 CSRF 토큰이 없으면
                //   CSRF 공격으로 보고 요청을 거절한다.
                // - 이 기능을 당분간 무효화시킨다.
//                .csrf().disable()
                // SecurityFilterChain 준비
                .build();
    }

    // 사용자 인증을 수행할 객체를 준비
    @Bean
    public UserDetailsService userDetailsService(MemberService memberService, PasswordEncoder passwordEncoder) {
        log.debug("DBUserDetailsService 준비!");

        // DB에 저장된 모든 사용자 암호를 BcryptPasswordEncoder를 사용해서 암호화한다.
        // 테스트를 위해 딱 한번만 수행한다.
        //memberService.changeAllPassword(passwordEncoder.encode("1111"));

        return new UserDetailsService() {
            /*
            로컬 클래스에서 밖의 매개변수 받는 생성자 자동 생성 됨 내부적으로 필드가 자동생성됨
            private  final MemberService memberService;
            public DBUserDetailsService(MemberService memberService){
                this.memberService=memberService;
            }
            */
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Member member = memberService.get(username);
                if( member == null){
                    member = new Member();
                    member.setEmail(username);
                    member.setPassword("");
                }
                return new CustomUserDetails(member);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("PasswordEncoder 준비!");
        // spring에서 제공하는 PasswordEncoder 사용하기
        return new BCryptPasswordEncoder();
    }
    // static Nasted 클래스라 부름
    // non-static Nasted 클래스 => innerclass
    // 메소드 안에 class => localclass
}
