package org.nuvola.tvshowtime.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nuvola")
public class TokenConfig {

    private String tokenStore;

    public String getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(String tokenStore) {
        this.tokenStore = tokenStore;
    }

}
