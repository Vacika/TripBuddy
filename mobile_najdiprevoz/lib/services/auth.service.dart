import 'dart:convert';
import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart';
import 'package:mobile_najdiprevoz/interfaces/jwt-response.dart';
import 'package:mobile_najdiprevoz/screens/home.screen.dart';
import 'package:mobile_najdiprevoz/screens/login.screen.dart';

class AuthService {
  String url = 'http://10.0.2.2:8080/api/authenticate';
  final storage = FlutterSecureStorage();

  Future<String> login(username, password) async {
    String body = '{"username":"$username", "password":"$password"}';
    Response response = await post(Uri.encodeFull("$url"),
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json"
        },
        body: body);

    if (response.statusCode == 200) {
      var jwtResponse = JwtResponse.fromJson(jsonDecode(response.body));
      await this.storage.deleteAll();
      await this
          .storage
          .write(key: 'token', value: 'Bearer ${jwtResponse.token}');
      await this.storage.write(key: 'user', value: '${jwtResponse.user}');
      var t = await this.storage.read(key: 'token');
      log("THIS TOKEN${t}");
      return jwtResponse.token;
    } else {
      throw Exception(['[Vasko]Error while trying to log in']);
    }
  }

  logout(context) async {
    await this.storage.delete(key: 'token');
    Navigator.pushAndRemoveUntil(
        context,
        MaterialPageRoute(builder: (context) => LoginScreen()),
        (Route<dynamic> route) => false);
  }

  Future<dynamic> getLoggedUser() async {
    var user = await this.storage.read(key: 'user');
    return user;
  }
}
