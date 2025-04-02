package bitcamp.myapp.common;

import bitcamp.myapp.member.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtAuth {
   static public Member extractUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof Jwt jwt) {
//      Jwt jwt = (Jwt) authentication.getPrincipal();
            Member member = new Member();
            member.setNo(Integer.parseInt(jwt.getClaim("no")));
            member.setEmail(jwt.getSubject());
            member.setName(jwt.getClaim("name"));
            member.setPhoto(jwt.getClaim("photo"));
            //객체 -> json 형식의 문자열
            return member;
        }
        return null;
    }

}
