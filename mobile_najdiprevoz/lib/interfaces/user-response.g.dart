// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user-response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserResponse _$UserResponseFromJson(Map<String, dynamic> json) {
  return UserResponse(
    json['id'] as int,
    json['firstName'] as String,
    json['lastName'] as String,
    json['profilePhoto'] as String,
    json['username'] as String,
    json['phoneNumber'] as String,
    json['gender'] as String,
    json['birthDate'] == null
        ? null
        : DateTime.parse(json['birthDate'] as String),
    (json['ratings'] as List)
        ?.map((e) =>
            e == null ? null : Rating.fromJson(e as Map<String, dynamic>))
        ?.toList(),
    (json['averageRating'] as num)?.toDouble(),
    json['defaultLanguage'] as String,
  );
}

Map<String, dynamic> _$UserResponseToJson(UserResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'firstName': instance.firstName,
      'lastName': instance.lastName,
      'profilePhoto': instance.profilePhoto,
      'username': instance.username,
      'phoneNumber': instance.phoneNumber,
      'gender': instance.gender,
      'birthDate': instance.birthDate?.toIso8601String(),
      'ratings': instance.ratings,
      'averageRating': instance.averageRating,
      'defaultLanguage': instance.defaultLanguage,
    };
