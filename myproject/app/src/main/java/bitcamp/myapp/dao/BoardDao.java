package bitcamp.myapp.dao;

import bitcamp.myapp.vo.Board;
import java.util.List;

public interface BoardDao {


    //메소드 시그니쳐
    List<Board> findAll() throws DaoException;

    int insert(Board board) throws DaoException;

    Board findByNo(int no) throws DaoException;

    int update(Board board) throws DaoException;

    int delete(int no) throws DaoException;
}
