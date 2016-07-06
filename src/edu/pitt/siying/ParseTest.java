package edu.pitt.siying;

/**
 * @author siying
 * As a user,
 * I would like to input something in the input box and do parsing 
 * So that the input could be parsed properly
 * 
 */

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

public class ParseTest {

	static WebDriver driver = new HtmlUnitDriver();
	static WebElement inputBox;
	static WebElement parseButton;
	
	// start at the home page for each test
	@Before
	public void setUp() throws Exception {
		driver.get("http://lit-bayou-7912.herokuapp.com/");
		inputBox = driver.findElement(By.id("code_code"));
		parseButton = driver.findElement(By.xpath("(//input[@name='commit'])[2]"));
	}

	//Given I'm in the first page
	//And I type nothing in the input box
	//When I click "Parse" button
	//Then there should be still two parts starting with "program" in the result
	@Test
	public void testProperFormat() {

		//input nothing
		inputBox.sendKeys("");
		
		parseButton.click();
		try {
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}
		List<WebElement> parseResults = driver.findElements(By.tagName("p"));
		
		//results should contain three p tags
		if(parseResults.size()!=3) assertTrue(false);
		else assertTrue(parseResults.get(0).getText().contains("program")&&
				parseResults.get(1).getText().contains("program"));
	}
	//Given I'm in the first page
	//And I type some equations in the input box
	//When I click "Parse" button
	//Then there is no content presenting whitespace and newline
	@Test
	public void testNoWhiteSpaces() {
		
		//Initialize input and parse
		String inputText = "a = 3\n" + "b = 4\n" + "c=a+b";
		inputBox.sendKeys(inputText);
		
		parseButton.click();
		
		try {
			//get the first part in the parse result
			WebElement parseResults = driver.findElement(By.tagName("code"));
			String parse = parseResults.getText();
			
			//should not contain whitespace and newline elements
			assertFalse(parse.contains("[1, 1]")||parse.contains("[1, 3]")||parse.contains("[1, 5]")||
					parse.contains("[2, 1]")||parse.contains("[2, 3]")||parse.contains("[2, 5]"));
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}
		
	}

}
