// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user-short-response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserShortResponse _$UserShortResponseFromJson(Map<String, dynamic> json) {
  return UserShortResponse(
    json['id'] as int,
    json['name'] as String,
    (json['rating'] as num)?.toDouble(),
    json['profilePhoto'] as String,
  );
}

Map<String, dynamic> _$UserShortResponseToJson(UserShortResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'rating': instance.rating,
      'profilePhoto': instance.profilePhoto,
    };
