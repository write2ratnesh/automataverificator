/*
 * Developed by eVelopers Corporation - 23.05.2008
 */
package ru.ifmo.test.verifier.concurrent;

import junit.textui.TestRunner;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.extensions.RepeatedTest;

public class VerifierTestRunner {

    public static void main(String[] args) {
        TestSuite ts = new TestSuite(CompareTest.class);
        for (int i = 0; i < ts.testCount(); i++) {
            if (i % 2 == 1) {
                Test t = ts.testAt(i);
                Test repeated = new RepeatedTest(t, 5000);
                TestRunner.run(repeated);
            }
        }
    }
}
