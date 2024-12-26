package hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @RequestMapping 클래스 "단독" 매핑
 * 
 * Spring MVC 4.x 지원 (5.x 부터는 클래스 단독 매핑 지원 안 함)
 */

@RequestMapping("/guestbook/*") // /*도 함께 써야 하는 것 주의 
@Controller
public class GuestbookController {
	
	@ResponseBody
	@RequestMapping // /guestbook/list로 자동 매핑 됨 
	public String list() {
		return "GuestbookController:list()";
	}
}