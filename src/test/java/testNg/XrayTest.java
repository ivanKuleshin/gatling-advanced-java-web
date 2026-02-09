package testNg;

import org.testng.Assert;
import org.testng.annotations.Test;

public class XrayTest {

    @Test
    public void testXrayPluginIntegration() {
        int randomNumber = (int) (Math.random() * 100);
        Assert.assertTrue(randomNumber >= 0 && randomNumber < 100, "Random number should be between 0 and 99");
    }
}
