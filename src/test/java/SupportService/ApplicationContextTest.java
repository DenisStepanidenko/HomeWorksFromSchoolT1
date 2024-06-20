package SupportService;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ApplicationContextTest {


    @Test
    public void application_context_should_return_instance_by_class() throws InvocationTargetException, IllegalAccessException {
        var applicationContext = new ApplicationContext("configuration");
        assertEquals(SupportManagerImpl.class , applicationContext.getInstance(SupportManager.class).getClass());

    }
}
