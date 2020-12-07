package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.singletons.LayoutSingleton;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LayoutSingletonUnitTest {
    @Test
    public void testSingletonPattern() {
        // Setup
        LayoutSingleton layoutSingleton = LayoutSingleton.getInstance();
        LayoutSingleton sameReferenceToLayoutSingleton = LayoutSingleton.getInstance();
        // Test
        assertEquals(layoutSingleton, sameReferenceToLayoutSingleton);
    }
}
