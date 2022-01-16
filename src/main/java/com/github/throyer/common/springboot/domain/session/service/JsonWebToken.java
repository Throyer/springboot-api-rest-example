package com.github.throyer.common.springboot.domain.session.service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.session.model.Authorized;

import io.jsonwebtoken.Jwts;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.util.Arrays.stream;


public class JsonWebToken {

    public static final String ROLES_KEY_ON_JWT = "roles";

    public String encode(User user, LocalDateTime expiration, String secret) {
        var roles = user.getRoles().stream().map(role -> role.getAuthority()).toList();
        return encode(user.getId(), roles, expiration, secret);
    }
    
    public String encode(UserDetails user, LocalDateTime expiration, String secret) {
        return encode(user.getId(), user.getRoles(), expiration, secret);
    }


    public String encode(
        Long id,
        List<String> authorities,
        LocalDateTime expiration,
        String secret
    ) {
        return Jwts.builder()
            .setSubject(id.toString())
            .claim(ROLES_KEY_ON_JWT, authorities
                .stream()                
                .collect(Collectors.joining(",")))
            .setExpiration(Date.from(expiration
                .atZone(ZoneId.systemDefault())
                .toInstant()))
            .signWith(HS256, secret)
            .compact();
    }

    public Authorized decode(String token, String secret) {

        var decoded = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

        var id = Long.parseLong(decoded.getBody().getSubject());
        
        var joinedRolesString = decoded.getBody().get(ROLES_KEY_ON_JWT).toString();
        var roles = joinedRolesString.split(",");
        var authorities = stream(roles).map(Role::new).toList();

        return new Authorized(id, authorities);
    }
}
