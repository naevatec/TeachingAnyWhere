package com.naevatec.taw.e2e.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.naevatec.taw.e2e.common.Constants.FOOTER;

public class Wait {
	
	public static WebDriverWait notTooMuch(WebDriver wd) {
		return new WebDriverWait(wd, 10);
	}
 
	public static WebDriverWait aLittle(WebDriver wd) {
		return new WebDriverWait(wd, 2);
	}

	public static void footer(WebDriver wd) {
		notTooMuch(wd).until(ExpectedConditions.presenceOfElementLocated(FOOTER));	
	}
	
}
