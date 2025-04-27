package MajorProject.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/menu")
	public String Menu() {
		return"menu";
	}
	
	@GetMapping("/")
	public String Home() {
		return"Home";
	}
	
	@GetMapping("/about.html")
	public String About() {
		return"about";
	}
	
	
	
	
	@GetMapping("/REGISTER")
	public String Register() {
		return"Register";
	}
	
//	@GetMapping("/over")
//	public String Registers() {
//		return"register1";
//	}
	
	//logAuth
	@GetMapping("/logauth")
	public String Log() {
		return"LogAuth";
	}
	
	@GetMapping("/otp1")
	public String OTP() {
		return"otp";
	}
	
	@GetMapping("/landing")
	public String LandingPage() {
		return"Landing";
	}
	
	@GetMapping("/Users1")
	public String User() {
		return"User";
	}
	
	@GetMapping("/NormalUsers")
	public String Users() {
		return"NormalUser";
	}
	
	@GetMapping("/reserve")
	public String Reserve() {
		return"reservationform";
	}
	
	@GetMapping("/canclreservation")
	public String CancelReserve() {
		return"CancelReservation";
	}
	
	@GetMapping("/cancelreservationdata")
	public String AdminCancelReserve() {
		return"reservationdata";
	}
	
	@GetMapping("/admincontactmessages")
	public String AdminContact() {
		return"admincontactmessages";
	}
	
	@GetMapping("/table")
	public String Table() {
		return"table";
	}
	@GetMapping("/q")
	public String Admintables() {
		return"Admintable";
	}
	
	
	
	@GetMapping("/adminmenu")
	public String adminmenu() {
		return"adminmenu";
	}
	
	@GetMapping("/feedback")
	public String usermenu() {
		return"feedback";
	}
	
	@GetMapping("/adminfeedback")
	public String Adminfeedback() {
		return"adminfeedbackmessages";
	}
	@GetMapping("/samplemenu")
	public String samplemenu() {
		return"samplemenu";
	}
	
	@GetMapping("/admintimeslots")
	public String admintimeslots() {
		return"admin_timeslots";
	}
	@GetMapping("/A")
	public String A() {
		return"A";
	}
	

	
	@GetMapping("/C")
	public String C() {
		return"order";
	}
	
	@GetMapping("/adminbank")
	public String adminbankdetails() {
		return"ABank";
	}
	
	@GetMapping("/userbank")
	public String userbankdetails() {
		return"UBank";
	}
	
	
	
	
	

}
