// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'rating-response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RatingResponse _$RatingResponseFromJson(Map<String, dynamic> json) {
  return RatingResponse(
    json['id'] as int,
    json['fromFullName'] as String,
    json['fromProfilePic'] as String,
    json['fromId'] as int,
    json['rating'] as int,
    json['note'] as String,
    json['rideId'] as int,
    json['rideFrom'] as String,
    json['rideTo'] as String,
    json['rideDate'] == null
        ? null
        : DateTime.parse(json['rideDate'] as String),
  );
}

Map<String, dynamic> _$RatingResponseToJson(RatingResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'fromFullName': instance.fromFullName,
      'fromProfilePic': instance.fromProfilePic,
      'fromId': instance.fromId,
      'rating': instance.rating,
      'note': instance.note,
      'rideId': instance.rideId,
      'rideFrom': instance.rideFrom,
      'rideTo': instance.rideTo,
      'rideDate': instance.rideDate?.toIso8601String(),
    };
