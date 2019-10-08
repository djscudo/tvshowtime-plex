package org.nuvola.tvshowtime.business;

import java.io.Serializable;
import java.util.*;


public class TokenStore implements Serializable {

    private LinkedHashSet<Token> m_tokenList = new LinkedHashSet<>();

    public LinkedHashSet<Token> getTokenList() {
        return m_tokenList;
    }

    public void setTokenList(LinkedHashSet<Token> tokenList) {
        m_tokenList = tokenList;
    }

}