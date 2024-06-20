package SupportService;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.assertEquals;

public class SupportManagerImplTest {

    @Test
    public void support_manager_should_return_support_phrase() throws InvocationTargetException, IllegalAccessException {
        var context = new ApplicationContext("SupportService.configuration");
        var supportManagerImpl = context.getInstance(SupportManager.class);
        assertEquals("Dear, Hey!" , supportManagerImpl.provideSupport());
    }
}
