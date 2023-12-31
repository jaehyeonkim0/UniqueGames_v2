package com.uniqueGames.service;


import com.uniqueGames.fileutil.BoardUtil;
import com.uniqueGames.fileutil.FileUploadUtil;
import com.uniqueGames.model.Company;
import com.uniqueGames.model.Notice;
import com.uniqueGames.repository.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoticeService extends FileUploadUtil {

    private final NoticeMapper noticeMapper;
    private final BoardUtil boardUtil;

    @Autowired
    public NoticeService(NoticeMapper noticeMapper, BoardUtil boardUtil) {
        this.noticeMapper = noticeMapper;
        this.boardUtil = boardUtil;
    }

    @Override
    protected void extractFile(Object obj) {
        super.setFile(((Notice) obj).getFile());
    }

    /**
     * ㅋ
     * 공지사항 - 전체 리스트 조회
     */
    public List<Notice> getNoticeList(int startCount, int endCount) {
        return boardUtil.getOutput(noticeMapper.selectNotice(startCount, endCount));
    }

    /**
     * 공지사항 - 상세 보기
     */
    public Notice getNoticeContent(String stat, String no) {
        Notice notice = noticeMapper.selectContent(no);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (notice != null) {
            if (stat == null || stat.equals("")) {
                noticeMapper.hitsCount(no);
                notice.setNoticeHits(notice.getNoticeHits() + 1);
            }

            notice.setDateOutput(format.format(notice.getNoticeDate()));

        }

        return notice;
    }

    /**
     * 공지사항 작성
     *
     * @param notice 폼 데이터
     */
    public int insert(Notice notice) {
        int insResult = noticeMapper.insertNotice(notice);

        if (notice.getUploadImg() != null) {
            String[] fileList = notice.getUploadImg().split(",");
            repeatInsert(fileList, notice);
        }

        return insResult;
    }

    /**
     * 공지사항 수정
     *
     * @param notice 폼 데이터
     */
    public int update(Notice notice) {
        // 이미 저장되어있는 이미지 정보 배열
        String[] dbImg = noticeMapper.getDbImage(notice.getId());

        int result = noticeMapper.updateNotice(notice);
        if (result == 1) {
            List<String> deleteList = null;

            // 이미지가 있다면
            if (notice.getUploadImg() != null) {
                // db와 이름 비교후 삭제 리스트에 저장
                deleteList = new ArrayList<>();

                for (String img : dbImg) {
                    if (!notice.getUploadImg().contains(img)) {
                        // db에 있는 이미지와 본문 이미지가 같은게 없으면
                        deleteList.add(img);
                    } else {
                        // db에 있는 이미지와 본문 이미지가 같은게 있으면
                        notice.setUploadImg(notice.getUploadImg().replaceAll(img, ""));
                    }
                }

                // db에 저장
                String[] fileList = notice.getUploadImg().split(",");
                repeatInsert(fileList, notice);

            } else if (dbImg.length > 0) {
                // 이미지가 없고 db에 저장된 이미지가 있으면 이미지 삭제
                List<String> dbImgList = Arrays.stream(dbImg).collect(Collectors.toList());
                noticeMapper.deleteImage(dbImgList);
                fileListDelete(dbImgList);
            }

            if (deleteList != null && deleteList.size() > 0) {
                //본문에 없는 이미지 db에서 삭제
                noticeMapper.deleteImage(deleteList);
                fileListDelete(deleteList);

            }

        }

        return result;
    }

    /**
     * 공지사항 삭제
     *
     * @param no     삭제할 페이지
     * @param imgDel 삭제할 이미지 이름 (ex:"1123.png,2143.png ...")
     */
    public int delete(String no, String imgDel) {

        if (imgDel != null) {
            String[] fileList = imgDel.split(",");
            fileListDelete(Arrays.stream(fileList).collect(Collectors.toList()));
        }


        return noticeMapper.deleteNotice(no);
    }

    /**
     * 공지사항 리스트 삭제
     * @param list    삭제할 페이지 번호가 담긴 배열
     * @param company 로그인 한 회사 정보
     * @return FAIL OR SUCCESS
     */
    public String deleteList(String[] list, Company company) {
        fileListDelete(noticeMapper.deleteListBefore(list));
        String result = "FAIL";

        int dbResult = noticeMapper.deleteList(list, company);
        if (dbResult == 1) {
            result = "SUCCESS";
        }
        return result;
    }

    /**
     * 공지사항 - 검색
     */
    public List<Notice> search(String keyword, Map<String, Integer> pageMap, String searchType) {

        String[] keywordList = keyword.split(" ");
        return boardUtil.getOutput(noticeMapper.searchList(keywordList, pageMap, searchType));
    }

    /**
     * db에 insert 반복
     *
     * @param fileList 파일 이름 리스트
     * @param notice 현재 글 정보
     */
    private void repeatInsert(String[] fileList, Notice notice) {
        for (String img : fileList) {
            if (!img.equals("")) {
                notice.setUploadImg(img);
                noticeMapper.insertImage(notice);
            }
        }
    }

}
