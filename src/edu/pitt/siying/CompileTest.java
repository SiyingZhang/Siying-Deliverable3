package edu.pitt.siying;

/**
 * @author siying
 * As a user,
 * I would like to input something in the input box and do compiling 
 * So that the input content could be compiled properly
 * 
 */

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

public class CompileTest {

	static WebDriver driver = new HtmlUnitDriver();
	static WebElement inputBox;
	static WebElement compileButton;

	//start at the main page for every test case
	@Before
	public void setUp() throws Exception {
		driver.get("http://lit-bayou-7912.herokuapp.com/");
		inputBox = driver.findElement(By.id("code_code"));
		compileButton = driver.findElement(By.xpath("(//input[@name='commit'])[3]"));
		
	}

	//Given I'm in the first page
	//And I type the program with "puts" statement
	//When I click the "Compile" button
	//Then the compiled result should have the putstring YARV operation
	@Test
	public void testPutString01() {
		
		//type something with puts statement
		String inputText = "puts \"" + "Today is a sunny day!\"";
		inputBox.sendKeys(inputText);
		
		compileButton.click();
		
		try {
			WebElement compileResult = driver.findElement(By.tagName("code"));
			String compiling = compileResult.getText();
			
			assertTrue(compiling.contains("putstring"));
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found");
		}
	}
	
	//Given I'm in the first page
	//And I type one program with "puts" statement without string behind
	//When I click the "Compile" button
	//Then the compiled result should not have the putstring YARV operation
	@Test
	public void testPutString02() {
		
		//type something with puts statement
		//puts a variable rather than a string
		String inputText = "a=3\n" + "puts" + " a";
		inputBox.sendKeys(inputText);
		
		compileButton.click();
		
		try {
			WebElement compileResult = driver.findElement(By.tagName("code"));
			String compiling = compileResult.getText();
			
			assertFalse (compiling.contains("putstring"));
			
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found");
		}
	}
	
	//Given I'm in the first page
	//And I type a program with some objects
	//When i click the "Parse" button
	//Then the objects are compiled by putobject operations
	@Test
	public void testPutObject() {
		
		int count = 0;
		//type something using four operators
		String inputText = "a = 3+2\n" + "b = 3-2\n" + "c = 3*2\n" + "d = 4/2";
		inputBox.sendKeys(inputText);
		
		compileButton.click();
		
		try {
			WebElement e = driver.findElement(By.tagName("code"));
			String compiled = e.getText();
			
			//System.out.println(compiled);
			String[] results = compiled.split("\\r?\\n");	//split by newline

			for(String s:results) {
				if(s.contains("putobject")) count++;
			}
			assertEquals(8, count);
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found");
		}
	}
	
	
	//Given I'm in the first page
	//And I type a program with four operators
	//When i click the "Parse" button
	//Then the compiled result should call four operations
	@Test
	public void testOperators() {
		
		//type something using four operators
		String inputText = "a = 1+2\n" + "b = 2-1\n" + "c = 1*2\n" + "d = 1/2";
		inputBox.sendKeys(inputText);
		
		compileButton.click();
		
		try {
			WebElement e = driver.findElement(By.tagName("code"));
			String compiled = e.getText();
			
			assertTrue(compiled.contains("opt_plus")&&compiled.contains("opt_minus")
					&&compiled.contains("opt_mult")&&compiled.contains("opt_div"));
		} catch (ElementNotFoundException e) {
			// TODO: handle exception
			fail("Element not found");
		}
	}
	
	//Given I'm in the first page
	//And I type the program with syntax error
	//When I click the "Parse" button
	//Then the syntax error should come up
	@Test
	public void testSyntaxError() {
		
		//Type something with syntax error in the puts statement
		String inputText = "a=3\n" + "puts \"" + "a: \"" + " a";
		inputBox.sendKeys(inputText);
		
		compileButton.click();
		
		//Get result
		try{
			
			WebElement compileResult = driver.findElement(By.tagName("code"));
			String result = compileResult.getText();

			assertTrue(result.contains("Syntax error"));
			
		} catch(ElementNotFoundException e) {
			fail("No element found.");
		}
	}

}
