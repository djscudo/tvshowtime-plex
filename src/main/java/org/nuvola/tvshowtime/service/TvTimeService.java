package org.nuvola.tvshowtime.service;

import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.tvshowtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.nuvola.tvshowtime.util.Constants.*;
import static org.springframework.http.HttpMethod.POST;

@Service
public class TvTimeService {

    private static final Logger LOG = LoggerFactory.getLogger(TvTimeService.class);

    @Autowired
    private TokenService m_tokenService;

    @Autowired
    private RestTemplate m_tvTimeTemplate;

    public AuthorizationCode requestAccessToken() throws Exception {
        LOG.info("RequestAccessToken....");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>("client_id=" + TVST_CLIENT_ID, headers);

        ResponseEntity<AuthorizationCode> content = m_tvTimeTemplate.exchange(TVST_AUTHORIZE_URI, POST, entity,
                AuthorizationCode.class);
        AuthorizationCode authorizationCode = content.getBody();

        if (authorizationCode != null && authorizationCode.getResult().equals("OK")) {
            return authorizationCode;
        }
        else {
            throw new Exception("OAuth authentication TVShowTime failed.");
        }
    }

    public String loadAccessToken(String deviceCode) throws Exception {
        String query = "client_id=" + TVST_CLIENT_ID +
                "&client_secret=" + TVST_CLIENT_SECRET +
                "&code=" + deviceCode;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        ResponseEntity<AccessToken> content = m_tvTimeTemplate.exchange(TVST_ACCESS_TOKEN_URI, POST, entity,
                AccessToken.class);
        if (Objects.requireNonNull(content.getBody()).getResult().equals("OK")) {
            return content.getBody().getAccess_token();
        }
        else {
            if (!content.getBody().getMessage().equals("Authorization pending")
                    && !content.getBody().getMessage().equals("Slow down")) {
                LOG.error("Unexpected error did arrive, please reload the service :-(");
                throw new Exception("Unexpected error did arrive, please reload the service :-(");
            }
            throw new Exception("");
        }
    }

    public void markEpisodesAsWatched(Set<String> episodes, Set<String> tokens) {
        for (String token : tokens) {
            markEpisodesAsWatched(episodes, token);
        }
    }

    private void markEpisodesAsWatched(Set<String> episodes, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("User-Agent", TVST_USER_AGENT);

        String checkInUrl = TVST_CHECKIN_URI + "?access_token=" + token;

        for (String episode : episodes) {
            HttpEntity<String> entity = new HttpEntity<>("filename=" + episode, headers);

            ResponseEntity<Message> content = m_tvTimeTemplate.exchange(checkInUrl, POST, entity, Message.class);
            Message message = content.getBody();

            if (message != null && message.getResult().equals("OK")) {
                LOG.info("Marked " + episode + " as watched in TVShowTime for token:" + token);
            }
            else {
                LOG.error("Error while marking [" + episode + "] as watched in TVShowTime ");
            }

            // Check if we are below the Rate-Limit of the API
            if (!Objects.requireNonNull(content.getHeaders().get(TVST_RATE_REMAINING_HEADER)).isEmpty()) {

                int remainingApiCalls = Integer.parseInt(content.getHeaders().get(TVST_RATE_REMAINING_HEADER).get(0));
                long epocNewRateLimit = (Long.valueOf(content.getHeaders().get(TVST_RATE_RESET_HEADER).get(0)) * 1000);
                if (remainingApiCalls == 0) {
                    try {
                        LOG.info("Consumed all available TVShowTime API calls slots, waiting for new slots at " + epocNewRateLimit);
                        epocNewRateLimit = epocNewRateLimit - System.currentTimeMillis();
                        if (epocNewRateLimit > 0) {
                            Thread.sleep(epocNewRateLimit);
                        }
                    }
                    catch (Exception e) {
                        LOG.error(e.getMessage());
                    }
                }

            }
        }
    }
}
