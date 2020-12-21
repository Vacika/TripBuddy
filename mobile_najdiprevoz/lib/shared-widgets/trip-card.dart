import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:mobile_najdiprevoz/interfaces/trip-list-response.dart';

class TripCard extends StatelessWidget {
  TripCard(this.trip);

  final TripListResponse trip;

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return getCard(this.trip)
  }
}

Widget getCard(TripListResponse trip) {
  return Container(
    child: Card(
      elevation: 0.8,
      color: Colors.grey,
    ),
  );
}
