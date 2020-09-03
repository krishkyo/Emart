package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	ProductTempRepo PTRepo;
	@Autowired
	ProductListRepo PLRepo;
	@Autowired
	LoginStatusRepo LRepo;
	@Autowired
	EmusicRepo ERepo;
	@Autowired
	UserTempRepo URepo; 
	@Autowired
	CacheRepo CRepo;
	@Autowired
	CartTempRepo CTRepo;
	@Autowired
	CartRepo CARTRepo;
	
	
	private Path path;
	@GetMapping("/")
	public String index(Model model) {
		if(LRepo.findById("1").get().getStatus().equals("yes")) {
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
			model.addAttribute("name", ERepo.findById(CRepo.findById("1").get().getIdnum()).get().getName());	
		}else {
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
		}
		
		return "index";
	}
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/images/";
	@GetMapping("/products")
	public String productList(Model model) {
		if(LRepo.findById("1").get().getStatus().equals("yes")) {
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
			model.addAttribute("name", ERepo.findById(CRepo.findById("1").get().getIdnum()).get().getName());	
		}else {
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
		}
		model.addAttribute("item", PLRepo.findAll());
		
		return "product";
	}
	
	@GetMapping("/admin")
	public String admin(Model model) {
		
		return "admin";
	}
	
	@GetMapping("/adminProducts")
	public String aView(Model model) {
		model.addAttribute("item", PLRepo.findAll());
		
		return "adminProduct";
	}
	@GetMapping("/addProduct")
	public String addProduct(Model model) {
		return "addproduct";
	}
	
	@PostMapping("/addProduct")
	public String adding(@ModelAttribute("product") Productlist product,Model model) {
		Optional<Producttemp> temp=PTRepo.findById("1");
		var a=temp.get();
		int n=Integer.parseInt(a.getNum());
		product.setId(n+"");
		a.setNum(""+(n+1));
		PTRepo.save(a);
		PLRepo.save(product);
		return "addImage";
	}
	
	@PostMapping("/addImage")
	public String addImage(@RequestParam("files") MultipartFile files){
		Optional<Producttemp> temp=PTRepo.findById("1");
		var a=temp.get();
		int n=Integer.parseInt(a.getNum())-1;
		Path fileNameANdPath=Paths.get(uploadDirectory, ""+n+".jpg");
		try {
			Files.write(fileNameANdPath, files.getBytes());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "redirect:/adminProducts";
	}
	@GetMapping("/deleteItem/{id}")
	public String delete(@PathVariable String id, Model model) {
		PLRepo.deleteById(id);
		return "redirect:/adminProducts";
	}
	@GetMapping("/editItem/{id}")
	public String edit(@PathVariable String id, Model model) {
		Optional<Productlist> p=PLRepo.findById(id);
		var a=p.get();
		model.addAttribute("item", a);
		return "edit";
	}
	@PostMapping("/editProduct/{id}")
	public String editing(@PathVariable String id,@ModelAttribute("product") Productlist product) {

		product.setId(id);
		PLRepo.save(product);
		return "redirect:/adminProducts";
	}
	@GetMapping("/info/{id}")
	public String info(@PathVariable String id, Model model) {
		Optional<Productlist> p=PLRepo.findById(id);
		var a=p.get();
		model.addAttribute("item", a);
		if(LRepo.findById("1").get().getStatus().equals("yes")) {
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
			model.addAttribute("name", ERepo.findById(CRepo.findById("1").get().getIdnum()).get().getName());	
		}else {
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
		}
		return "detail";
	}
	
	@GetMapping("/buy/{id}")
	public String buy(@PathVariable String id,Model model,@RequestParam String quantity ) {
		if(LRepo.findById("1").get().getStatus().equals("yes")) {
			model.addAttribute("id", id);
			model.addAttribute("status", LRepo.findById("1").get().getStatus());
			Cart c=new Cart();
			var temp=CTRepo.findById("1").get();
			c.setId(temp.getNum());
			c.setQuantity(quantity);
			c.setName(PLRepo.findById(id).get().getName());
			c.setTotal(""+(Integer.parseInt(quantity)*Integer.parseInt(PLRepo.findById(id).get().getPrice())));
			c.setIdnum(CRepo.findById("1").get().getIdnum());
			CARTRepo.save(c);
			temp.setNum((Integer.parseInt(temp.getNum())+1)+"");
			CTRepo.save(temp);
			return "redirect:/products";			
		}else {
			model.addAttribute("status", LRepo.findById("1").get().getStatus());
			return "redirect:/login";
		}
		
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/register")
	public String reg(Model model) {
		Emusicuser e=new Emusicuser();
		model.addAttribute("user", e);
		return "register";
	}
	@PostMapping("/adduser")
//	@ResponseBody
	public String addUser(@ModelAttribute("userinfo") Emusicuser u,Model model) {
		boolean flag=false;
		for(Emusicuser a:ERepo.findAll()) {
			if(u.getEmail().equals(a.getEmail())) {
				flag=true;
				break;
			}
		}
		if(flag) {
			model.addAttribute("message", "Email address is already taken");
			model.addAttribute("user", u);
			return "register";
		}else {
			u.setId(URepo.findById("1").get().getNum());
			var a=URepo.findById("1").get();
			int n=Integer.parseInt(a.getNum())+1;
			a.setNum(n+"");
			URepo.save(a);
			ERepo.save(u);
			return "result";
		}
//		return u;
	}
	@PostMapping("/login")
	public String logining(@ModelAttribute("user") UserCheck user,Model model) {
		boolean flag = false;
		if(user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("admin")) {
			var temp=CRepo.findById("1").get();
			temp.setIdnum("Admin");
			CRepo.save(temp);
			var b=LRepo.findById("1").get();
			b.setStatus("yes");
			LRepo.save(b);
			return "admin";
		}else {
			for(Emusicuser a:ERepo.findAll()) {
				if(a.getEmail().equals(user.getEmail()) && a.getPassword().equals(user.getPassword())) {
					var temp=CRepo.findById("1").get();
					temp.setIdnum(a.getId());
					CRepo.save(temp);
					flag=true;
					break;
				}
			}
			if(flag){
				var b=LRepo.findById("1").get();
				b.setStatus("yes");
				LRepo.save(b);
				
				return "redirect:/products";
			}else {
				model.addAttribute("message", "email or pasword is incorrect");
				return "login";
			}
		}
		
	}
	@GetMapping("/logout")
	public String log() {
		var b=LRepo.findById("1").get();
		b.setStatus("no");
		LRepo.save(b);
		var temp=CRepo.findById("1").get();
		temp.setIdnum("");
		CRepo.save(temp);
		return "redirect:/";
	}
	
	@GetMapping("/orders")
	public String order(Model model) {
		if(LRepo.findById("1").get().getStatus().equals("yes")) {
			model.addAttribute("item", CARTRepo.findByIdnum(CRepo.findById("1").get().getIdnum()));
			model.addAttribute("status",LRepo.findById("1").get().getStatus());
			model.addAttribute("name", ERepo.findById(CRepo.findById("1").get().getIdnum()).get().getName());
			return "cart";
		}else {
			return "redirect:/login";
		}
	}
	@GetMapping("/resultcart")
	public String resultcar() {
		for(Cart c:CARTRepo.findByIdnum(CRepo.findById("1").get().getIdnum())) {
			CARTRepo.deleteById(c.getId());
		}
		System.out.println(CRepo.findById("1").get().getIdnum());
		return "resultcart";
	}
}
