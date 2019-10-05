package org.nuvola.tvshowtime;

import org.nuvola.tvshowtime.business.tvshowtime.*;
import org.nuvola.tvshowtime.config.TVShowTimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

import static org.nuvola.tvshowtime.util.Constants.*;
import static org.nuvola.tvshowtime.util.Constants.MINUTE_IN_MILIS;
import static org.springframework.http.HttpMethod.POST;

@Service
class TvTimeService {

    private static final Logger LOG = LoggerFactory.getLogger(TvTimeService.class);

    @Autowired
    private TVShowTimeConfig tvShowTimeConfig;

    @Autowired
    private PlexService m_plexService;

    private RestTemplate tvShowTimeTemplate;
    private AccessToken accessToken;
    private Timer tokenTimer;

    void init() {
        tvShowTimeTemplate = new RestTemplate();

        File storeToken = new File(tvShowTimeConfig.getTokenFile());
        if (storeToken.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(storeToken);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                accessToken = (AccessToken) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
                LOG.info("AccessToken loaded from file with success : " + accessToken);
            }
            catch (Exception e) {
                LOG.error("Error parsing the AccessToken stored in 'session_token'.");
                LOG.error("Please remove the 'session_token' file, and try again.");
                LOG.error(e.getMessage());
                System.exit(1);
            }

            try {
                m_plexService.getUsersInPlex();
                markEpisodesAsWatched(m_plexService.getWatchedOnPlex());
                LOG.info("All episodes are processed successfully ...");
                System.exit(0);
            }
            catch (Exception e) {
                LOG.error("Error during marking episodes as watched.");
                LOG.error("Error: ", e);

                System.exit(1);
            }
        }
        else {
            requestAccessToken();
        }
    }

    private void requestAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> entity = new HttpEntity<>("client_id=" + TVST_CLIENT_ID, headers);

            ResponseEntity<AuthorizationCode> content = tvShowTimeTemplate.exchange(TVST_AUTHORIZE_URI, POST, entity,
                    AuthorizationCode.class);
            AuthorizationCode authorizationCode = content.getBody();

            if (authorizationCode != null && authorizationCode.getResult().equals("OK")) {
                LOG.info("Linking with your TVShowTime account using the code " + authorizationCode.getDevice_code());
                LOG.info("Please open the URL " + authorizationCode.getVerification_url() + " in your browser");
                LOG.info("Connect with your TVShowTime account and type in the following code : ");
                LOG.info(authorizationCode.getUser_code());
                LOG.info("Waiting for you to type in the code in TVShowTime :-D ...");

                tokenTimer = new Timer();
                tokenTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            loadAccessToken(authorizationCode.getDevice_code());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000 * authorizationCode.getInterval(), 1000 * authorizationCode.getInterval());
            }
            else {
                LOG.error("OAuth authentication TVShowTime failed.");
                System.exit(1);
            }
        }
        catch (Exception e) {
            LOG.error("OAuth authentication TVShowTime failed.", e);
            System.exit(1);
        }
    }

    private void loadAccessToken(String deviceCode) throws Exception {
        String query = new StringBuilder("client_id=")
                .append(TVST_CLIENT_ID)
                .append("&client_secret=")
                .append(TVST_CLIENT_SECRET)
                .append("&code=")
                .append(deviceCode)
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        ResponseEntity<AccessToken> content = tvShowTimeTemplate.exchange(TVST_ACCESS_TOKEN_URI, POST, entity,
                AccessToken.class);
        accessToken = content.getBody();

        if (accessToken == null) {
            throw new Exception("Access token is null");
        }

        if (accessToken.getResult().equals("OK")) {
            LOG.info("AccessToken from TVShowTime with success : " + accessToken);
            tokenTimer.cancel();
            storeAccessToken();
            markEpisodesAsWatched(m_plexService.getWatchedOnPlex());
            System.exit(0);
        }
        else {
            if (!accessToken.getMessage().equals("Authorization pending")
                    && !accessToken.getMessage().equals("Slow down")) {
                LOG.error("Unexpected error did arrive, please reload the service :-(");
                tokenTimer.cancel();
                System.exit(1);
            }
        }
    }

    private void storeAccessToken() {
        try {
            File storeToken = new File(tvShowTimeConfig.getTokenFile());
            FileOutputStream fileOutputStream = new FileOutputStream(storeToken);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(accessToken);
            objectOutputStream.close();
            fileOutputStream.close();
            LOG.info("AccessToken store successfully inside a file...");
        }
        catch (Exception e) {
            LOG.error("Unexpected error did arrive when trying to store the AccessToken in a file ");
            LOG.error(e.getMessage());
            System.exit(1);
        }
    }

    private void markEpisodesAsWatched(Set<String> episodes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("User-Agent", TVST_USER_AGENT);

        String checkInUrl = new StringBuilder(TVST_CHECKIN_URI)
                .append("?access_token=")
                .append(accessToken.getAccess_token())
                .toString();

        for (String episode : episodes) {
            HttpEntity<String> entity = new HttpEntity<>("filename=" + episode, headers);

            ResponseEntity<Message> content = tvShowTimeTemplate.exchange(checkInUrl, POST, entity, Message.class);
            Message message = content.getBody();

            if (message != null && message.getResult().equals("OK")) {
                LOG.info("Marked " + episode + " as watched in TVShowTime");
            }
            else {
                LOG.error("Error while marking [" + episode + "] as watched in TVShowTime ");
            }

            // Check if we are below the Rate-Limit of the API
            if (content.getHeaders().get(TVST_RATE_REMAINING_HEADER) != null
                    && !content.getHeaders().get(TVST_RATE_REMAINING_HEADER).isEmpty()) {

                int remainingApiCalls = Integer.parseInt(content.getHeaders().get(TVST_RATE_REMAINING_HEADER).get(0));
                if (remainingApiCalls == 0) {
                    try {
                        LOG.info("Consumed all available TVShowTime API calls slots, waiting for new slots ...");
                        Thread.sleep(MINUTE_IN_MILIS);
                    }
                    catch (Exception e) {
                        LOG.error(e.getMessage());
                        System.exit(1);
                    }
                }

            }
        }
    }
}
