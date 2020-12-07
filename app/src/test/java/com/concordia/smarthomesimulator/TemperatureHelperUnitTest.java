package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.helpers.TemperatureHelper;
import org.junit.Test;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_SUMMER_END;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_SUMMER_START;
import static com.concordia.smarthomesimulator.helpers.UserbaseHelper.getUserWithCredentials;
import static org.junit.Assert.*;

public class TemperatureHelperUnitTest {
    @Test
    public void temperatureHelperKnowsIfMonthIsinSummer() {
        // Setup
        int summerStart = DEFAULT_SUMMER_START;
        int summerEnd = DEFAULT_SUMMER_END;
        int summerMonth = 6;
        int winterMonth = 12;

        // Test
        assertTrue(TemperatureHelper.isInSummer(summerStart, summerEnd, summerMonth));
        assertFalse(TemperatureHelper.isInSummer(summerStart, summerEnd, winterMonth));
    }
}
