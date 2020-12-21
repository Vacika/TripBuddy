import 'package:json_annotation/json_annotation.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'reservation-request-full-response.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class ReservationRequestFullResponse {
  ReservationRequestFullResponse(
      this.id,
      this.requesterName,
      this.tripId, this.allowedActions,
      this.fromLocation,
      this.toLocation,
      this.departureTime,
      this.driverName,
      this.requestStatus,
      this.additionalDescription,
      this.requestedSeats,
      this.tripStatus);

  int id;
  String requesterName;
  int tripId;
  List<String> allowedActions;
  String fromLocation;
  String toLocation;
  DateTime departureTime;
  String driverName;
  String requestStatus;
  String tripStatus;
  int requestedSeats;
  String additionalDescription;

  factory ReservationRequestFullResponse.fromJson(Map<String, dynamic> json) =>
      _$ReservationRequestFullResponseFromJson(json);

  Map<String, dynamic> toJson() => _$ReservationRequestFullResponseToJson(this);
}
