package bitcamp.myapp.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultMemberService implements MemberService {

  private MemberDao memberDao;

  public DefaultMemberService(MemberDao memberDao) {
    this.memberDao = memberDao;
  }
  @Override
  public Member get(String email) {return memberDao.findByEmail(email);}
  @Override
  @Transactional
  public int changePassword(String email, String password) {return memberDao.updatePassword(email, password);}
  @Override
  @Transactional
  public int changeAllPassword(String password) {return memberDao.updateAllPassword(password);}

}
