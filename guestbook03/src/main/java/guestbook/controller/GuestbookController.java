package guestbook.controller;

import java.util.Enumeration;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guestbook.repository.GuestbookRepository;
import guestbook.vo.GuestbookVo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GuestbookController {
	private GuestbookRepository guestbookRepository;
	
	private GuestbookController(GuestbookRepository guestbookRepository) {
		this.guestbookRepository = guestbookRepository;
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
		
		model.addAttribute("list", guestbookRepository.findAll());
		
		return "index";
	}
	
//	@RequestMapping("/")
//	public String index(Model model) {
//		List<GuestbookVo> list = guestbookRepository.findAll();
//		model.addAttribute("list", list);
//		
//		return "index";
//	}
	
	@RequestMapping("/add")
	public String add(GuestbookVo guestbookVo) {
		guestbookRepository.insert(guestbookVo);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable("id") Long id) {
//		model.addAttribute("id", id); (@PathVariable로 있는 경우 Model이 자동으로 addAttribute를 해줌) 
		
		return "delete";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
	public String delete(@PathVariable("id") Long id, @RequestParam("password") String password) {
		guestbookRepository.deleteByIdAndPassword(id, password);
		
		return "redirect:/";
	}
}
