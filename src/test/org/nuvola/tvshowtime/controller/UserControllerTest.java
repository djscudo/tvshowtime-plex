package org.nuvola.tvshowtime.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.tvshowtime.AuthorizationCode;
import org.nuvola.tvshowtime.config.AppConfig;
import org.nuvola.tvshowtime.service.TokenService;
import org.nuvola.tvshowtime.service.TvTimeService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController m_userController;

    @Mock
    private TokenService m_tokenService;

    @Mock
    private TvTimeService m_tvTimeService;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(m_userController).build();
    }

    @Test
    public void testSaveUserToken() throws Exception {
        mockMvc.perform(post("/user/token")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("token", "newToken")
                .param("userId", "1234")
                .param("user", "UserName"))
                .andExpect(status().isOk());
        verify(m_tokenService, times(1)).saveToken(any(Token.class));
    }

    @Test
    public void testSaveUserTokenFail() throws Exception {
        mockMvc.perform(post("/user/token")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
        verify(m_tokenService, times(0)).saveToken(any(Token.class));
    }

    @Test
    public void testUserConfirmTokenFail() throws Exception {
        mockMvc.perform(post("/user/confirmToken")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
        verify(m_tokenService, times(0)).saveToken(any(Token.class));
    }

    @Test
    public void testUserConfirmToken() throws Exception {
        Token token = new Token(1234L, "UserName", "accessToken");

        when(m_tvTimeService.loadAccessToken("newDevice")).thenReturn(token.getToken());

        mockMvc.perform(post("/user/confirmToken")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("userId", String.valueOf(token.getIdUser()))
                .param("user", token.getUser())
                .param("deviceCode", "newDevice"))
                .andExpect(status().isOk());

        verify(m_tokenService, times(1)).saveToken(token);
    }

    @Test
    public void testUserDeleteToken() throws Exception {

        final String token = "TokenValue";
        final Long userId = 1111L;


        mockMvc.perform(post("/user/tokenDelete")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("userId", String.valueOf(userId))
                .param("token", token))
                .andExpect(status().isOk());

        verify(m_tokenService, times(1)).removeTokenByTokenName(userId, token);
    }

    @Test
    public void testUserDeleteTokenFail() throws Exception {

        final String token = null;
        final Long userId = 1111L;


        mockMvc.perform(post("/user/tokenDelete")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("userId", String.valueOf(userId))
                .param("token", token))
                .andExpect(status().isBadRequest());

        verify(m_tokenService, times(0)).removeTokenByTokenName(userId, token);
    }

    @Test
    public void testUserRequestToken() throws Exception {

        final String user = "username";
        final Long userId = 1111L;

        AuthorizationCode ac = new AuthorizationCode();
        ac.setDevice_code("12345Device");
        ac.setExpires_in(1111);
        ac.setInterval(1010);
        ac.setUser_code("UC10-UC10");
        ac.setVerification_url("http://fakeverificationurl.co");

        when(m_tvTimeService.requestAccessToken()).thenReturn(ac);

        String response = mockMvc.perform(post("/user/requestToken")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("userId", String.valueOf(userId))
                .param("user", user))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response, containsString("12345Device"));
        assertThat(response, containsString("UC10-UC10"));
        assertThat(response, containsString("http://fakeverificationurl.co"));
    }

}