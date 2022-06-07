package com.shiro.cloudshirosso.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {
    // 过期时间5分钟
    private static final long EXPIRE_TIME = 60 * 1000;

    //自己定制密钥
    public static final String SECRET = "SECRET_VALUE";

    //请求头
    public static final String AUTH_HEADER = "X-Authorization-With";

    /**
     * 验证token是否正确
     *
     * @param token 当前用户携带的token
     * @return boolean
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 验证token不需要传入Claim里面的信息
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得token中的自定义信息,一般是获取token的username，无需secret解密也能获得
     *
     * @param token
     * @return
     */
    public static Map<String, Claim> getClaimFiled(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaims();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getClaimFiledUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaims().get("userId").asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成签名,准确地说是生成token
     *
     * @return
     */
    public static String sign(Map<String, String> claim) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //附带username,nickname信息
            JWTCreator.Builder builder = JWT.create();
            claim.forEach(builder::withClaim);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1); //一天过期
            return builder.withExpiresAt(calendar.getTime()).sign(algorithm);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取token的签发时间
     *
     * @param token
     * @return
     */
    public static Date getIssueAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证token是否过期
     *
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * 刷新token的有效期
     *
     * @param token
     * @return
     */
    public static String refreshTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token); //解析token
        Map<String, Claim> claims = jwt.getClaims(); //获取token的参数信息

        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create().withExpiresAt(date);
            claims.forEach((key, value) -> builder.withClaim(key, value.asString()));
            return builder.sign(algorithm);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成16位随机盐,用在密码加密上面
     *
     * @return
     */
    public static String generateSalt() {
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        String hex = secureRandomNumberGenerator.nextBytes(16).toHex();
        return hex;
    }
}
