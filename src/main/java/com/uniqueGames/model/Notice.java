package com.uniqueGames.model;

import com.uniqueGames.test.GenericSuper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Setter
@Getter
public class Notice extends GenericSuper {
	int rno;
	int postId;
	String companyId;
	String name;
	String title;
	String content;
	int noticeHits;
	Date noticeDate;
	String dateOutput;
	MultipartFile file;
	String imageId;
	String uploadFile;
	int cmtCount;

}
