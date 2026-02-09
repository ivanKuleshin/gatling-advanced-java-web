package testNg;

import org.testng.Assert;
import org.testng.annotations.Test;

public class XrayAnotherRandomTest {

    @Test
    public void testAnotherRandomNumber2() {
        int randomNumber = (int) (Math.random() * 100);
        Assert.assertTrue(randomNumber >= 0 && randomNumber < 100,
                "Random number should be between0 and99");
    }

    @Test
    public void testAnotherRandomNumber3() {
        int randomNumber = (int) (Math.random() * 100);
        Assert.assertTrue(randomNumber >= 0 && randomNumber < 100,
                "Random number should be between0 and99");
    }
}
