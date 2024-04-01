package com.uniqueGames.controller;

import com.uniqueGames.model.Company;
import com.uniqueGames.model.Member;
import com.uniqueGames.service.CompanyMemberService;
import com.uniqueGames.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class JoinController {

	private MemberService memberService;
	private CompanyMemberService companyMemberService;

	@Autowired
	public JoinController(MemberService memberService, CompanyMemberService companyMemberService) {
		this.memberService = memberService;
		this.companyMemberService = companyMemberService;
	}
	@GetMapping("join")
	public String join(Member member) {
		return "join/member-join";
	}

	@GetMapping("joincompany")
	public String companyJoin(Company company) { return "join/company-join"; }

	@PostMapping("join")
	public String joinProc(Member member, Model model) {
		//member-join.html에서 넘어온 input name = file인 file을 받아
		//파일이 있는지 확인 후, 있으면 업로드용 이미지 이름 추출
		String fileName = memberService.fileCheck(member.getFile());
		//memberDto에 String profileImg set
		member.setProfileImg(fileName);

		int result = memberService.save(member);
		if(result == 1) {
			memberService.fileSave(); //directory에 파일 저장
			//login.html로 가서 script 코드 실행
//			model.addAttribute("result", "join");
		}else {
			return "redirect:/";
		}
		return "redirect:/login";
	}
	@PostMapping("joincompany")
	public String joinCompanyProc(Company company, Model model) {
		String fileName = companyMemberService.fileCheck(company.getFile());
		company.setProfileImg(fileName);
		int result = companyMemberService.save(company);
		if(result == 1) {
			companyMemberService.fileSave();
//			model.addAttribute("result", "join");
		}else {
			return "redirect:/";
		}
		return "redirect:/login";
	}

}
