package what.the.oauth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod

@Configuration
class AuthConfig {

    @Bean
    fun accessTokenProvider(
        @Value("\${jwt.secret:}") secret: String
    ) = JwtTokenProvider(secret = secret, validateDays = 1)

//    @Bean
//    fun refreshTokenProvider(
//        @Value("\${auth.jwt.refresh.secret}") secret: String,
//        @Value("\${auth.jwt.refresh.expire-days}") expireDays: Long,
//    ) = JwtTokenProvider(secret = secret, validateDays = expireDays)

    @Bean
    fun jwtRequestFilter(
        accessTokenProvider: JwtTokenProvider,
        userDetailsService: CustomUserDetailsService,
    ): JwtRequestFilter {
        return JwtRequestFilter(accessTokenProvider, userDetailsService)
    }

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        val kakao = Oauth2Provider.Kakao.builder().build()
        return InMemoryClientRegistrationRepository(listOf(kakao))
    }
}

sealed class Oauth2Provider {
    object Kakao : Oauth2Provider() {
        override fun builder(): ClientRegistration.Builder {
            return builder("kakao", ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .scope("profile_nickname", "profile_image", "account_email")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .clientId("내 애플리케이션>앱 설정>요약 정보 :: 앱 키 - REST API 키")
                .clientSecret("TODO")
                .userNameAttributeName("id")
                .clientName("Kakao")
        }
    }

    abstract fun builder(): ClientRegistration.Builder

    internal fun builder(registrationId: String, method: ClientAuthenticationMethod): ClientRegistration.Builder {
        return ClientRegistration.withRegistrationId(registrationId)
            .apply {
                clientAuthenticationMethod(method)
                authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                redirectUri(DEFAULT_LOGIN_REDIRECT_URL)
            }
    }

    companion object {
        private const val DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}"
    }
}