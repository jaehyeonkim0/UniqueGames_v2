package com.uniqueGames.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Member {

	private int id;
	private String memberId;
	private String password;
	private String name;
	private String email;

	private String tel, phoneNum, addr;
	private String addr1, addr2, newpassword;
	private MultipartFile file;
	private String profileImg;
	private String newProfileImg;

	public String getAddr() {
		if(addr1!=null) {
			addr = addr1 +"   "+ addr2;
		}
		return addr;
	}
}