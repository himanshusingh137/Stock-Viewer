package com.SpringBoot.StockViewer_SPB.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.SpringBoot.StockViewer_SPB.service.StockService;

@Controller
public class TestController2 {

	@Autowired
	StockService stockService2;

	@PostMapping("/upload-csv")
	public ResponseEntity<Map<String, Object>> uploadCSV(@RequestParam("file") MultipartFile file) {

		System.out.println("call endpoint");

		Map<String, Object> response = new HashMap();
		try {
			if (file.isEmpty()) {
				System.out.println("file is empty");
				response.put("success", false);
				response.put("message", "Please select a file to upload.");
				return ResponseEntity.badRequest().body(response);
			}
			System.out.println("file is not empty");
			boolean success = stockService2.uploadCSV(file);
			System.out.println("uploadcsv complete");
			if (success) {
				System.out.println("succed");
				response.put("success", true);
				response.put("message", "File uploaded successfully!");
			} else {
				System.out.println("fail to upload");
				response.put("success", false);
				response.put("message", "Failed to process the file.");
			}
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "An error occurred while uploading the file.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@RequestMapping("/home")
	public String home() {
		return "Upload";
	}

	@RequestMapping("fetchAllData")
	public String fetchAllData(Model model) {

		List<Object[]> allOHLCDataWithStock = stockService2.getAllData();

		model.addAttribute("allDataListOfObjectArray", allOHLCDataWithStock);

		return "Details";
	}
	
	@RequestMapping("fetchLatestUnique")
	public String fetchOnlyLatestUniqueStockRecords(Model model) {

		List<Object[]> onlyLatestDetails = stockService2.fetchOnlyLatestUniqueStockRecords();

		model.addAttribute("onlyLatestDetails", onlyLatestDetails);

		System.out.println(onlyLatestDetails);
		return "latestDetailsOnly";
	}

	@RequestMapping("fetchDataBySymbol")
	public ResponseEntity<List<Object[]>> fetchDataBySymbol(@RequestParam String symbol) {

		List<Object[]> allDataOfSpecificSymbol = stockService2.getAllDataOfSpecificSymbol(symbol);

		return ResponseEntity.ok(allDataOfSpecificSymbol);
	}
	
	@RequestMapping("fetchDataBySymbolOnlyLatest")
	public ResponseEntity<List<Object[]>> fetchDataBySymbolOnlyLatest(@RequestParam String symbol) {

		List<Object[]> allLatestDataOfSpecificSymbol = stockService2.getAllDataOfSpecificSymbolOnlyLatest(symbol);


		return ResponseEntity.ok(allLatestDataOfSpecificSymbol);
	}
	
	@RequestMapping("fetchDataByDateRange")
	public ResponseEntity<List<Object[]>> fetchDataByDateRange(@RequestParam("startDate")String startDate,
	                                                            @RequestParam("endDate") String endDate) {
		
	    try {
	    	
	    	System.out.println("start:-" +startDate);
	    	System.out.println("end:-"+ endDate);
	    	
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date parsedStartDate = dateFormat.parse(startDate);
	        Date parsedEndDate = dateFormat.parse(endDate);
	    	
	        List<Object[]> dataInRange = stockService2.getDataByDateRange(parsedStartDate, parsedEndDate);
	        return ResponseEntity.ok(dataInRange);
	        
	    } catch (Exception e) {
	        // Handle any exceptions or errors
	    	System.out.println("not fetched betwn dates");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@RequestMapping("view_chart")
	public String viewChart(@RequestParam String stockSymbol , Model model) {
		
		List<Object[]> allDataOfSpecificSymbol = stockService2.getAllDataOfSpecificSymbol(stockSymbol);  
		
		System.out.println("view chart list of object:  "+allDataOfSpecificSymbol);
		model.addAttribute("allDataOfSpecificSymbol", allDataOfSpecificSymbol);
		return "viewChart";      
	}

}
