package com.fengchao.sso.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtHelper {
	// 秘钥
	static final String SECRET = "yearcon";
	// 签名是有谁生成
	static final String ISSUSER = "";
	// 签名的主题
	static final String SUBJECT = "admin";
	// 签名的观众
	static final String AUDIENCE = "";
	
	
	public String createToken(String userId){
		try {
		    Algorithm algorithm = Algorithm.HMAC256(SECRET);
		    Map<String, Object> map = new HashMap<String, Object>();
		    Date nowDate = new Date();
		    // 过期时间：2小时
		    Date expireDate = new Date(); // TimeUnit.getAfterDate(nowDate,0,0,0,2,0,0);
	        map.put("alg", "HS256");
	        map.put("typ", "JWT");
		    String token = JWT.create()
		    	// 设置头部信息 Header
		    	.withHeader(map)
		    	// 设置 载荷 Payload
		    	.withClaim("userId", userId)
		        .withIssuer(ISSUSER)
		        .withSubject(SUBJECT)
		        .withAudience(AUDIENCE)
		        // 生成签名的时间 
		        .withIssuedAt(nowDate)
		        // 签名过期的时间 
		        .withExpiresAt(expireDate)
		        // 签名 Signature
		        .sign(algorithm);
		    return token;
		} catch (JWTCreationException exception){
			exception.printStackTrace();
		}
		return null;
	}
	
	public static Integer verifyTokenAndGetUserId(String token, String key) {
		try {
		    Algorithm algorithm = Algorithm.HMAC512(SECRET);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(ISSUSER)
		        .build();
			DecodedJWT jwt = JWT.decode(token);
//		    DecodedJWT jwt = verifier.verify(token);

		    Map<String, Claim> claims = jwt.getClaims();
		    Claim claim = claims.get(key);
		    return claim.asInt();
		} catch (JWTVerificationException exception){
			exception.printStackTrace();
		}
		
		return 0;
	}

    
    public static void main(String[] args) {
    	Integer uid = 123456;
    	JwtHelper jwtHelper = new JwtHelper();
//        String token = jwtHelper.createToken(uid);
//        System.out.println("签名后: " + token);
        System.out.println("验证后, 获取的用户ID:" + jwtHelper.verifyTokenAndGetUserId("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlck5hbWUiOiJ0ZXN0IiwicmVhbE5hbWUiOiJ0ZXN0IiwiZXhwIjoxNTY1OTQ4ODIyfQ.rUjS4QpE5Qr5cxS1tPLx7BvDvgbw6OPp1wkfewTAJfvJ34d2FQuflVO8ujZ_kwx3Ji32xyBiGfPLQCEkhAsn5w", ""));
        
	}
	
}
