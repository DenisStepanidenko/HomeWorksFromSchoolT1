package CheeringService;

import CheeringService.CheeringServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheeringServletTest {
    private CheeringServlet cheeringServlet;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private StringWriter responseWriter;

    @Test
    @DisplayName("Возврат сообщения об успешном добавлении фразы")
    public void handleDoGet() throws IOException, ServletException {
        String testPhrase = "Тестовая фраза";
        cheeringServlet = new CheeringServlet(new CheeringManager());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getParameter("phrase")).thenReturn(testPhrase);

        responseWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(responseWriter);

        when(response.getWriter()).thenReturn(writer);

        cheeringServlet.doPost(request, response);
        assertEquals("Добавлена фраза " + testPhrase, responseWriter.toString());
    }
}
