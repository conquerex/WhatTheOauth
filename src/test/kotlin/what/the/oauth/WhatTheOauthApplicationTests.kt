package what.the.oauth

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

class WhatTheOauthApplicationTests {

    @Test
    fun contextLoads() {
        val reqURL = "https://kapi.kakao.com/v2/user/me"
        val access_Token = "getAccessToken로 받은 토큰"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", "bearer $access_Token")
//        headers.set("KakaoAK", kakaoApiKey);

        val param = LinkedMultiValueMap<String, String>()
//        param.add("property_keys", "[\"id\"]")

        val req = HttpEntity<MultiValueMap<String, String>>(param, headers)
        val res: ResponseEntity<String> = restTemplate.exchange(
            reqURL,
            HttpMethod.POST,
            req,
            String::class.java
        )

        println(">>>> info res >>>> $res")

        val userInfo: KakaoUserInfo = Gson().fromJson(res.body, KakaoUserInfo::class.java)
        println(">>>> userInfo >>>> $userInfo")

    }

}
