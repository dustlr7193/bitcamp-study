package bitcamp.myapp.dao;

import bitcamp.myapp.vo.Member;

public interface MemberDao {
    Member findByEmailAndPassword(String email, String password) throws DaoException;
}
