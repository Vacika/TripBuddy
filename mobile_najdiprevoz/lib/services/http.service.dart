import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
class HttpService {
  final storage = FlutterSecureStorage();
  Future<http.Response> get(String url) async {
    var token = await this.storage.read(key:'token');
    return http.get(url,  headers: {"Authorization": token} );
  }
  Future<http.Response> post(String url, dynamic body) async {
    var token = await this.storage.read(key:'token');
    return http.post(url,  headers: {"Authorization": token}, body:body);
  }

}