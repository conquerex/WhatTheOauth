package what.the.oauth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {

        return CustomUserDetails(
            _id = "this.id!!",
            _username = username,
            _nickname = "this.nickname",
            _password = "this.password",
            _email = "this.email",
            _profileImageUrl = "this.profileImageUrl",
            _thumbnailImageUrl = "this.thumbnailImageUrl",
            _pushNotification = true,
            _authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
        )
    }
}

class CustomUserDetails(
    private val _id: String,
    private val _username: String,
    private val _nickname: String,
    private val _password: String,
    private val _email: String,
    private val _profileImageUrl: String?,
    private val _thumbnailImageUrl: String?,
    private val _pushNotification: Boolean?,
    private val _authorities: MutableList<out GrantedAuthority>,
) : UserDetails {
    val id: String get() = _id
    val nickname: String get() = _nickname
    val email: String get() = _email
    val profileImageUrl: String? get() = _profileImageUrl
    val thumbnailImageUrl: String? get() = _thumbnailImageUrl
    val pushNotification: Boolean get() = _pushNotification ?: true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return _authorities
    }

    override fun getPassword(): String {
        return _password
    }

    override fun getUsername(): String {
        return _username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}