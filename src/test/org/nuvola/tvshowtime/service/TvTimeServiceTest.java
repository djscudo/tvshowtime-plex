package org.nuvola.tvshowtime.service;


import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.nuvola.tvshowtime.TestBase;
import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.tvshowtime.AccessToken;
import org.nuvola.tvshowtime.config.TokenConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TvTimeServiceTest extends TestBase {

    @Spy
    private RestTemplate m_restTemplate = new RestTemplate();

    @Spy
    private AccessToken m_accessToken;

    @Spy
    private TokenConfig m_tokenConfig;

    @Spy
    @InjectMocks
    private TvTimeService m_tvTimeService;
    private MockRestServiceServer mockServer = MockRestServiceServer.createServer(m_restTemplate);

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequestAccessToken() throws Exception {
        String tvTimeResponse = readFile("tvtime_request_device_code.json");

        mockServer.expect(requestTo("https://api.tvtime.com/v1/oauth/device/code"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        m_tvTimeService.requestAccessToken();
    }

    @Test
    public void markEpisodeEmptySet() {
        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");

        m_tvTimeService.markEpisodesAsWatched(Collections.emptySet(), Collections.emptySet());
    }

    @Test
    public void markEpisode() {
        String tvTimeResponse = readFile("tvtime_episode_not_found.json");

        Set<String> episodes = new HashSet<>(Arrays.asList("episode1", "episode2"));

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");
        mockServer.expect(times(2),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=TokenTvTime"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        m_tvTimeService.markEpisodesAsWatched(episodes, Collections.emptySet());
    }

    @Test
    public void markEpisodeSuccess() {
        String tvTimeResponse = readFile("tvtime_ok_response.json");

        Set<String> episodes = new HashSet<>(Arrays.asList("episode1", "episode2"));

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");
        mockServer.expect(times(2),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=TokenTvTime"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        m_tvTimeService.markEpisodesAsWatched(episodes, Collections.emptySet());
    }

    @Test
    public void markEpisodeTestRateLimit() {
        String tv_rateLimit = readFile("tvtime_rate_limit_exceeded.json");
        String tvTimeResponse = readFile("tvtime_ok_response.json");

        Set<String> episodes = new HashSet<>(Arrays.asList("episode1", "episode2"));

        long reset = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RateLimit-Limit", "10");
        headers.set("X-RateLimit-Remaining", "0");
        headers.set("X-RateLimit-Reset", Long.toString((reset / 1000) + 15));
        headers.set(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON));
        mockServer.expect(times(2), requestTo("https://api.tvtime.com/v1/checkin?access_token=TokenTvTime"))
                .andRespond(withStatus(HttpStatus.OK).headers(headers).body(tvTimeResponse));

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");

        m_tvTimeService.markEpisodesAsWatched(episodes, Collections.emptySet());
    }

    @Test
    public void loadAccessToken() throws Exception {
        String tvTimeResponse = readFile("tvtime_access_token.json");

        when(m_tokenConfig.getTokenStore()).thenReturn("TokenTvTimeSTORE");

        mockServer.expect(requestTo("https://api.tvtime.com/v1/oauth/access_token"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        String deviceCode = m_tvTimeService.loadAccessToken("deviceCode");
        Assert.assertNotNull(deviceCode);
        Assert.assertEquals("2343", deviceCode);
    }

    @Test
    public void markEpisodeSuccessDifferentToken() {
        String tvTimeResponse = readFile("tvtime_ok_response.json");
        Set<String> episodes = new HashSet<>(Collections.singletonList("episode1"));
        Set<String> tokens = new HashSet<>(Collections.singletonList("tokenDifferent"));

        long reset = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RateLimit-Limit", "10");
        headers.set("X-RateLimit-Remaining", "10");
        headers.set("X-RateLimit-Reset", Long.toString((reset / 1000) + 15));

        mockServer.expect(times(1),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=tokenDifferent"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON).headers(headers));

        m_tvTimeService.markEpisodesAsWatched(episodes, tokens);
    }

    @Test
    public void markEpisodeSuccessDifferentTokens() {
        String tvTimeResponse = readFile("tvtime_ok_response.json");
        Set<String> episodes = new HashSet<>(Collections.singletonList("episode1"));
        Set<String> tokens = new HashSet<>(Arrays.asList("tokenDifferent", "moreToken"));

        long reset = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RateLimit-Limit", "10");
        headers.set("X-RateLimit-Remaining", "10");
        headers.set("X-RateLimit-Reset", Long.toString((reset / 1000) + 15));

        mockServer.expect(times(1),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=moreToken"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON).headers(headers));
        mockServer.expect(times(1),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=tokenDifferent"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON).headers(headers));

        m_tvTimeService.markEpisodesAsWatched(episodes, tokens);
    }

}
