package com.uniqueGames.controller;


import com.uniqueGames.model.Company;
import com.uniqueGames.model.Member;
import com.uniqueGames.model.SessionConstants;
import com.uniqueGames.repository.CompanyRepositoryMapper;
import com.uniqueGames.repository.MemberRepositoryMapper;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	MemberRepositoryMapper memberRepositoryMapper;
	CompanyRepositoryMapper companyRepositoryMapper;

	@Autowired
	public LoginController(MemberRepositoryMapper memberRepositoryMapper,
			CompanyRepositoryMapper companyRepositoryMapper) {
		this.memberRepositoryMapper = memberRepositoryMapper;
		this.companyRepositoryMapper = companyRepositoryMapper;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login/login";
	}


	/**
	 * HTML form의 action에 지정된 표현식은 브라우저가 해당 요청을 생성할 때, 실제 URL을 구성하기 위한 것
	 * 예를 들어, @{/login(redirectURL=${param.redirectURL})}는 브라우저에서 해당 form을 제출할 때
	 * Thymeleaf 템플릿 엔진에 의해 실제로 /login으로 매핑되며, 추가적인 URL 매개변수 redirectURL을 가져오게 된다.
	 *
	 * 즉, form action이 @{/login(redirectURL=${param.redirectURL})}으로 설정되어 있더라도,
	 * 실제로는 해당 폼이 /login 엔드포인트로 POST 요청을 보내게되고,
	 * 컨트롤러에서는 /login으로 지정된 경로의 POST 요청을 처리하게 된다.
	 *
	 * 따라서 폼의 action과 컨트롤러 메서드의 경로가 일치하지 않더라도, 실제 요청이 컨트롤러에 의해 처리될 수 있다.
	 * Thymeleaf의 템플릿 문법을 통해 HTML form의 action 속성을 동적으로 설정할 수 있으며,
	 * 이는 실제 HTTP 요청을 보내는 데 사용되는 경로를 정의한다.
	 *
	 * @param member
	 * @param company
	 * @param request
	 * @param redirectURL
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginOk(@Validated @ModelAttribute Member member, @Validated @ModelAttribute Company company,
						  HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURL,
						  Model model) {
		HttpSession session = request.getSession(); // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성하여 반환

		/* member */
		if (member != null && memberRepositoryMapper.passEqual(member) == 1) {
			// loginMember != null && loginMember.getPassword().equals(member.getPassword())
			session.setAttribute(SessionConstants.LOGIN_MEMBER, memberRepositoryMapper.findById(member.getMemberId()));
			session.setAttribute("login", "member");
		}
		/* company */
		else if (company != null && companyRepositoryMapper.passEqual(company) == 1) {
			session.setAttribute(SessionConstants.LOGIN_MEMBER,  companyRepositoryMapper.findById(company.getCompanyId())); // 세션에 로그인 회원 정보 보관
			session.setAttribute("login", "company");
		}else {
			model.addAttribute("result", "login");
			model.addAttribute("url", "/login");
			return "login/login";
		}
		
		return "redirect:" + redirectURL;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();

		return "redirect:/";
	}

	@RequestMapping("/session-info")
	@ResponseBody
	public String sessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session.getAttribute(SessionConstants.LOGIN_MEMBER) == null) {
			System.out.println("세션 X");
			return "세션 X";
		}
		// 세션 id와 저장된 객체 정보 출력
		System.out.println(session.getId() + ", " + session.getAttribute(SessionConstants.LOGIN_MEMBER));

		// 세션 데이터 출력
		Enumeration<String> attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String name = attributeNames.nextElement();
		}

		return "세션 출력";

	}

}
