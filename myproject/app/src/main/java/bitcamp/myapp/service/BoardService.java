package bitcamp.myapp.service;

import bitcamp.myapp.vo.Board;

import java.util.List;

public interface BoardService {


    List<Board> list();

    void add(Board board);

    Board get(int no);

    void update(Board board);

    void delete(int no);
}
