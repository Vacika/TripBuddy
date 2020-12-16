// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'jwt-response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

JwtResponse _$JwtResponseFromJson(Map<String, dynamic> json) {
  return JwtResponse(
    json['token'] as String,
    json['user'],
  );
}

Map<String, dynamic> _$JwtResponseToJson(JwtResponse instance) =>
    <String, dynamic>{
      'token': instance.token,
      'user': instance.user,
    };
