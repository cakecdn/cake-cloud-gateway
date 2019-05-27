package net.cakecdn.cloud.gateway.all.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.cakecdn.cloud.gateway.all.domain.User;
import net.cakecdn.cloud.gateway.all.domain.dto.JwtUser;
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
            e.printStackTrace();
            claims = null;
        }

        return claims;
    }

    /**
     * 从令牌中获取sub字段
     *
     * @param token 令牌
     * @return 用户名
     */
    public Map<String, Object> getSubFromToken(String token) {
        Map<String, Object> subject = null;

        try {
            Claims claims = getClaimsFromToken(token);
            subject = (Map<String, Object>) claims.get("sub");
        } catch (Exception ignored) {
        }

        return subject;
    }

    /**
     * 生成令牌
     *
     * @param user        用户
     * @param userDetails 用户登录对象
     * @return JWT令牌
     */
    public String generateToken(User user, UserDetails userDetails) {
        Map<String, Object> claimsMap = new HashMap<>(2);
        Map<String, Object> subjectMap = new HashMap<>();
        List<String> authList = new LinkedList<>();

        userDetails.getAuthorities().forEach(a -> authList.add(a.getAuthority()));

        subjectMap.put("uid", user.getId());
        subjectMap.put("username", user.getUsername());
        subjectMap.put("created", new Date());
        subjectMap.put("auth", authList);
        subjectMap.put("disabled", user.isDisabled());
        subjectMap.put("avatar", user.getAvatar());

        claimsMap.put("sub", subjectMap);

        return generateToken(claimsMap);
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
        Map<String, Object> sub = getSubFromToken(token);
        String username = (String) sub.get("username");
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public List<String> getAuthoritiesFromToken(String token) {
        List<String> authorities;

        try {
            Claims claims = getClaimsFromToken(token);
            authorities = ((Map<String, List<String>>) claims.get("sub")).get("auth");
        } catch (Exception e) {
            authorities = null;
        }

        return authorities;
    }
}
