package com.shiro.cloudshirosso.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.shiro.cloudshirosso.constant.JwtConstant;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {
    // 过期时间30分钟
    private static final long EXPIRE_TIME = 30 * 60 * 1000;

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
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        // 验证token不需要传入Claim里面的信息
        //
        verifier.verify(token);
        return true;
    }

    /**
     * 获得token中的自定义信息,一般是获取token的username，无需secret解密也能获得
     *
     * @param token
     * @return
     */
    public static Map<String, Claim> getClaimFiled(String token) {
        DecodedJWT jwt = parsingToken(token);
        return jwt.getClaims();

    }

    public static String getClaimFiledUserId(String token) {
        DecodedJWT jwt = parsingToken(token);
        return jwt.getClaims().get("userId").asString();

    }

    public static String getClaimFiledUserName(String token) {
        DecodedJWT jwt = parsingToken(token);
        return jwt.getClaims().get("userName").asString();
    }

    /**
     * 生成签名,准确地说是生成token
     */
    public static String sign(Map<String, String> claim) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        //附带username,nickname信息
        JWTCreator.Builder builder = JWT.create();
        claim.forEach(builder::withClaim);
        Calendar calendar = Calendar.getInstance();
        int minute = Calendar.DATE;
        calendar.add(minute, JwtConstant.JWT_MINUTE);
        // jwt的签发者,默认就是系统签发
        builder.withIssuer(JwtConstant.JWT_SYSTEM);
        // 面向的用户
        builder.withSubject(JwtConstant.JWT_USER);
        // 接收jwt的一方
        builder.withAudience(JwtConstant.JWT_PC);
        // 过期时间
        builder.withExpiresAt(calendar.getTime());
        // 在什么时间前,当前jwt不可用
        //builder.withNotBefore(null);
        // 签发时间
        builder.withIssuedAt(new Date());
        // jwt的唯一标识,主要用来作为一次性token,从而回避重放攻击
        builder.withJWTId(NanoIdUtils.randomNanoId());
        return builder.sign(algorithm);
    }

    public static String getJTI(String token) {
        return JWT.decode(token).getClaim(JwtConstant.JWT_ID).asString();
    }

    /**
     * 获取token的签发时间
     *
     * @param token
     * @return
     */
    public static Date getIssueAt(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getIssuedAt();
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
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTCreator.Builder builder = JWT.create().withExpiresAt(date);
        claims.forEach((key, value) -> builder.withClaim(key, value.asString()));
        return builder.sign(algorithm);
    }

    /**
     * 生成16位随机盐,用在密码加密上面
     *
     * @return
     */
    public static String generateSalt() {
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        return secureRandomNumberGenerator.nextBytes(16).toHex();
    }

    /**
     * 解析token
     */

    public static DecodedJWT parsingToken(String token) {
        return JWT.decode(token);
    }

    /**
     * 验证jwt是否为空,是否正确,是否过期
     *
     * @param token 令牌
     */
    public static void validationToken(String token) {
        if (token == null) {
            throw new NullPointerException("jwt不能为空");
        }
        if (!verify(token)) {
            throw new AuthenticationException("当前token验证不通过.");
        }
        // 有点多余了,verify方法可以判断出token是否过期的
        if (isTokenExpired(token)) {
            throw new AuthenticationException("token过期");
        }
    }
}
