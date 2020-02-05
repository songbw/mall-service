package com.fengchao.sso.util;

import com.fengchao.sso.bean.LoginBean;
import com.fengchao.sso.bean.ThirdLoginBean;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    /**
     * 过期时间 12小时
     */
    public static final int EXPIRATIONTIME = 1000 * 60 * 60 * 12;

    /**
     * JWT 密码
     */
    private static final String SECRET = "yearcon";


    /**
     * HS512算法生成Token
     * @param jwtUserInfo
     *          用户信息
     * @return
     *          生成的Token
     */
    public static String generateToken(ThirdLoginBean jwtUserInfo){
        if(jwtUserInfo == null){
            return null;
        }
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS512");
        return Jwts.builder().setHeader(map)
                .setSubject(jwtUserInfo.getiAppId() + jwtUserInfo.getOpenId())
                .setExpiration(DateTime.now().plusSeconds(EXPIRATIONTIME).toDate())
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    /**
     * HS512算法生成Token
     * @param loginBean
     *          用户信息
     * @return
     *          生成的Token
     */
    public static String generateToken(LoginBean loginBean){
        if(loginBean == null){
            return null;
        }
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS512");
        return Jwts.builder().setHeader(map)
                .setSubject(loginBean.getAppId() + loginBean.getUsername())
                .setExpiration(DateTime.now().plusSeconds(EXPIRATIONTIME).toDate())
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }


    /**
     * HS256解密Token
     * @param token
     *          token字符串
     * @return
     *          返回用户信息
     */
    public static JWTUserInfo getInfoFromToken(String token, String tokenSecret){
        if(token == null){
            return null;
        }
        Claims body = null;
        try {
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenSecret);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
            body = claimsJws.getBody();
        }catch (Exception e){
            return null;
        }
        return new JWTUserInfo(body.getSubject(),
                getObjectValue(body.get("userName")),
                getObjectValue(body.get("realName")));
    }

    private static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }


}