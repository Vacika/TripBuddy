// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'reservation-request-full-response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ReservationRequestFullResponse _$ReservationRequestFullResponseFromJson(
    Map<String, dynamic> json) {
  return ReservationRequestFullResponse(
    json['id'] as int,
    json['requesterName'] as String,
    json['tripId'] as int,
    (json['allowedActions'] as List)?.map((e) => e as String)?.toList(),
    json['fromLocation'] as String,
    json['toLocation'] as String,
    json['departureTime'] == null
        ? null
        : DateTime.parse(json['departureTime'] as String),
    json['driverName'] as String,
    json['requestStatus'] as String,
    json['additionalDescription'] as String,
    json['requestedSeats'] as int,
    json['tripStatus'] as String,
  );
}

Map<String, dynamic> _$ReservationRequestFullResponseToJson(
        ReservationRequestFullResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'requesterName': instance.requesterName,
      'tripId': instance.tripId,
      'allowedActions': instance.allowedActions,
      'fromLocation': instance.fromLocation,
      'toLocation': instance.toLocation,
      'departureTime': instance.departureTime?.toIso8601String(),
      'driverName': instance.driverName,
      'requestStatus': instance.requestStatus,
      'tripStatus': instance.tripStatus,
      'requestedSeats': instance.requestedSeats,
      'additionalDescription': instance.additionalDescription,
    };
