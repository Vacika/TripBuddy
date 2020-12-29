import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:mobile_najdiprevoz/interfaces/trip-list-response.dart';
import 'package:intl/intl.dart';

class TripCard extends StatelessWidget {
  TripCard({Key key, this.trip}) : super(key: key);

  final TripListResponse trip;

  @override
  Widget build(BuildContext context) {
    return getCard(this.trip);
  }
}

Widget getCard(TripListResponse trip) {
  return Center(
      child: Container(
          padding: EdgeInsetsDirectional.fromSTEB(5, 10, 5, 10),
          child: Card(
              child: Column(
            mainAxisSize: MainAxisSize.max,
            children: <Widget>[
              ListTile(
                  leading: departureTime(trip.departureTime),
                  title: destinationAndPriceWidget(
                      trip.from, trip.to, trip.pricePerHead),
                  subtitle: Row(
                    children: [
                      Expanded(
                          flex: 2,
                          child: Text(
                              trip.availableSeats.toString() + " seats left")),
                      Expanded(child: tripAdditionalDetails(), flex: 2)
                    ],
                    mainAxisSize: MainAxisSize.max,
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  )),
              userBasicInfo(trip),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: <Widget>[
                  OutlineButton(
                    shape: new RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(30.0)),
                    splashColor: Colors.lightBlue,
                    focusColor: Colors.white70,
                    child: const Text(
                      'RESERVE',
                      style: TextStyle(
                          color: Colors.green, fontWeight: FontWeight.bold),
                    ),
                    onPressed: () {
                      /* ... */
                    },
                  ),
                  const SizedBox(width: 8),
                ],
              ),
            ],
          ))));
}

/**
 * Departure time widget with clock icon and formatted departure time
 */
departureTime(DateTime departureTime) {
  DateFormat dateFormat = DateFormat("dd MMM");
  DateFormat timeFormat = DateFormat("HH:mm");
  return Column(
    crossAxisAlignment: CrossAxisAlignment.center,
    mainAxisAlignment: MainAxisAlignment.center,
    children: [
      Icon(Icons.timer),
      Text(dateFormat.format(departureTime)),
      Text(timeFormat.format(departureTime))
    ],
  );
}

Widget circleImage(String image) {
  var convertedImage;
  if (image != null && image != "") {
    convertedImage = Image.memory(base64Decode(image));
  }

  return Container(
      height: 50,
      width: 50,
      child: ClipOval(child: convertedImage),
      decoration:
          new BoxDecoration(color: Colors.blue, shape: BoxShape.circle));
}

Widget userBasicInfo(TripListResponse trip) {
  return Column(
    children: [
      Divider(
        color: Colors.grey,
        height: 30,
        thickness: 0.5,
        indent: 10,
        endIndent: 10,
      ),
      ListTile(
          leading: circleImage(trip.driver.profilePhoto),
          title: Text(trip.driver.name),
          subtitle: userRating(trip.driver.rating, 23)),
    ],
  );
}

// Widget getAvailableSeats(int availableSeats) {
//   List<Icon> seats = [];
//   for (int i = 0; i < availableSeats; i++) {
//     seats.add(Icon(Icons.person, color: Colors.green));
//   }
//   return Container(
//       child: Row(
//     children: seats,
//     mainAxisSize: MainAxisSize.min,
//     crossAxisAlignment: CrossAxisAlignment.center,
//   ));
// }
//
/**
 * Rating container
 */
Widget userRating(double rating, int totalRatings) {
  return Row(
    mainAxisSize: MainAxisSize.min,
    crossAxisAlignment: CrossAxisAlignment.center,
    // mainAxisAlignment: MainAxisAlignment.end,
    children: [
      Container(
          child: Icon(Icons.star_rate, color: Colors.amber),
          margin: EdgeInsetsDirectional.fromSTEB(0, 0, 0, 2)),
      Text(rating.toString() + '/5 - $totalRatings rating(s)')
    ],
  );
}

/**
 * Row with destination on left and price on the right side.
 */
Widget destinationAndPriceWidget(from, to, price) {
  return Row(
    children: [
      Column(
          children: [Text(from), Text(to)],
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start),
      Text("\$" + price.toString(),
          style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold))
    ],
    mainAxisAlignment: MainAxisAlignment.spaceBetween,
    crossAxisAlignment: CrossAxisAlignment.start,
  );
}

/**
 * Row with icons for smoking, pets, airconditioner, maxTwoBackseat
 */
Widget tripAdditionalDetails() {
  return Row(children: [
    Icon(
      Icons.smoke_free,
      // size: 20,
      color: Colors.red,
    ),
    Icon(Icons.pets, color: Colors.red),
    Icon(Icons.ac_unit, color: Colors.red)
  ], mainAxisAlignment: MainAxisAlignment.end, mainAxisSize: MainAxisSize.max);
}
