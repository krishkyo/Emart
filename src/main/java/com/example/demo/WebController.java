package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
	@Autowired
	ItemRepo Repo;
	@Autowired
	LoginRepo LRepo;
	@Autowired
	UserRepo URepo;
	@Autowired
	TempRepo TRepo;
	@Autowired
	RandomRepo RRepo;
	@Autowired
	AddressRepo ARepo;
	@GetMapping("/")
	public String getIndex(Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		model.addAttribute("status", a.getStatus());
		return "index";
	}
	
	@GetMapping("/product")
	public String getProduct(Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		model.addAttribute("status", a.getStatus());
		model.addAttribute("item", Repo.findAll());
		return "productList";
	}
	@GetMapping("/addProduct")
	public String addProduct(Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		model.addAttribute("status", a.getStatus());
		model.addAttribute("item", Repo.findAll());
		return "productAdd";
	
	}
	@GetMapping("/addItem")
	public String addItem() {
		return "addItem";
	}
	@PostMapping("/addItem")
	public String added(ItemData item,Model model) {
		Repo.save(item);
		model.addAttribute("item", Repo.findAll());
		return "productAdd";
	}
	@GetMapping("/deleteItem/{id}")
	public String delete(@PathVariable String id, Model model) {
		Repo.deleteById(id);
		model.addAttribute("message", "Product Deleted Successfully");
		return "result";
	}
	@GetMapping("/editItem/{id}")
	public String edit(@PathVariable String id,Model  model) {
		model.addAttribute("num", id);
		return "edit";
	}
	@PostMapping("/editaItem")
	public String editt(ItemData item,Model model) {
		Repo.save(item);
		model.addAttribute("message", "Product Updated Successfully");
		return "result";
	}
	@GetMapping("/product/{id}")
//	@ResponseBody
	public String moreInfo(@PathVariable String id,Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		model.addAttribute("status", a.getStatus());
		model.addAttribute("item", Repo.findByName(id));
		return "productDetail";
	}
	@GetMapping("/buy/{id}")
	public String buy(@PathVariable String id,Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		if(a.getStatus().equals("yes")) {
			model.addAttribute("id", id);
			model.addAttribute("status", a.getStatus());
			return "buy";			
		}else {
			return "login";
		}
		
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String reg() {
		return "register";
	}
	@PostMapping("/addUser")
	public String addUser(AdduserTemp user,Model model) {
		Optional<UserEInfo> list=URepo.findById(user.getUsername());
		if(list.isEmpty()) {
			if(user.getCpassword().equals(user.getNpassword())) {
				UserEInfo u=new UserEInfo();
				u.setUsername(user.getUsername());
				u.setPassword(user.getNpassword());
				Optional<LoginStatus> L=LRepo.findById("1");
				var a=L.get();
				a.setStatus("yes");
				LRepo.save(a);
				URepo.save(u);
				return "login";
			}else {
				model.addAttribute("message2", "Password does not match");
				return "register";
			}
		}else {
			model.addAttribute("message1", "Username already exits");
			return "register";
		}
	}
	@PostMapping("/login")
	public String check(UserEInfo user,Model model) {
		Optional<UserEInfo> list=URepo.findById(user.getUsername());
		if(user.getUsername().equals("admin") && user.getPassword().equals("admin")) {
			Optional<LoginStatus> L=LRepo.findById("1");
			var a=L.get();
			a.setStatus("yes");
			LRepo.save(a);
			model.addAttribute("status", a.getStatus());
			model.addAttribute("item", Repo.findAll());
			return "productAdd";
		}else {
			if(list.isEmpty()) {
				model.addAttribute("message", "Username does not exist");
				return "login";
			}else {
				var a=list.get();
					if(user.getUsername().equals(a.getUsername()) && user.getPassword().equals(a.getPassword())) {
						Optional<LoginStatus> L=LRepo.findById("1");
						var b=L.get();
						b.setStatus("yes");
						LRepo.save(b);
						TempUser n=new TempUser();
						n.setNum("1");
						n.setUsername(user.getUsername());
						TRepo.save(n);
						model.addAttribute("status",b.getStatus());
						model.addAttribute("item", Repo.findAll());
						return "productList";
					}else {
						model.addAttribute("message", "password is incorrect");
						return "login";
					}
				
				
				
			}
		}
		
	}
	@GetMapping("/logout")
	public String logout(Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var b=L.get();
		b.setStatus("no");
		LRepo.save(b);
		TempUser n=new TempUser();
		n.setNum("1");
		n.setUsername("");
		TRepo.save(n);
		model.addAttribute("status", b.getStatus());
		return "index";
	}
	@PostMapping("/addOrder")
	public String addOrder(AddressTemp address,Model model) {
		Optional<RandomEData> e=RRepo.findById("1");
		var a=e.get();
		AddressDetail order=new AddressDetail();
		Optional<TempUser> u=TRepo.findById("1");
		var b=u.get();
		order.setNum(a.getNum());
		order.setName(address.getName());
		order.setEmail(address.getEmail());
		order.setPhno(address.getPhno());
		order.setMethod(address.getMethod());
		order.setUsername(b.getUsername());
		order.setAddress(address.getAddress());
		ItemData i=Repo.findByName(address.getModel());
		order.setModel(i.getName());
		order.setPrice(i.getCost());
		order.setStatus("Not Delivered");
		ARepo.save(order);
		a.setNum((Integer.parseInt(a.getNum())+1)+"");
		RRepo.save(a);
		model.addAttribute("message", "Thanks for purchasing!");
		return "userResult";
	}
	@GetMapping("/orders")
	public String order(Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		if(a.getStatus().equals("yes")) {
			Optional<TempUser> u=TRepo.findById("1");
			var b=u.get();
			List<AddressDetail> list=ARepo.findAllByUsername(b.getUsername());
			model.addAttribute("status", a.getStatus());
			model.addAttribute("item",list);
			return "order";			
		}else {
			return "login";
		}
		
	}
	
	@GetMapping("/showOrder")
//	@ResponseBody
	public String showOrder(Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		Iterable<AddressDetail> list=ARepo.findByStatus("Not Delivered");
		Iterable<AddressDetail> list2=ARepo.findByStatus("Delivered");
		model.addAttribute("status", a.getStatus());
		model.addAttribute("item", list);
		model.addAttribute("item1", list2);
		return "adminorder";
	}
	@GetMapping("/showinfo/{id}")
	public String show(@PathVariable String id,Model model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		Optional<AddressDetail> add=ARepo.findById(id);
		var b=add.get();
		model.addAttribute("status", a.getStatus());
		model.addAttribute("item", b);
		return "orderDetail";
	}
	@GetMapping("/deliver/{id}")
	public String deliver(@PathVariable String id,Model  model) {
		Optional<LoginStatus> L=LRepo.findById("1");
		var a=L.get();
		model.addAttribute("status", a.getStatus());
		Optional<AddressDetail> add=ARepo.findById(id);
		var b=add.get();
		b.setStatus("Delivered");
		ARepo.save(b);
		Iterable<AddressDetail> list=ARepo.findByStatus("Not Delivered");
		Iterable<AddressDetail> list2=ARepo.findByStatus("Delivered");
		model.addAttribute("item", list);
		model.addAttribute("item1", list2);
		return "adminorder";
	}
}
