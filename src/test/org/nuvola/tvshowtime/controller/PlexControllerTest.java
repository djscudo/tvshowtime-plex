package org.nuvola.tvshowtime.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.config.AppConfig;
import org.nuvola.tvshowtime.service.TokenService;
import org.nuvola.tvshowtime.service.TvTimeService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.nuvola.tvshowtime.TestBase.readFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class PlexControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TvTimeService m_tvTimeService;

    @Mock
    private TokenService m_tokenService;

    @InjectMocks
    private PlexController m_plexController;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(m_plexController).build();
    }

    @Test
    public void testPayloadMovie() throws Exception {
        String payload = readFile("plex_payload_movie_eventStop.json");
        Token t = new Token();
        t.setIdUser(1L);
        t.setUser("NAME");
        t.setToken("ABCTOKEN");

        when(m_tokenService.getTokenByUserId(1L)).thenReturn(Collections.singleton(t));

        mockMvc.perform(post("/plex/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .param("payload", payload))
                .andExpect(status().isOk());
        verify(m_tvTimeService, times(0)).markEpisodesAsWatched(anySet(), anySet());
    }

    @Test
    public void testPayloadEpisode() throws Exception {
        String payload = readFile("plex_payload_episode_eventPause.json");

        mockMvc.perform(post("/plex/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .param("payload", payload))
                .andExpect(status().isOk());

        verify(m_tvTimeService, times(0)).markEpisodesAsWatched(anySet(), anySet());
    }

    @Test
    public void testPayloadEmpty() throws Exception {
        mockMvc.perform(post("/plex/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .param("payload", ""))
                .andExpect(status().isBadRequest());
        verify(m_tvTimeService, times(0)).markEpisodesAsWatched(anySet(), anySet());
    }

    @Test
    public void testPayloadNull() throws Exception {
        mockMvc.perform(post("/plex/webhook")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(m_tvTimeService, times(0)).markEpisodesAsWatched(anySet(), anySet());
    }

}