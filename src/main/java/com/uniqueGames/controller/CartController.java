package com.uniqueGames.controller;


import com.uniqueGames.model.CompanyVo;
import com.uniqueGames.model.GameVo;
import com.uniqueGames.model.MemberVo;
import com.uniqueGames.model.Order;
import com.uniqueGames.model.SessionConstants;
import com.uniqueGames.service.OrderServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({SessionConstants.LOGIN_MEMBER,"game","companyVo"})
public class CartController {
	@Autowired
    OrderServiceImpl orderService;

	@RequestMapping(value = "/cart", method = RequestMethod.POST)
	public String getValue(@RequestParam("selectedValue") String selectedValue, @ModelAttribute("companyVo")
	CompanyVo companyVo, @ModelAttribute(SessionConstants.LOGIN_MEMBER) MemberVo memberVo, @ModelAttribute("game") GameVo gameVo){

		/*
		* orderService의 데이터 insert 기능 추가
		* */
		Order order = orderService.addToOrderVo(
				memberVo.getMember_id(),
				companyVo.getCompany_id(),
				gameVo.getId(),
				Integer.parseInt(selectedValue),
				gameVo.getName(),
				gameVo.getImage_path()
				);
		orderService.insertCart(order);
		return "order/cart";
	}
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public ModelAndView cart(@ModelAttribute(SessionConstants.LOGIN_MEMBER) MemberVo member) {
		ModelAndView model = new ModelAndView();
		ArrayList<Order> cartList = orderService.getCartList(member.getMember_id());
		System.out.println(member.getMember_id());


		if (cartList.size() > 0) {
			model.addObject("cartList", cartList);
			model.addObject("nothingInCart", false);
		} else {
			model.addObject("nothingInCart", true);
		}

		model.setViewName("order/cart");

		return model;
	}

	@RequestMapping(value = "/cart_delete_one", method = RequestMethod.GET)
	public String cart_delete_one(int id) {
		String view;
		int result = orderService.getCartDeleteOne(id);

		if (result != 0) {
			view = "redirect://cart";
		} else {
			view = "order/error";
		}

		return view;
	}

	@RequestMapping(value = "/cart_delete_selected")
	public String cart_delete_selected(@RequestParam(value = "checkedList[]") List<Integer> checkedList) {
		int result = 0;

		for (int i = 0; i < checkedList.size(); i++) {
			result = orderService.getCartDeleteOne((int) checkedList.get(i));
			if (result == 0) {
				return "order/error";
			}
		}
		return "redirect://cart";
	}

	@RequestMapping(value = "/cart_delete_all", method = RequestMethod.GET)
	public String cart_delete_selected(@ModelAttribute(SessionConstants.LOGIN_MEMBER) MemberVo member) {
		String m_id = member.getMember_id();
		int result = orderService.getCartDeleteAll(m_id);

		if (result == 0) {
			return "order/error";
		}

		return "redirect://cart";
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error() {
		return "order/error";
	}

}
