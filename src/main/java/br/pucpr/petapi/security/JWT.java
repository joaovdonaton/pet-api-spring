package br.pucpr.petapi.security;

import br.pucpr.petapi.security.dto.UserInfoDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JWT {
    private final SecuritySettings settings;

    public JWT(SecuritySettings settings) {
        this.settings = settings;
    }

    private static final String PREFIX = "Bearer";

    public Authentication extract(HttpServletRequest req){
        final var header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if(header == null || !header.startsWith(PREFIX)) return null;

        final var token = header.replace(PREFIX, "").trim();
        final var claims = Jwts.parserBuilder()
                .setSigningKey(settings.getSecret().getBytes())
                .deserializeJsonWith(new JacksonDeserializer(Map.of("user", UserInfoDTO.class)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        if(!settings.getIssuer().equals(claims.getIssuer())) return null;

        final var user = claims.get("user", UserInfoDTO.class);
        if(user == null) return null;

        final var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_"+r)).toList();

        return UsernamePasswordAuthenticationToken.authenticated(user, user.getId(), authorities);
    }
}
