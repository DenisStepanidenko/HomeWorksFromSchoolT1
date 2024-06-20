package SupportService;

public class SupportServiceFactory {

    private static final SupportService INSTANCE = new SupportServiceImpl();

    public static SupportService getInstance() {
        return INSTANCE;
    }
}
