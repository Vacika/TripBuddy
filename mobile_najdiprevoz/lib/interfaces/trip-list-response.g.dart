// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'trip-list-response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TripListResponse _$TripListResponseFromJson(Map<String, dynamic> json) {
  return TripListResponse(
    json['id'] as int,
    json['from'] as String,
    json['to'] as String,
    json['availableSeats'] as int,
    json['status'] as String,
    json['totalSeats'] as int,
    json['departureTime'] == null
        ? null
        : DateTime.parse(json['departureTime'] as String),
    json['pricePerHead'] as int,
    json['maxTwoBackSeat'] as bool,
    (json['allowedActions'] as List)?.map((e) => e as String)?.toList(),
    json['driver'] == null
        ? null
        : UserShortResponse.fromJson(json['driver'] as Map<String, dynamic>),
  );
}

Map<String, dynamic> _$TripListResponseToJson(TripListResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'driver': instance.driver,
      'from': instance.from,
      'to': instance.to,
      'availableSeats': instance.availableSeats,
      'status': instance.status,
      'totalSeats': instance.totalSeats,
      'departureTime': instance.departureTime?.toIso8601String(),
      'pricePerHead': instance.pricePerHead,
      'maxTwoBackSeat': instance.maxTwoBackSeat,
      'allowedActions': instance.allowedActions,
    };
