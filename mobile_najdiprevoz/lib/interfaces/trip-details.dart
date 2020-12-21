import 'package:json_annotation/json_annotation.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'trip-details.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class TripDetails {
  TripDetails(this.isPetAllowed,this.isSmokingAllowed,this.hasAirCondition, this.additionalDescription);

  bool isPetAllowed;
  bool isSmokingAllowed;
  bool hasAirCondition;
  String additionalDescription;

  factory TripDetails.fromJson(Map<String, dynamic> json) =>
      _$TripDetailsFromJson(json);

  Map<String, dynamic> toJson() => _$TripDetailsToJson(this);
}
