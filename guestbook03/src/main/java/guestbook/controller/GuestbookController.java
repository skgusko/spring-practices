package guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guestbook.service.GuestbookService;
import guestbook.vo.GuestbookVo;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GuestbookController {
	private GuestbookService guestbookService;
	
	private GuestbookController(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}
	
	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) {
		/*
		ServletContext sc = request.getServletContext();
		Enumeration<String> e = sc.getAttributeNames();
		
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			System.out.println(name);
		}
		
		ApplicationContext ac1 = (ApplicationContext)sc.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
		ApplicationContext ac2 = (ApplicationContext)sc.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");
		GuestbookRepository repository = ac1.getBean(GuestbookRepository.class);
		System.out.println("====== repository : " + repository);
		
		GuestbookController controller = ac2.getBean(GuestbookController.class);
		System.out.println("====== controller : " + controller);
		
		System.out.println(ac1 == ac2);
		
		*/
		
		model.addAttribute("list", guestbookService.getContentsList());
		
		return "index";
	}
	
	@RequestMapping("/add")
	public String add(GuestbookVo guestbookVo) {
		guestbookService.addContents(guestbookVo);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {
		return "delete";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
	public String delete(@PathVariable("id") Long id, 
						@RequestParam("password") String password) {
		guestbookService.deleteContents(id, password);
		
		return "redirect:/";
	}
}
