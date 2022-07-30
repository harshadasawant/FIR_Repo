package com.police.fir;

import com.police.fir.bean.CitizenFirSearchBean;
import com.police.fir.bean.FIRSearchBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class FIRApplicationTests {

	@Test
	void contextLoads() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			FIRSearchBean firSearchBean = objectMapper.readValue(new File("data/data.json"), FIRSearchBean.class);
			for(CitizenFirSearchBean bean : firSearchBean.getList()){
				System.out.println("=="+bean.getFirNumDisplay());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		try {

//			PoliceStationResponseBean policeStationResponseBean = objectMapper.readValue(new File("data/stationcode.json"), PoliceStationResponseBean.class);
//			List<List<String>> rows = policeStationResponseBean.getRows();
//			System.out.println(policeStationResponseBean.getRows());
//			for(List<String> station: rows){
//				for(String s : station){
//					System.out.println(s);
//				}
//				System.out.println("===============");
//			}

//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
	}
}
