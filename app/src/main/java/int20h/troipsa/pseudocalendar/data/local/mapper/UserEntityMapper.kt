package int20h.troipsa.pseudocalendar.data.local.mapper

import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import int20h.troipsa.pseudocalendar.data.network.models.UserApiModel
import int20h.troipsa.pseudocalendar.domain.models.User

object UserEntityMapper {

    fun mapToDomain(userEntity: UserEntity) = User(
        id = userEntity.id,
        name = userEntity.name,
        phoneNumber = userEntity.phoneNumber,
    )

    fun mapToDomain(users: List<UserEntity>) = users.map { user -> mapToDomain(user) }

    fun mapToEntity(user: UserApiModel) = UserEntity(
        id = user.id,
        name = user.name,
        phoneNumber = user.phoneNumber,
    )

    fun mapToEntity(users: List<UserApiModel>) = users.map { user -> mapToEntity(user) }
}