package hellospring.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HelloController {
	
	@RequestMapping("/hello")
	public String hello() {
		return "/WEB-INF/views/hello.jsp";
	}

	@RequestMapping("/hello2")
	public String hello(@RequestParam("name") String name) {
		System.out.println("name : " + name);
		return "/WEB-INF/views/hello.jsp";
	}
	
	@RequestMapping("/hello3")
	public ModelAndView hello3(@RequestParam("name") String name) {
		// 파라미터를 jsp로 넘기기
		ModelAndView mav = new ModelAndView(); //내가 직접 만들어서 view랑 model 각각 넣어주기 
		mav.setViewName("/WEB-INF/views/hello3.jsp"); //viewResolver가 viewName 가지고 resolving 해줌
		mav.addObject("name", name);
		
		return mav;
	}
	
	@RequestMapping("/hello4")
	public String hello4(@RequestParam("name") String name, Model model) { //DispatcherServlet이 만들어준 Model 사용
		model.addAttribute("name", name);
		
		return "/WEB-INF/views/hello3.jsp";
	}
	
	@ResponseBody //내가 리턴하는 거 ResponseBody에 붙여서 보내
	@RequestMapping("/hello5")
	public String hello5() { //DispatcherServlet이 만들어준 Model 사용
		return "<h1>Hello Spring</h1>";
	}
	
	@RequestMapping("/hello6")
	public String hello6() {
		return "redirect:/hello";
	}
	
	@RequestMapping("/hello7") // Anti-spring... 좋은 건 아님
	public void hello7(HttpServletResponse response) throws IOException {
		response.getWriter().print("<h1>hello Spring</h1>");
	}
}
