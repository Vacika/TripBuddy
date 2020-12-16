import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'package:mobile_najdiprevoz/screens/login.screen.dart';

class HttpService {
  final storage = FlutterSecureStorage();

  Future<http.Response> get(String url, context) async {
    var token = await this.storage.read(key: 'token');
    return http.get(url, headers: {"Authorization": token}).catchError(
        (error) => {log("ERROR:$error")});
  }

  Future<String> read(String url, context) async {
    String token = await this.storage.read(key: 'token');
    return http.read(url, headers: {"Authorization": token}).catchError(
        (error) => {log("ERROR:$error")});
  }

  Future<http.Response> post(String url, dynamic body, context) async {
    var token = await this.storage.read(key: 'token');
    return http.post(url, headers: {"Authorization": token}, body: body);
  }
}
