package bitcamp.myapp.service;

import bitcamp.myapp.dao.BoardDao;
import bitcamp.myapp.dao.BoardFileDao;
import bitcamp.myapp.vo.AttachedFile;
import bitcamp.myapp.vo.Board;

import java.util.List;

public class DefaultBoardService implements BoardService {

    private BoardDao boardDao;
    private BoardFileDao boardFileDao;

    public DefaultBoardService(BoardDao boardDao, BoardFileDao boardFileDao) {
        this.boardDao = boardDao;
        this.boardFileDao = boardFileDao;
    }
    @Override
    public List<Board> list() {
        return boardDao.findAll();
    }
    @Override
    public void add(Board board) {
        boardDao.insert(board);

        for (AttachedFile file : board.getAttachedFiles()) {
            file.setBoardNo(board.getNo());
            boardFileDao.insert(file);
        }
    }
    @Override
    public Board get(int no) {
        return boardDao.findByNo(no);
    }
    @Override
    public void update(Board board) {
        boardDao.update(board);
    }
    @Override
    public void delete(int no) {
        boardDao.delete(no);
    }

    @Override
    public AttachedFile getAttachedFile(int fileNo) {
        return boardFileDao.findByNo(fileNo);
    }
    @Override
    public void deleteAttachedFile(int fileNo) {
        boardFileDao.delete(fileNo);
    }
}
