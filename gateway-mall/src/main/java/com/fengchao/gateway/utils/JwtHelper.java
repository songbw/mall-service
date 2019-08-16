package com.fengchao.gateway.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtHelper {
	// 秘钥
	static final String SECRET = "X-Weesharing-Token";
	// 签名是有谁生成
	static final String ISSUSER = "WEESHARING";
	// 签名的主题
	static final String SUBJECT = "weesharing token";
	// 签名的观众
	static final String AUDIENCE = "APP";
	
	
	public String createToken(Integer userId){
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

	public static String getValue(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getSubject();
	}
	
	public static Integer verifyTokenAndGetUserId(String token, String key) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(SECRET);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(ISSUSER)
		        .build();
		    DecodedJWT jwt = verifier.verify(token);
		    Map<String, Claim> claims = jwt.getClaims();
		    Claim claim = claims.get(key);
		    return claim.asInt();
		} catch (JWTVerificationException exception){
			exception.printStackTrace();
		}
		
		return 0;
	}

    
    public static void main(String[] args) {
    	String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYXZpZCIsInVpZCI6IjI3IiwiZXhwIjoxNTY1ODkxNTMxfQ.VCE9FCbQmqhqEGuA00UbmCp3qe3N-0PNV-FuTKwsx1aGqT97oJPOTlZ49agi5jTSaxWDHiMMZ8y-ooUsnr6aZw";

		DecodedJWT jwt = JWT.decode(token);
		jwt.getSubject();
		System.out.println(jwt.getSubject());
	}
	
}
