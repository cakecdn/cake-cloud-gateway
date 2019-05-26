package net.cakecdn.cloud.gateway.all.util;

import net.cakecdn.cloud.gateway.all.domain.User;
import net.cakecdn.cloud.gateway.all.domain.dto.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class JwtTokenUtil implements Serializable {

    private final String secret;

    public JwtTokenUtil(@Value("${cake.jwt-hs512-secret:Munich}") String secret) {
        this.secret = secret;
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + 2592000L * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    /**
     * 从令牌中获取数据声明
     * 这里验证秘钥
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }


    /**
     * 生成令牌
     *
     * @param user        用户
     * @param userDetails 用户登录对象
     * @return JWT令牌
     */
    public String generateToken(User user, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        List<String> authList = new LinkedList<>();

        userDetails.getAuthorities().forEach(a -> authList.add(a.getAuthority()));

        claims.put("uid", user.getId());
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());
        claims.put("auth", authList);
        claims.put("disabled", user.isDisabled());
        claims.put("avatar", user.getAvatar());

        return generateToken(claims);
    }


    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;

        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }

        return username;
    }


    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }


    /**
     * 验证令牌
     * 1、是否被篡改
     * 2、是否过期
     * 3、是否匹配
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public List<String> getAuthoritiesFromToken(String token) {
        List<String> authorities;

        try {
            Claims claims = getClaimsFromToken(token);
            authorities = (List<String>) claims.get("auth");
        } catch (Exception e) {
            authorities = null;
        }

        return authorities;
    }
}
