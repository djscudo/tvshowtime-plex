package org.nuvola.tvshowtime.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nuvola.tvshowtime.business.plex.*;
import org.nuvola.tvshowtime.config.PMSConfig;
import org.nuvola.tvshowtime.exceptions.PlexNotAvailableException;
import org.nuvola.tvshowtime.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.nuvola.tvshowtime.util.Constants.PMS_GET_ACCOUNTS;
import static org.nuvola.tvshowtime.util.Constants.PMS_WATCH_HISTORY;

@Service
public class PlexService {

    private static final Logger LOG = LoggerFactory.getLogger(PlexService.class);

    @Autowired
    private PMSConfig pmsConfig;
    @Autowired
    private RestTemplate pmsTemplate;
    private Map<String, Long> m_users = new HashMap<>();
    private String m_url;
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    List<Account> getUsersInPlex() throws PlexNotAvailableException {
        m_url = pmsConfig.getPath() + PMS_GET_ACCOUNTS;
        appendPlexToken();
        LOG.debug("Get users from plex with url: " + m_url);
        PlexResponse plexResponse = null;
        try {
            String response = pmsTemplate.getForObject(m_url, String.class);
            plexResponse = OBJECT_MAPPER.readValue(response, PlexResponse.class);
        }
        catch (Exception e) {
            throw new PlexNotAvailableException(e.getMessage());
        }
        return plexResponse.getMediaContainer().getAccount();
    }

    void updateUserObject() throws PlexNotAvailableException {
        List<Account> usersInPlex = getUsersInPlex();
        usersInPlex.stream()
                .filter(user -> !user.getName().isEmpty())
                .forEach(user -> m_users.put(user.getName(), user.getId()));
    }

    public Map<String, Long> getUsers() throws PlexNotAvailableException {
        if (m_users.isEmpty()) {
            updateUserObject();
        }
        return m_users;
    }

    Set<String> getWatchedOnPlex() throws PlexNotAvailableException {
        Map<String, Long> users = getUsers();

        Set<String> watched = new HashSet<>();
        m_url = pmsConfig.getPath() + PMS_WATCH_HISTORY;

        appendPlexToken();

        PlexResponse plexResponse = null;
        try {
            String response = pmsTemplate.getForObject(m_url, String.class);
            plexResponse = OBJECT_MAPPER.readValue(response, PlexResponse.class);
        }
        catch (Exception e) {
            throw new PlexNotAvailableException(e.getMessage());
        }
        LOG.debug(plexResponse.getMediaContainer().toString());

        for (Video video : plexResponse.getMediaContainer().getMetaData()) {
            LocalDateTime date = DateUtils.getDateTimeFromTimestamp(video.getViewedAt());

            LOG.debug(video.toString());

            // If present, mark as watched only episodes for configured user
            if (pmsConfig.getUsername() != null) {
                if (!video.getAccountID().equals(m_users.get(pmsConfig.getUsername()))) {
                    continue;
                }
            }

            // Mark as watched only today and yesterday episodes
            if (DateUtils.isTodayOrYesterday(date) || pmsConfig.getMarkall()) {
                if (video.getType().equals("episode")) {
                    String episode = generateEpisodeName(video);
                    watched.add(episode);
                }
                /*
                 * Enable when TvTime should accept movie

                else if (metaData.getType().equals("movie")) {
                    watched.add(metaData.getTitle());
                }
                */
            }
        }
        return watched;
    }

    private void appendPlexToken() {
        if (pmsConfig.getToken() != null && !pmsConfig.getToken().isEmpty()) {
            m_url += "?X-Plex-Token=" + pmsConfig.getToken();
            LOG.debug("Calling Plex with a X-Plex-Token = " + pmsConfig.getToken());
        }
        else {
            throw new IllegalArgumentException("Plex Token is missing in application.properties");
        }
    }

    public static String generateEpisodeName(Metadata video) {
        return video.getGrandparentTitle() +
                " - S" + video.getParentIndex() +
                "E" + video.getIndex();
    }

}
