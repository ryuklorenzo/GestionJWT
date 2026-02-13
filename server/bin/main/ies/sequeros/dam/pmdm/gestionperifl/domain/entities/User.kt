package ies.sequeros.dam.pmdm.gestionperifl.domain.entities
import java.util.UUID

enum class UserStatus {
    pending,
    active,
    inactive,
    suspended
}
data class User(
    val id: UUID,
    var name: String,
    val email: String,
    var image: String? = null,
    var status: UserStatus = UserStatus.pending
) {
    fun isCanLogin(): Boolean = (this.status == UserStatus.active || this.status== UserStatus.pending)
    fun activate() {
        this.status = UserStatus.active
    }
    fun suspendAccount() {
        this.status = UserStatus.suspended
    }

    fun updateProfile(newName: String, newImage: String?) {
        if (newName.isNotBlank()) {
            this.name = newName
        }
        this.image = newImage
    }
}