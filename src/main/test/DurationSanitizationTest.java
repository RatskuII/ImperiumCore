import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationSanitizationTest {

    @Test
    public void test() {
        Duration initial;
        Duration expected;

        initial = DataUtil.parseDuration("4d");
        expected = Duration.of(4, ChronoUnit.DAYS);
        Assert.assertEquals(initial, expected);

        initial = DataUtil.parseDuration("1d14h");
        expected = Duration.ZERO
                .plusDays(1)
                .plusHours(14);
        Assert.assertEquals(initial, expected);

        initial = DataUtil.parseDuration("5h30s");
        expected = Duration.ZERO
                .plusHours(5)
                .plusSeconds(30);
        Assert.assertEquals(initial, expected);
    }
}
