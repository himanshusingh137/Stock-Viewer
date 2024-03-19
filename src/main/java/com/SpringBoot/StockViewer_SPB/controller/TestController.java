//package com.SpringBoot.StockViewer_SPB.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.SpringBoot.StockViewer_SPB.service.StockService;
//
//
//@Controller
//public class TestController {
//
//	@Autowired
//	StockService stockService;
//
//	@PostMapping("/upload-csvfile")
//	public String uploadCSV(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//
//		 if (file.isEmpty()) {
//			 redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
//		        return "redirect:/result"; // Redirect back to the same page
//		    }
//
//		    boolean success = stockService.uploadCSV(file);
//
//		    if (success) {
//		    	redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
//		    } else {
//		    	redirectAttributes.addFlashAttribute("message", "Failed to process the file.");
//		    }
//
//		     return "redirect:/result"; // Return the same view
//	}
//
//	@GetMapping("/result")
//	public String showResult(Model model) {
//		// Add any necessary model attributes here before showing the result page
//		return "result"; // Return the result page
//	}
//
//	@RequestMapping("/home")
//	public String home(Model model) {
//		return "Home";
//	}
//
//	@RequestMapping("/oye")
//	public String oye() {
//		return "Details";
//	}
//
//}
