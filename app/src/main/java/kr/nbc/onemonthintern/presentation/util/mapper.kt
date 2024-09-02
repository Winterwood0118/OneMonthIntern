package kr.nbc.onemonthintern.presentation.util

import kr.nbc.onemonthintern.domain.model.UserEntity
import kr.nbc.onemonthintern.presentation.model.UserModel

fun UserModel.toEntity() = UserEntity(
    email = email,
    name = name,
    phoneNumber = phoneNumber
)

fun UserEntity.toModel() = UserModel(
    email = email,
    name = name,
    phoneNumber = phoneNumber
)