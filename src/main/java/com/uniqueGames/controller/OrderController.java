package com.uniqueGames.controller;

import com.uniqueGames.config.Login;
import com.uniqueGames.model.Game;
import com.uniqueGames.model.Member;
import com.uniqueGames.model.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.uniqueGames.service.GameService;
import com.uniqueGames.service.MemberService;
import com.uniqueGames.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Log4j2
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    MemberService memberService;

    @Autowired
    GameService gameService;

    ArrayList<Integer> idList;
    String idStr;

    @RequestMapping(value = "/order")
    public String order(@Login Member member, Model model, @Param("checkedList") String[] checkedList) {
        idList = new ArrayList<Integer>();
        for (String id : checkedList) {
            idList.add(Integer.parseInt(id));
        }

//        idList = Arrays.stream(checkedList).map(str ->
//                        Integer.parseInt(str)).collect(Collectors.toCollection(ArrayList::new));
//        idStr = String.join(", ", checkedList);

        idStr = orderService.listToString(idList);
        ArrayList<Order> orderList = orderService.getOrderList(idStr);
        orderList = orderService.addGameInfo(orderList);

        int totalAmount = orderService.getOrderAmount(idStr);
        Member buyer = memberService.aGetDetailMember(member.getMemberId());

        model.addAttribute("orderList", orderList);
        model.addAttribute("count", "총 " + orderList.size() + "개");
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalAmountStr", orderService.formatCurrency(totalAmount));
        model.addAttribute("member", buyer);

        return "order/order";
    }

    @RequestMapping(value = "/orderDeleteOne")
    public String orderDeleteOne(Model model, int id, @Login Member member) {
        idList.remove(Integer.valueOf(id));
        if (idList.size() == 0) {
            return "order/error";
        }

        ArrayList<Order> orderList = orderService.getOrderList(orderService.listToString(idList));
        orderList = orderService.addGameInfo(orderList);
        int totalAmount = orderService.getOrderAmount(idStr);

        model.addAttribute("orderList", orderList);
        model.addAttribute("count", "총 " + orderList.size() + "개");
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalAmountStr", orderService.formatCurrency(totalAmount));
        model.addAttribute("member", memberService.aGetDetailMember(member.getMemberId()));

        return "order/order";
    }

    @RequestMapping(value = "/order-pay")
    @ResponseBody
    public String order_pay() {
        if (orderService.getOrderComplete(idStr) == 0) {
            return "error";
        }
        return "complete";
    }

    @RequestMapping(value = "/order-complete")
    public String order_complete() {
        return "order/order-complete";
    }
}
