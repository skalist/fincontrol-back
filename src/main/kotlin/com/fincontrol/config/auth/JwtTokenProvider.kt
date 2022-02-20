package com.fincontrol.config.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${fincontrol.security.jwtSecret}")
    private val jwtSecret: String,
    @Value("\${fincontrol.security.jwtExpirationInMs}")
    private val jwtExpirationInMs: Int,
) {
    fun generateToken(authentication: Authentication): String {
        val principal = authentication.principal as UserPrincipal
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()), SignatureAlgorithm.HS512)
            .setSubject(principal.id)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .compact()
    }

    fun getUserIdFromJWT(token: String): String {
        val claims = Jwts.parser()
            .setSigningKey(jwtSecret.toByteArray())
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret.toByteArray()).parseClaimsJws(authToken)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
