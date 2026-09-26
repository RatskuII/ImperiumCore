import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StringMatcherTest {

    @Test
    public void test() {
        String original, input1, input2, input3;

        original = "test";
        input1 = "test";
        input2 = "foo";
        input3 = "bar";
        Assert.assertTrue(DataUtil.matches(original, input1, input2, input3));

        original = "";
        input1 = "673";
        Assert.assertFalse(DataUtil.matches(original, input1, input2, input3));
    }
}
