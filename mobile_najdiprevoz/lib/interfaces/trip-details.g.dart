// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'trip-details.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TripDetails _$TripDetailsFromJson(Map<String, dynamic> json) {
  return TripDetails(
    json['isPetAllowed'] as bool,
    json['isSmokingAllowed'] as bool,
    json['hasAirCondition'] as bool,
    json['additionalDescription'] as String,
  );
}

Map<String, dynamic> _$TripDetailsToJson(TripDetails instance) =>
    <String, dynamic>{
      'isPetAllowed': instance.isPetAllowed,
      'isSmokingAllowed': instance.isSmokingAllowed,
      'hasAirCondition': instance.hasAirCondition,
      'additionalDescription': instance.additionalDescription,
    };
