package com.project.najdiprevoz.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "password_reset_tokens")
class PasswordResetToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true)
        val id: Long = 0L,

        @Column(name = "token", nullable = false, unique = true)
        var token: String,

        @OneToOne
        @JoinColumn(name = "user_id", unique = true, referencedColumnName = "id", nullable = false)
        var user: User,

        @Column(name = "expiry_date", nullable = false)
        var expiryDate: Date = Calendar.getInstance().time
) {
    fun isExpired(): Boolean {
        return Date().after(expiryDate)
    }

    fun setExpiryDate(minutes: Int) {
        val t = Calendar.getInstance()
        t.add(Calendar.MINUTE, minutes)
        this.expiryDate = t.time
    }
}
