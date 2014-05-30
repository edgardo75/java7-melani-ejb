/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.test;

import com.melani.utils.ProjectHelpers;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author win7
 */
public class EncriptTextTest {
    
    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }
    
    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     */
    public EncriptTextTest() {
        }
    
    /**
     *
     */
    @Before
    public void setUp() {
    }
    
    /**
     *
     */
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    /**
     *
     */
         @Test
    public void texto_encript_test() {
         //for (int i = 0; i < 1000; i++) {
             
             assertEquals("Exito", ProjectHelpers.ClaveSeguridad.encriptar("monoTitu29"));
             
         //}
         
    }
}
