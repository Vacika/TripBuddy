import 'package:json_annotation/json_annotation.dart';

import 'user-short-response.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'trip-list-response.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class TripListResponse {
  TripListResponse(this.id, this.from, this.to, this.availableSeats,
      this.status, this.totalSeats, this.departureTime, this.pricePerHead, this.maxTwoBackSeat, this.allowedActions, this.driver);

  int id;
  UserShortResponse driver;
  String from;
  String to;
  int availableSeats;
  String status;
  int totalSeats;
  DateTime departureTime;
  int pricePerHead;
  bool maxTwoBackSeat;
  List<String> allowedActions;

  factory TripListResponse.fromJson(Map<String, dynamic> json) =>
      _$TripListResponseFromJson(json);

  Map<String, dynamic> toJson() => _$TripListResponseToJson(this);
}
