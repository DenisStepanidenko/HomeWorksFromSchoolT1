package CheeringService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CheeringServlet extends HttpServlet {
    private CheeringManager cheeringManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cheeringManager = new CheeringManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().append(cheeringManager.getPhrase());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().append(cheeringManager.addCheeringPhrase(req.getParameter("phrase")));

    }

    public CheeringServlet() {

    }

    public CheeringServlet(CheeringManager cheeringManager) {
        this.cheeringManager = cheeringManager;
    }
}
