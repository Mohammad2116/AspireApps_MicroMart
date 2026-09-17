package ir.aspireapps.identityservice.mapper;

import ir.aspireapps.common.dto.identify.UserRegisterRequest;
import ir.aspireapps.common.dto.identify.UserResponse;
import ir.aspireapps.identityservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(UserRegisterRequest dto);

    UserResponse toDto(User savedUser);
}
