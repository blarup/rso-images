package si.thoughts.texts.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("configuration-properties")
@ApplicationScoped
public class IntegrationProperties {
    @ConfigValue(value = "ratings-service.enabled", watch = true)
    private boolean integrateWithRatingsService;

    public boolean isIntegrateWithRatingsService()
    {
        return integrateWithRatingsService;
    }

    public void setIntegrateWithRatingsService(boolean integrateWithRatingsService){
        this.integrateWithRatingsService = integrateWithRatingsService;
    }
}
