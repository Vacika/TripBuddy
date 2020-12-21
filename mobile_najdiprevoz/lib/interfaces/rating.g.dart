// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'rating.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Rating _$RatingFromJson(Map<String, dynamic> json) {
  return Rating(
    json['id'] as int,
    json['note'] as String,
    json['dateSubmitted'] == null
        ? null
        : DateTime.parse(json['dateSubmitted'] as String),
    json['rating'] as int,
  );
}

Map<String, dynamic> _$RatingToJson(Rating instance) => <String, dynamic>{
      'id': instance.id,
      'note': instance.note,
      'dateSubmitted': instance.dateSubmitted?.toIso8601String(),
      'rating': instance.rating,
    };
