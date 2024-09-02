package kr.nbc.onemonthintern.data.util

import kr.nbc.onemonthintern.data.model.UserResponse
import kr.nbc.onemonthintern.domain.model.UserEntity

fun UserEntity.toResponse() = UserResponse(
    email = email,
    name = name,
    phoneNumber = phoneNumber
)

fun UserResponse.toEntity() = UserEntity(
    email = email,
    name = name,
    phoneNumber = phoneNumber
)