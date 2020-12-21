import 'package:json_annotation/json_annotation.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'rating-response.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class RatingResponse {
  RatingResponse(
      this.id,
      this.fromFullName,
      this.fromProfilePic,
      this.fromId,
      this.rating,
      this.note,
      this.rideId,
      this.rideFrom,
      this.rideTo,
      this.rideDate);

  int id;
  String fromFullName;
  String fromProfilePic;
  int fromId;
  int rating;
  String note;
  int rideId;
  String rideFrom;
  String rideTo;
  DateTime rideDate;

  factory RatingResponse.fromJson(Map<String, dynamic> json) =>
      _$RatingResponseFromJson(json);

  Map<String, dynamic> toJson() => _$RatingResponseToJson(this);
}
