package com.lwq.bilibili.service.util;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lwq.bilibili.domain.exception.ConditionException;
//  1、认证流程
// - 首先，前端通过web表单将自己的用户名和密码发送到后端的接口。这一过程一般是一个HTTP POST请求。建议的方式是通过SSL加密的传输（https协议）,从而避免敏感信息被嗅探。

// - 后端核对用户名和密码成功后，将用户的id等其他信息作为JWT Payload（负载），将其与头部分别进行Base64编号拼接后签名，形成一个JWT，形成的JWT（Token）就是一个形同111.zzz.xxx的字符串
// - token (head.payload.singurater)

// - 后端将JWT字符串作为登录成功的返回结果返回到前端，前端可以将返回的结果保存在localStorage或sessionStorage上，退出登录时前端删除保存的JWT即可

// - 前端在每次请求时将JWT放入HTTp Header中的Authorization位。（解决XSS和XSRF问题）

// - 后端检测是否存在，如存在验证JWT有效性。例如，检查签名是否正确；检查Token是否过期；检查Token的接收方案是否是自己（可选）

// - 验证通过后后端使用JWT中包含的用户信息进行其他逻辑操作，返回相应的结果。

// # 2、JWT优势
// - `简洁（Compact）`: 可以通过URL，POST参数或者在Http header发送，因为数据量少，传输速度也很快
// - `自包含（Self-contained）`: 负载中包含了所有用户所需要的信息，避免了多次查询数据库
// - 因为Token是以JSON加密的形式保存在客户端的，所以JWT是跨语言的，原则上任何web形式都有支持
// - 不需要服务端保存会话信息，特别适用于分布式微服务
public class TokenUtil {
    private static String ISSUER = "bilibili";

    public static String generateToken(Long userId) throws IllegalArgumentException, Exception {
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 30);
        //解释下面的代码
        //withKeyId(String.valueOf(userId))  这个是设置token的keyId，这个keyId可以是用户的id，也可以是用户的账号，这个keyId是唯一的，用来标识这个token的唯一性
        //withIssuer(ISSUER)  这个是设置token的签发者，这个签发者可以是任意字符串，一般是公司的名称
        //withExpiresAt(calendar.getTime())  这个是设置token的过期时间，这个过期时间是一个Date类型，也就是说token会在这个时间过期，过期后token就失效了
        //sign(algorithm)  这个是设置token的签名，这个签名是根据上面的算法生成的，这个算法是RSA256，这个算法是非对称加密算法，这个算法需要一个公钥和一个私钥，
        //这个公钥和私钥是一对的，公钥加密的数据只能私钥解密，私钥加密的数据只能公钥解密，这个算法的好处是，只要私钥不泄露，那么token就是安全的，
        //因为别人不知道私钥，就算知道了token，也无法解密，也就无法篡改token，这样就保证了token的安全性
        return JWT.create()
                .withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withClaim("name", "lwq")
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);

    }

    public static Long vertifyToken(String token) {
        Algorithm algorithm;
        try {
            algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555", "token已过期");
        } catch (Exception e) {
            throw new ConditionException("非法用户token");
        }
    }
}
