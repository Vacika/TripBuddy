import 'package:json_annotation/json_annotation.dart';

/// This allows the `User` class to access private members in
/// the generated file. The value for this is *.g.dart, where
/// the star denotes the source file name.
part 'jwt-response.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
@JsonSerializable()
class JwtResponse{
  JwtResponse(this.token, this.user);

  String token;
  dynamic user;

  factory JwtResponse.fromJson(Map<String, dynamic> json) => _$JwtResponseFromJson(json);

  Map<String, dynamic> toJson() => _$JwtResponseToJson(this);



}
