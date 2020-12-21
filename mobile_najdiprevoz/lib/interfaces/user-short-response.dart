import 'package:json_annotation/json_annotation.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'user-short-response.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class UserShortResponse {
  UserShortResponse(this.id, this.name, this.rating, this.profilePhoto);

  int id;
  String name;
  double rating;
  String profilePhoto;

  factory UserShortResponse.fromJson(Map<String, dynamic> json) =>
      _$UserShortResponseFromJson(json);

  Map<String, dynamic> toJson() => _$UserShortResponseToJson(this);
}
