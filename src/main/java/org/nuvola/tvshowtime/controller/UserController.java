package org.nuvola.tvshowtime.controller;

import org.nuvola.tvshowtime.business.Token;
import org.nuvola.tvshowtime.business.tvshowtime.AuthorizationCode;
import org.nuvola.tvshowtime.exceptions.PlexNotAvailableException;
import org.nuvola.tvshowtime.exceptions.TokenStoreNotFoundException;
import org.nuvola.tvshowtime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import java.io.IOException;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private TokenService m_tokenService;

    @Autowired
    private PlexService m_plexService;

    @Autowired
    private TvTimeService m_tvTimeService;

    @PostMapping("/user/token")
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveUserToken(String token, Long userId, String user) throws IOException {

        if (token == null || userId == null || user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameters Empty");
        }
        if (token.isEmpty() || user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameters Empty");
        }

        m_tokenService.saveToken(new Token(userId, user, token));

        return ResponseEntity.status(HttpStatus.OK).body("<head><meta http-equiv = \"refresh\" content = \"1; url = / \" /></head>Token Registered:" + token);
    }

        @PostMapping("/user/tokenDelete")
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity removeTokenByTokenName(Long userId, String token) throws TokenStoreNotFoundException, IOException, ClassNotFoundException {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameters Empty");
        }

        m_tokenService.removeTokenByTokenName(userId, token);
        return ResponseEntity.status(HttpStatus.OK).body("<head><meta http-equiv = \"refresh\" content = \"1; url = /\" /></head>Token removed, wait for refresh...");
    }

    @PostMapping("/user/confirmToken")
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity confirmUserToken(Long userId, String user, String deviceCode) throws Exception {

        if (userId == null || user == null || deviceCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameters Empty");
        }
        String accessToken = m_tvTimeService.loadAccessToken(deviceCode);
        m_tokenService.saveToken(new Token(userId, user, accessToken));

        return ResponseEntity.status(HttpStatus.OK).body("<head><meta http-equiv = \"refresh\" content = \"1; url = /\" /></head>Token added, wait for refresh...");
    }


    @PostMapping("/user/requestToken")
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity requestUserToken(Long userId, String user) throws Exception {

        if (userId == null || user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parameters Empty");
        }

        AuthorizationCode authorizationCode = m_tvTimeService.requestAccessToken();
        StringBuilder ret = new StringBuilder();
        ret.append("Linking with your TVShowTime account using the code ").append(authorizationCode.getDevice_code());
        ret.append("<br>Please open the URL ");
        ret.append("<a href=\"").append(authorizationCode.getVerification_url()).append("\">");
        ret.append(authorizationCode.getVerification_url()).append("</a> in your browser");
        ret.append("<br>Connect with your TVShowTime account and type in the following code : ");
        ret.append("<b>").append(authorizationCode.getUser_code()).append("</b>");
        ret.append("<br><br>Waiting for you to type in the code in TVShowTime :-D ...");

        ret.append("<form action=/user/confirmToken method=POST>");
        ret.append("<input type=hidden name=userId value=").append(userId).append(">");
        ret.append("<input type=hidden name=user value=").append(user).append(">");
        ret.append("<input type=hidden name=deviceCode value=").append(authorizationCode.getDevice_code()).append(">");
        ret.append("<input type=submit value=\"Click here when completed in TvTime\">");

        return ResponseEntity.status(HttpStatus.OK).body(ret.toString());
    }

    @GetMapping("/")
    public ResponseEntity getUsers() {

        Map<String, Long> users = null;
        try {
            users = m_plexService.getUsers();
        }
        catch (PlexNotAvailableException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PlexNotAvailable: " + e.getMessage());
        }

        StringBuilder sb = new StringBuilder("<table><tr>" +
                "<th>ID</th>" +
                "<th>Name</th>" +
                "<th>Token TvTime</th>" +
                "<th>Insert Token</th>" +
                "<th>Request new Token</th></tr>");
        users.forEach((key, value) -> {
            Set<Token> tokenByUserId = new HashSet<>();
            try {
                tokenByUserId = m_tokenService.getTokenByUserId(value);
            }
            catch (TokenStoreNotFoundException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            sb.append("<tr>");
            sb.append("<td>").append(value).append("</td>");
            sb.append("<td>").append(key).append("</td>");
            sb.append("<td>");
            tokenByUserId.forEach(y -> {
                sb.append(y.getToken());
                sb.append("<form action=/user/tokenDelete method=POST>");
                sb.append("<input type=hidden name=token value=").append(y.getToken()).append(">");
                sb.append("<input type=hidden name=userId value=").append(value).append(">");
                sb.append("<input type=submit value=\"Remove Token\">");
                sb.append("</form><br>");
            });
            sb.append("</td>");
            sb.append("<td><form action='/user/token' method=POST>");
            sb.append("     <input type=text name=token>");
            sb.append("     <input type=submit>");
            sb.append("     <input type=hidden name=userId value=").append(value).append(">");
            sb.append("     <input type=hidden name=user value=").append(key).append(">");
            sb.append("</form></td>");

            sb.append("<td><form action='/user/requestToken' method=POST>");
            sb.append("     <input type=submit value=requestToken>");
            sb.append("     <input type=hidden name=userId value=").append(value).append(">");
            sb.append("     <input type=hidden name=user value=").append(key).append(">");
            sb.append("</form></td>");

            sb.append("</tr>");
        });
        sb.append("</table>");

        return ResponseEntity.status(HttpStatus.OK).body(sb.toString());
    }

}
