package com.example.stockboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.stockboard.mapper")
public class StockBoardApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockBoardApplication.class, args);
	}

}
