package what.the.oauth

import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest


@RestController
class KakaoController {

    @GetMapping("/")
    fun index(): String {
        return "... Sample api ..."
    }

    @GetMapping("/login/getKakaoAuthUrl")
    fun getKakaoAuthUrl(request: HttpServletRequest): String {
        // 1. index 화면에서 버튼을 클릭한 직후
        println(">>>> 1. getKakaoAuthUrl")
        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=내 애플리케이션>앱 설정>요약 정보 :: 앱 키 - REST API 키" +
                "&redirect_uri=내 애플리케이션>제품 설정>카카오 로그인 :: Redirect URI" +
                "&response_type=code"
    }

    @GetMapping("/login/oauth_kakao")
    fun oauthKakao(
        @RequestParam code: String, model: Model
    ): String {
        // 2. Kakao 화면에서 로그인 한 직후
        println(">>>> 2. oauthKakao")
        System.out.println("##### code : $code")
        val access_Token: String? = getAccessToken(code) // 3번 진입점
        println("###access_Token#### : $access_Token")

        val userInfo: KakaoUserInfo? = access_Token?.let { getUserInfo(it) } // 4번 진입점
        println("### Token    : $access_Token")
        println("### email    : " + userInfo?.kakao_account?.email)
        println("### nickname : " + userInfo?.properties?.nickname)

        val gson = Gson()
        model.addAttribute("kakaoInfo", gson.toJson(userInfo))

        return "/home" //본인 원하는 경로 설정

    }

    // 토큰발급
    fun getAccessToken(authorize_code: String): String? {
        // 3. 카카오에서 받아온 코드로 액세스 발급 직전
        println(">>>> 3. 토큰발급")

        val reqURL = "https://kauth.kakao.com/oauth/token"
//        val restTemplate = RestTemplate()
//        val headers = HttpHeaders()

        val param = LinkedMultiValueMap<String, String>()
        param.add("grant_type", "authorization_code")
        param.add("client_id", "내 애플리케이션>앱 설정>요약 정보 :: 앱 키 - REST API 키")
//        param.add("redirect_uri", "http://localhost:8989/")
        param.add("code", authorize_code)
        param.add("client_secret", "키 발급할 때 알려주는 password")

        val req = HttpEntity<MultiValueMap<String, String>>(param, HttpHeaders())
        val res: ResponseEntity<String> = RestTemplate().exchange(
            reqURL,
            HttpMethod.POST,
            req,
            String::class.java
        )

        println(">>> res ::: " + res)
        val resJson = Gson().fromJson(res.body, KakaoAuth::class.java)
        println(">>> token ::: " + resJson.access_token)
        return resJson.access_token
    }


    // 유저 정보 조회
    fun getUserInfo(access_Token: String): KakaoUserInfo {
        // 4. 액세스 토큰으로 유저 정보 획득 직전
        println(">>>> 4. 유저 정보 조회")
        val reqURL = "https://kapi.kakao.com/v2/user/me"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", "bearer " + access_Token)
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

        println(">>>> info res >>>> " + res)

        return Gson().fromJson(res.body, KakaoUserInfo::class.java)
    }
}