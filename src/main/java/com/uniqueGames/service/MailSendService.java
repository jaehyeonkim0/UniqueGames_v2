package com.uniqueGames.service;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.uniqueGames.model.Comment;
import com.uniqueGames.model.Member;
import com.uniqueGames.repository.MemberMapper;
import com.uniqueGames.repository.MemberRepositoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSendService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MemberMapper memberMapper;

	private int authNumber;

	public void makeRandomNumber() {
		Random r = new Random();
		int CheckNum = r.nextInt(888888) + 111111;
		System.out.println("checkNum="+ CheckNum);
		authNumber = CheckNum;
	}

	public String joinEmail(String email) {
		makeRandomNumber();
		String setFrom = "a01083529494@gmail.com";
        String toMail = email;
        String title = "회원가입 인증 이메일 입니다.";
        String content =
                "홈페이지를 방문해주셔서 감사합니다." +
                "<br><br>" +
                "인증 번호는 " + authNumber + "입니다." +
                "<br>" +
                "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
        mailSend(setFrom, toMail, title, content);
		System.out.println("인증번호="+authNumber);
        return Integer.toString(authNumber);
	}

	public String reportEmail(Comment comment) {
		String setFrom = "uniquegames@uniquegames.com";
		String toMail = "durrl5897@gmail.com";
		String title = "신고 내역";
		String content =
				"댓글 번호 : " + comment.getId() +
						"<br><br>" +
						"댓글 내용 : " + comment.getCommentContent() +
						"<br>" +
						"신고 사유 : " + comment.getReason() +
						"<br>" +
						"신고자 : " + comment.getMId();
		mailSend(setFrom, toMail, title, content);
		return "Email Send Success";
	}

	public String sendIdToEmail(String email, String id) {
		String setFrom = "a01083529494@gmail.com";
		String toMail = email;
		String title = "유니크게임즈 아이디 관련 이메일입니다";
		String content =
				"회원님의 아이디를 알려드립니다." +
						"<br><br>" +
						"회원님의 아이디는 <font color='blue'>" + id + "</font>입니다." +
						"<br>";
		mailSend(setFrom, toMail, title, content);
		return "Send ID Success";
	}

	public String sendPassToEmail(String email, String id, String type2) {
		makeRandomNumber();
		String setFrom = "a01083529494@gmail.com";
		String toMail = email;
		String title = "유니크게임즈 임시 비밀번호입니다";
		String content =
				"회원님의 아이디를 알려드립니다." +
						"<br><br>" +
						"회원님의 임시 비밀번호 : <font color='blue'>" + authNumber + "</font>입니다." +
						"<br>" +
						"로그인 후 비밀번호를 변경해주시기 바랍니다" +
						"<br>";
		mailSend(setFrom, toMail, title, content);
		Map<String, Object> param = new HashMap();
		param.put("email", email);
		param.put("id", id);
		param.put("type2", type2);
		param.put("newpassword", Integer.toString(authNumber));
		memberMapper.changePassword(param);
		return "Send Pass Success";
	}



	public void mailSend(String setFrom, String toMail, String title, String content) {

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content,true);
			// 포함된 텍스트가 HTML이라는 의미로 true 플래그를 사용한다
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}


}
