import 'package:json_annotation/json_annotation.dart';

import 'rating.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'user-response.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class UserResponse {
  UserResponse(
      this.id,
      this.firstName,
      this.lastName,
      this.profilePhoto,
      this.username,
      this.phoneNumber,
      this.gender,
      this.birthDate,
      this.ratings,
      this.averageRating,
      this.defaultLanguage);

  int id;
  String firstName;
  String lastName;
  String profilePhoto;
  String username;
  String phoneNumber;
  String gender;
  DateTime birthDate;
  List<Rating> ratings;
  double averageRating;
  String defaultLanguage;

  factory UserResponse.fromJson(Map<String, dynamic> json) =>
      _$UserResponseFromJson(json);

  Map<String, dynamic> toJson() => _$UserResponseToJson(this);
}
