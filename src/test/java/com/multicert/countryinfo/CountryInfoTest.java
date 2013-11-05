/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multicert.countryinfo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author floriano.alves
 */
public class CountryInfoTest {
    
    public CountryInfoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getHtml method, of class CountryInfo.
     */
    @org.junit.Test
    public void testGetHtml1() {
        System.out.println("getHtml");
        String countryCode = "PT";
        CountryInfo instance = new CountryInfo();
        String result = instance.getHtml(countryCode);
        assertFalse(result.isEmpty());
    }
    
    @org.junit.Test
    public void testGetHtml2() {
        System.out.println("getHtml");
        String countryCode = "ES";
        CountryInfo instance = new CountryInfo();
        String result = instance.getHtml(countryCode);
        assertFalse(result.isEmpty());
    }
    
    @org.junit.Test
    public void testGetHtml3() {
        System.out.println("getHtml");
        String countryCode = "FR";
        CountryInfo instance = new CountryInfo();
        String result = instance.getHtml(countryCode);
        assertFalse(result.isEmpty());
    }

    /**
     * Test of putHtml method, of class CountryInfo.
     */
    @org.junit.Test
    public void testPutHtml() {
        System.out.println("putHtml");
        String content = "";
        CountryInfo instance = new CountryInfo();
        instance.putHtml(content);
    }
}