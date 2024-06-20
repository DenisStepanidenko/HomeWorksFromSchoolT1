package SupportService.configuration;


import SupportService.SupportManagerImpl;
import SupportService.SupportService;
import SupportService.SupportServiceImpl;
import SupportService.SupportManager;
import SupportService.ProxyApplier;
import SupportService.ControllerLoggingProxyApplier;

@Configuration
public class SupportConfiguration {

    @Instance(priority = Integer.MAX_VALUE)
    public ProxyApplier controllerLoggingProxy() {
        return new ControllerLoggingProxyApplier();
    }

    @Instance()
    public SupportManager SupportManagerImpl(SupportService supportService) {
        return new SupportManagerImpl(supportService);
    }

    @Instance()
    public SupportService supportService() {
        return new SupportServiceImpl();
    }
}
