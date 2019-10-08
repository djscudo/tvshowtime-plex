package org.nuvola.tvshowtime.business;

import java.io.Serializable;
import java.util.Objects;

public class Token implements Serializable {

    private Long m_idUser;
    private String m_user;
    private String m_token;

    public Token(){
    }

    public Token(Long idUser, String user, String token) {
        m_idUser = idUser;
        m_user = user;
        m_token = token;
    }

    public Long getIdUser() {
        return m_idUser;
    }

    public void setIdUser(Long idUser) {
        m_idUser = idUser;
    }

    public String getUser() {
        return m_user;
    }

    public void setUser(String user) {
        m_user = user;
    }

    public String getToken() {
        return m_token;
    }

    public void setToken(String token) {
        m_token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return m_idUser.equals(token.m_idUser) &&
                m_user.equals(token.m_user) &&
                m_token.equals(token.m_token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_idUser, m_user, m_token);
    }

    @Override
    public String toString() {
        return "Token{" +
                "idUser=" + m_idUser +
                ", user='" + m_user + '\'' +
                ", token='" + m_token + '\'' +
                '}';
    }
}
