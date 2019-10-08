package org.nuvola.tvshowtime.service;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.TokenStore;
import org.nuvola.tvshowtime.config.TokenConfig;

import java.util.LinkedHashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TokenServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private TokenConfig m_tokenConfig;

    @Spy
    @InjectMocks
    TokenService m_tokenService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveSingleToken() throws Exception {
        when(m_tokenConfig.getTokenStore()).thenReturn("tokenStore");

        m_tokenService.saveToken(new Token(1L, "name", "token"));

        LinkedHashSet<Token> tokens = m_tokenService.getTokens();
        assertEquals("[Token{idUser=1, user='name', token='token'}]", tokens.toString());

        m_tokenService.cleanTokens();
    }

    @Test
    public void saveDoubleTokens() throws Exception {
        when(m_tokenConfig.getTokenStore()).thenReturn("tokenStore");

        m_tokenService.saveToken(new Token(1L, "name", "token"));
        m_tokenService.saveToken(new Token(2L, "second", "toc"));

        LinkedHashSet<Token> tokens = m_tokenService.getTokens();
        assertEquals("[Token{idUser=1, user='name', token='token'}, Token{idUser=2, user='second', token='toc'}]", tokens.toString());

        m_tokenService.cleanTokens();
    }
}