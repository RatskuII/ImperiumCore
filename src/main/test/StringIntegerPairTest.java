import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StringIntegerPairTest {

    @Test
    public void test() {
        String input, expected;
        int expectedInt;

        input = "fffts:55";
        expected = "fffts";
        expectedInt = 55;
        Assert.assertEquals(DataUtil.parseData(input).key(), expected);
        Assert.assertEquals(DataUtil.parseData(input).value(), expectedInt);


        input = "foobar";
        String finalInput = input;
        Assert.assertThrows(() -> DataUtil.parseData(finalInput));
    }
}
