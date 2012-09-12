/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.test.InstrumentationTestCase;

/**
 *
 * @author MaYichao
 */
public class UtilsTest extends InstrumentationTestCase {

    public UtilsTest() {
//        this("com.expro.future.card", LoginAct.class);
    }

//    public UtilsTest(Class activityClass) {
//        super(activityClass);
//    }
//
//    public UtilsTest(String pkg, Class activityClass) {
//        super(pkg, activityClass);
//    }

    public void testPasswordCrypto() {
        System.out.println("testPasswordCrypto");

        String pw = "123456";
        String p2 = Utils.passwordCrypto(pw);
        System.out.println(pw);
        System.out.println(p2);
    }
}
