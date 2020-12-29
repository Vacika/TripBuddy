import 'package:flutter/material.dart';
import 'package:mobile_najdiprevoz/screens/login.screen.dart';
import 'package:mobile_najdiprevoz/screens/trip-listing.screen.dart';
import 'package:mobile_najdiprevoz/services/auth.service.dart';

class HomeScreen extends StatelessWidget {
  HomeScreen({Key key, this.token, this.initialIndex = 0}) : super(key: key);

  final int initialIndex;
  final String token;

  factory HomeScreen.fromBase64(String token) => HomeScreen(token: token);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: DefaultTabController(
        initialIndex: initialIndex,
        length: 3,
        child: Scaffold(
          appBar: AppBar(
            bottom: TabBar(
              tabs: [
                Tab(
                  icon: Icon(Icons.car_rental),
                  text: 'Trips',
                ),
                Tab(
                  icon: Icon(Icons.notification_important),
                  text: 'Notifications',
                ),
                Tab(
                  icon: Icon(Icons.storage),
                  text: 'Requests',
                ),
              ],
            ),
            title: Text('Trip Finder'),
          ),
          body: TabBarView(
            children: [TripListingScreen(), LoginScreen(), LoginScreen()],
          ),
        ),
      ),
    );
  }
}

class LogoutButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return FlatButton(
        child: Text("Logout"),
        onPressed: () async => AuthService().logout(context));
  }
}

// body: Center(
//   child: FutureBuilder(
//       future: HttpService()
//           .get('http://10.0.2.2:8080/api/notifications', context)
//           .catchError((error) => {log("VaskoERROR:$error")}),
//       builder: (context, snapshot) => snapshot.hasData
//           ? Column(
//               children: <Widget>[
//                 LogoutButton(),
//                 Text(snapshot.data.toString(),
//                     style: Theme.of(context).textTheme.display1)
//               ],
//             )
//           : snapshot.hasError
//               ? Text("An error occurred${snapshot.error}")
//               : CircularProgressIndicator()),
// ),
