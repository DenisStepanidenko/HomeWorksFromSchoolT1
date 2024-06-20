package SupportService;

import SupportService.configuration.Logged;

@Controller
public interface SupportService {

    @Logged
    String getPhrase();
}
