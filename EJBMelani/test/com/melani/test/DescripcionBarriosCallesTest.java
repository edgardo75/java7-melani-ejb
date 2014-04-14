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
public class DescripcionBarriosCallesTest {
    
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
    public DescripcionBarriosCallesTest() {
            
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
     public void chequearDescripcion() {
            //assertEquals(true, desc.validate("6 ' av.argentina"));
            assertEquals(true, ProjectHelpers.DescripcionValidator.validate("bladés"));
//            assertEquals(true, desc.validate("15 de abril"));
//            assertEquals(true, desc.validate  ("???%$%==%$#))"));
//            String s = " humbapumpa jim";
//                assertTrue(s.matches(".*(jim|joe).*"));
//                s = "humbapumpa jom";
//                assertFalse(s.matches(".*(jim|joe).*"));
//                s = "humbaPumpa joe";
//                assertTrue(s.matches(".*(jim|joe).*"));
//                s = "humbapumpa joe jim";
//                assertTrue(s.matches(".*(jim|joe).*"));
     
     }
}
