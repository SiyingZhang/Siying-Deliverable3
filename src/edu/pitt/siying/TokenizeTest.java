package edu.pitt.siying;

/**
 * @author siying
 * As a user,
 * I would like to input something and do tokenization 
 * So that the input could be tokenized properly
 * 
 */

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

public class TokenizeTest {
	
	static WebDriver driver = new HtmlUnitDriver();
	static WebElement inputBox;
	static WebElement tokenizerButton;
	
	// start at the home page
	// Get input box and tokenizer button
	@Before
	public void setUp() throws Exception {
		driver.get("http://lit-bayou-7912.herokuapp.com/");
		inputBox = driver.findElement(By.id("code_code"));
		tokenizerButton = driver.findElement(By.xpath("(//input[@name='commit'])[1]"));
	}
	
	//----------- 1 ------------
	//Given that I am in the first page
	//And I type something in the input box
	//When I click on "Tokenize" button
	//Then the titles of both pages include "Hoodpopper"
	@Test
	public void testTitle() {
		
		//Initialize inputText
		String inputText = "a=3";
		
		String firstPageTitle = driver.getTitle();
		
		//Type and click
		inputBox.sendKeys(inputText);
		tokenizerButton.click();
		
		String tokenTitle = driver.getTitle();
		
		assertTrue(firstPageTitle.contains("Hoodpopper")&&tokenTitle.contains("Hoodpopper")); 

	}

	//----------- 2 ------------
	//Given that I am in the first page
	//And I type an equation in the input box
	//When I click on "Tokenize" button
	//Then the amount of tokenized items is correct
	@Test
	public void testTokenizeAmount() {
		
		//Initialize inputText
		String inputText = "a = 3\n" + "b = 4\n" +
				"c=a+b" + "\n" + "puts " + "c";
		
		//Type and click
		inputBox.sendKeys(inputText);
		tokenizerButton.click();
		
		try {
			//Get the result
			WebElement tokenizerResult = driver.findElement(By.tagName("code"));
			String result = tokenizerResult.getText();
			
			String[] results = result.split("\\r?\\n");	//split by newline
			
			assertEquals(21, results.length); 
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}

	}
	
	//----------- 3 -------------
	//Given that I am in the first page
	//And I type an equation with whitespace (a = 3) in the input box
	//When I click on "Tokenizer"
	//Then I see two whitespaces show up at :on_sp 

	@Test
	public void testWhiteSpace() {
		
		// input a equation with whitespace
		String inputText1 = "a = 3";
		inputBox.sendKeys(inputText1);
		
		//click the button and get the result
		tokenizerButton.click();

		try {
			WebElement tokenizerResult = driver.findElement(By.tagName("code"));
			String result = tokenizerResult.getText();
			
			String[] results = result.split("\\r?\\n");	//split by newline
			
			//line 2 & 4 should contain symbol of whitespace
			assertTrue(results[1].contains("on_sp") && results[3].contains("on_sp"));
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}
		
	}
	
	//----------- 4 -------------
	//Given that I am in the first page
	//And I type an equation with several identifiers in the input box
	//When I click on "Tokenizer"
	//Then I see all identifiers show up at :on_ident 
	@Test
	public void testIdentifier() {
		
		//Initialize inputText
		String inputText = "a = 3\n" + "b = 4\n" +
				"c=a+b" + "\n" + "puts " + "c";
		
		//Type and click
		inputBox.sendKeys(inputText);
		tokenizerButton.click();
		
		try {
			
			//Get result to check on_ident
			WebElement tokenizerResult = driver.findElement(By.tagName("code"));
			String result = tokenizerResult.getText();

			String[] results = result.split("\\r?\\n");	//split by newline
			
			//line 1/7/13/15/17/19/21 should contain symbol of identifier
			assertTrue(results[0].contains("on_ident") && results[6].contains("on_ident")
					&& results[12].contains("on_ident")&& results[14].contains("on_ident")
					&& results[16].contains("on_ident")&& results[18].contains("on_ident")
					&& results[20].contains("on_ident"));
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}
		
	}
	
	//----------- 5 -------------
	//Given that I am in the first page
	//And I type an equation with several operator in the input box
	//When I click on "Tokenize" button
	//Then I see all operators show up at :on_op 
	@Test
	public void testOperations() {
		
		//Initialize inputText
		String inputText = "c=3+4" + "\n" + "d=2*3" + "\n" + 
				"a=3/4" + "\n" + "b=5-3";
		
		//Type and click
		inputBox.sendKeys(inputText);
		tokenizerButton.click();
		
		try {
			//Get result to check on_ident
			WebElement tokenizerResult = driver.findElement(By.tagName("code"));
			String result = tokenizerResult.getText();

			String[] results = result.split("\\r?\\n");	//split by newline
			
			int operatorsCount = 0;
			for(String s:results) {
				if(s.contains("on_op")) operatorsCount++;
			}
			
			//There should be 8 operators 
			assertEquals(8, operatorsCount);
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}
	}
	
	//----------- 6 -------------
	//Given that I am in the first page
	//And I type code with three lines in the input box
	//When I click on "Tokenize" button
	//Then I see two newline symbols show up at :on_nl 
	@Test
	public void testNewLine() {
		
		//Initialize inputText
		String inputText = "a = 3\n" + "b = 4\n" +
				"c=a+b";
		
		//Type and click
		inputBox.sendKeys(inputText);
		tokenizerButton.click();
		
		try {
			//Get result to check on_ident
			WebElement tokenizerResult = driver.findElement(By.tagName("code"));
			String result = tokenizerResult.getText();

			String[] results = result.split("\\r?\\n");	//split by newline
			
			//line 6/12 should present new line
			assertTrue(results[5].contains("on_nl") && results[11].contains("on_nl") );
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found!");
		}	
		
	}

}
