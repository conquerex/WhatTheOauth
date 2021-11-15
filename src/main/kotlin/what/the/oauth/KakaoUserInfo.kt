package what.the.oauth

data class Properties(
    var nickname: String
)

data class Profile(
    var nickname: String
) {
    override fun toString(): String {
        return "Profile(nickname='$nickname')"
    }
}

data class KakaoAccount(
    var profile_nickname_needs_agreement: Boolean,
    var profile: Profile,
    var has_email: Boolean,
    var email_needs_agreement: Boolean,
    var is_email_valid: Boolean,
    var is_email_verified: Boolean,
    var email: String
) {
    override fun toString(): String {
        return """  KakaoAccount(
            |   profile_nickname_needs_agreement=$profile_nickname_needs_agreement, 
            |   profile=$profile, 
            |   has_email=$has_email, 
            |   email_needs_agreement=$email_needs_agreement, 
            |   is_email_valid=$is_email_valid, 
            |   is_email_verified=$is_email_verified, 
            |   email='$email')""".trimMargin()
    }
}

data class KakaoUserInfo(
    var id: Int,
    var connected_at: String,
    var properties: Properties,
    var kakao_account: KakaoAccount
) {
    override fun toString(): String {
        return """KakaoUserInfo(
            |id=$id, 
            |connected_at='$connected_at', 
            |properties=$properties, 
            |kakao_account=$kakao_account)""".trimMargin()
    }
}
