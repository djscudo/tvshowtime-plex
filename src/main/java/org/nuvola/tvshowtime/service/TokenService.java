package org.nuvola.tvshowtime.service;

import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.TokenStore;
import org.nuvola.tvshowtime.config.TokenConfig;
import org.nuvola.tvshowtime.exceptions.TokenStoreNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private TokenConfig m_tokenConfig;

    private TokenStore m_tokenStore;

    public void saveToken(LinkedHashSet<Token> tokens) throws IOException {
        m_tokenStore.getTokenList().addAll(tokens);
        storeTokens();
    }

    public void saveToken(Token t) throws IOException {
        try {
            loadTokens();
        }
        catch (Exception e) {
            m_tokenStore = new TokenStore();
        }

        m_tokenStore.getTokenList().add(t);
        storeTokens();
    }

    LinkedHashSet<Token> getTokens() throws TokenStoreNotFoundException, IOException, ClassNotFoundException {
        loadTokens();
        return m_tokenStore.getTokenList();
    }

    public Set<Token> getTokenByUserId(Long userId) throws TokenStoreNotFoundException, IOException, ClassNotFoundException {
        return getTokens().stream().filter(x -> x.getIdUser().equals(userId)).collect(Collectors.toSet());
    }

    void cleanTokens() {
        File storeToken = new File(m_tokenConfig.getTokenStore());
        if (storeToken.exists()) storeToken.delete();
    }

    public void removeTokenByTokenName(Long userId, String token) throws TokenStoreNotFoundException, IOException, ClassNotFoundException {
        LinkedHashSet<Token> tokens = getTokens();
        LOG.debug("Remove token=");
        tokens.removeIf(x -> x.getToken().equals(token) && x.getIdUser().equals(userId));
        cleanTokens();
        saveToken(tokens);
    }

    private void storeTokens() throws IOException {
        try {
            LOG.info(m_tokenConfig.getTokenStore());
            File storeToken = new File(m_tokenConfig.getTokenStore());
            FileOutputStream fileOutputStream = new FileOutputStream(storeToken);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(m_tokenStore);
            objectOutputStream.close();
            fileOutputStream.close();
            LOG.info("Tokens store successfully inside a file...");
        }
        catch (Exception e) {
            LOG.error("Unexpected error did arrive when trying to store the Tokens in a file ");
            LOG.error(e.getMessage());
            throw e;
        }
    }

    private void loadTokens() throws IOException, ClassNotFoundException, TokenStoreNotFoundException {
        File storeToken = new File(m_tokenConfig.getTokenStore());
        if (storeToken.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(storeToken);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                m_tokenStore = (TokenStore) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
                LOG.info("Tokens loaded from file with success");
            }
            catch (Exception e) {
                LOG.error("Error parsing the Token stored in 'session_token'.");
                LOG.error("Please remove the 'session_token' file, and try again.");
                LOG.error(e.getMessage());
                throw e;
            }
        }
        else {
            LOG.info("Re-run the app to load info to TvTime ;-)");
            throw new TokenStoreNotFoundException(m_tokenConfig.getTokenStore());
        }
    }
}
