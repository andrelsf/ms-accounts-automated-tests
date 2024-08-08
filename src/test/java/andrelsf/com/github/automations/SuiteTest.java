package andrelsf.com.github.automations;

import andrelsf.com.github.automations.suites.ApiCustomersSuiteTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    ApiCustomersSuiteTest.class
})
public class SuiteTest {

}
