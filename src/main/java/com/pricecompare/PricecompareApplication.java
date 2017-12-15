package com.pricecompare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootApplication
public class PricecompareApplication
{
    public static void main(String[] args) throws IOException
    {
        Resource resource = new ClassPathResource("static/phantomJs/phantomjs.exe");
		System.setProperty("phantomjs.binary.path", resource.getFile().getPath());
		SpringApplication.run(PricecompareApplication.class, args);
	}
}
