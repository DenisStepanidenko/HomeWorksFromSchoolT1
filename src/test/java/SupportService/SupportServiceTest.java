package SupportService;

import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;

public class SupportServiceTest {

    @Test
    public void support_service_should_return_support_phrase(){
        SupportService supportService = SupportServiceFactory.getInstance();

        assertEquals("Hey!" , supportService.getPhrase());
    }
}
