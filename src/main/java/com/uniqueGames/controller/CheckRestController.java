package com.uniqueGames.controller;

import com.uniqueGames.config.Login;
import com.uniqueGames.model.Company;
import com.uniqueGames.model.Member;
import com.uniqueGames.service.CompanyMemberService;
import com.uniqueGames.service.MailSendService;
import com.uniqueGames.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class CheckRestController {

    private MemberService memberService;
    private CompanyMemberService companyMemberService;
    private MailSendService mailSendService;

    @Autowired
    public CheckRestController(MemberService memberService,
                               CompanyMemberService companyMemberService,
                               MailSendService mailSendService) {
        this.memberService = memberService;
        this.companyMemberService = companyMemberService;
        this.mailSendService = mailSendService;
    }
    
    /**
     * 회원가입 : 메일로 인증번호 전송
     * @param email 입력한 이메일
     */
    @PostMapping("mailCheck")
    public String mailSend(@RequestParam("email") String email) {
        return mailSendService.joinEmail(email);
    }

    /**
     * 아이디 찾기 : 메일로 아이디 전송
     * @param email 입력한 이메일
     * @param type2 개인 / 법인 구분 String
     *
     */
    @PostMapping("sendIdToEmail")
    public ResponseEntity<Object> sendIdToEmail(@RequestParam("email") String email,
                                                @RequestParam("type2") String type2) {
        int findingResult = memberService.findUserByEmail(email, type2);
        if(findingResult == 1) {
            String id = memberService.findId(email, type2);
            mailSendService.sendIdToEmail(email, id);
            return ResponseEntity.ok(id);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 비밀번호 찾기 : 메일로 임시 비밀번호 전송
     * @param email 입력한 이메일
     * @param id    입력한 아이디
     * @param type2 개인 / 법인 구분 String
     */
    @PostMapping("sendPassToEmail")
    public ResponseEntity<Object> sendPassToEmail(@RequestParam("email") String email,
                                  @RequestParam("id") String id,
                                  @RequestParam("type2") String type2) {
        int findingResult = memberService.findUserByIdEmail(email, id, type2);
        if(findingResult == 1) {
            mailSendService.sendPassToEmail(email, id, type2);
            return ResponseEntity.ok(id);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 회원가입 :     아이디 중복체크
     * @param id    입력한 아이디
     * @param type2 개인 / 법인 구분 String
     */
    @PostMapping("idcheck1")
    public String idCheck1(@RequestParam("id") String id,
                           @RequestParam("type2") String type2) {
        int result = memberService.idCheck1(id, type2);
        return String.valueOf(result);
    }
    /**
     * 회원가입 :           휴대전화 중복체크
     * @param phoneNum    입력한 휴대전화
     * @param type2       개인 / 법인 구분 String
     */
    @PostMapping("phonecheck1")
    public String phoneCheck1(@RequestParam("phone_num") String phoneNum,
                              @RequestParam("type2") String type2) {
        int result = memberService.phoneCheck1(phoneNum, type2);
        return String.valueOf(result);
    }
    
    /**
     * 회원가입 :        이메일 중복체크
     * @param email    입력한 이메일
     * @param type2    개인 / 법인 구분 String
     */
    @PostMapping("emailduplicatecheck")
    public String emailDuplicateCheck(@RequestParam("email") String email,
                                      @RequestParam("type2") String type2) {
        int result = memberService.emailDuplicateCheck(email, type2);
        return String.valueOf(result);
    }
    
    /**
     * 마이페이지 :      회원탈퇴
     * @param id       탈퇴할 아이디
     * @param password 탈퇴할 아이디의 비밀번호
     * @param type2    개인 / 법인 구분 String
     */
    @PostMapping(value="/deletecheck1")
    public String deleteCheck1(HttpSession session,
                                @RequestParam("id") String id,
                                @RequestParam("password") String password,
                                @RequestParam("type2") String type2) {

        int result = memberService.delete1(id, password, type2);
        if(result==1) {
            session.invalidate();
        }
        return String.valueOf(result);
    }
}
