package org.nuvola.tvshowtime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.plex.PayLoad;
import org.nuvola.tvshowtime.exceptions.TokenStoreNotFoundException;
import org.nuvola.tvshowtime.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PlexController {

    private static final Logger LOG = LoggerFactory.getLogger(PlexController.class);

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Autowired
    private TvTimeService m_tvTimeService;
    @Autowired
    private TokenService m_tokenService;

    @PostMapping(value = "/plex/webhook")
    @Consumes("multipart/form-data")
    @Produces("multipart/form-data")
    @POST
    public ResponseEntity webhooks(@FormParam("payload") String payload) {

        LOG.debug("request received... ");

        if (!StringUtils.hasText(payload)) {
            LOG.error("payload empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No request found.");
        }

        try {
            PayLoad payLoad = OBJECT_MAPPER.readValue(payload, PayLoad.class);

            LOG.debug(payLoad.toString());

            if (payLoad.getEvent().equalsIgnoreCase("media.scrobble")) {
                //TODO: call TvTime checkIn when episode is completed
                Set<Token> tokensByUserId = m_tokenService.getTokenByUserId(payLoad.getAccount().getId());

                Set<String> tokens = tokensByUserId.stream().map(Token::getToken).collect(Collectors.toSet());

                Set<String> episodes = new HashSet<>(Collections.singletonList(payLoad.getMetadata().getEpisodeOrMovieName()));
                m_tvTimeService.markEpisodesAsWatched(episodes, tokens);

                LOG.debug("response sent ");
            }
            else {
                LOG.info("Event received: " + payLoad.getEvent());
            }

            return ResponseEntity.status(HttpStatus.OK).body("OK");
        }
        catch (IOException | TokenStoreNotFoundException | ClassNotFoundException e) {
            LOG.error("IOException", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
