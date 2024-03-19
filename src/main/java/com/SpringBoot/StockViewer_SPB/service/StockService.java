package com.SpringBoot.StockViewer_SPB.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.SpringBoot.StockViewer_SPB.entity.OHLC;
import com.SpringBoot.StockViewer_SPB.entity.Stock;
import com.SpringBoot.StockViewer_SPB.repository.OhlcRepo;
import com.SpringBoot.StockViewer_SPB.repository.StockRepo;

@Service
public class StockService {

	@Autowired
	private StockRepo stockRepo;

	@Autowired
	private OhlcRepo ohlcRepo;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

	public boolean uploadCSV(MultipartFile file) {

		boolean processed;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			String line;

			List<Stock> stocks = new ArrayList();

			List<OHLC> ohlcList = new ArrayList();

			boolean firstLineSkipped = false; // Flag to skip the first line

			while ((line = br.readLine()) != null) {

				if (!firstLineSkipped) {
					firstLineSkipped = true;
					continue; // Skip the first line
				}

				String[] data = line.split(",");

				if (data[1].equals("EQ")) { // Check if series equals "EQ"

					Stock stock = new Stock();
					OHLC ohlc = new OHLC();

					boolean symbolExists = false;
					Stock stockBySymbol = stockRepo.getStockBySymbol(data[0]);
					if (stockBySymbol != null) {
						symbolExists = true;
						stock = stockBySymbol;
					}

					if (!symbolExists) {
						stock.setSymbol(data[0]);
						stock.setSeries(data[1]);
					}
					ohlc.setOpen(Double.parseDouble(data[2]));
					ohlc.setHigh(Double.parseDouble(data[3]));
					ohlc.setLow(Double.parseDouble(data[4]));
					ohlc.setClose(Double.parseDouble(data[5]));
					ohlc.setLast(Double.parseDouble(data[6]));
					ohlc.setPrevclose(Double.parseDouble(data[7]));
					ohlc.setTimestamp(dateFormat.parse(data[10]));
					ohlc.setIsin(data[12]);

					ohlc.setStock(stock);

					if (stockBySymbol == null) {
						stocks.add(stock);
					}
					ohlcList.add(ohlc);
				}
			}

			List<Stock> fileSaved = stockRepo.saveAll(stocks);
			List<OHLC> ohlcSaved = ohlcRepo.saveAll(ohlcList);

			if (fileSaved != null && ohlcSaved != null) {
				processed = true;
			} else {
				processed = false;
			}

		} catch (Exception e) {
			throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
		}

		return processed;

	}

	public List<Object[]> getAllData() {
		return ohlcRepo.getAllOHLCDataWithStock();
	}
	
	public List<Object[]> getAllDataOfSpecificSymbol(String symbol) {
		return ohlcRepo.getAllDataOfSpecificSymbol(symbol);
	}
	
	public List<Object[]> getAllDataOfSpecificSymbolOnlyLatest(String symbol) {
		return ohlcRepo.getAllDataOfSpecificSymbolOnlyLatest(symbol);
	}
	
	public List<Object[]> fetchOnlyLatestUniqueStockRecords() {
		return ohlcRepo.findLatestUniqueStockRecords();
	}
	
	public List<Object[]> getDataByDateRange(Date startDate , Date endDate){
		
		return ohlcRepo.getDataByDateRange(startDate, endDate);
		 
	}
	

}
