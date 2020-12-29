import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile_najdiprevoz/screens/home.screen.dart';

List<String> cities = ['Struga', 'Ohrid', 'Strumica', 'Skopje'];

class SearchTripsWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return SearchTripsWidgetState();
  }
}

class SearchTripsWidgetState extends State<SearchTripsWidget> {
  String fromLocation;
  String toLocation;
  DateTime departureDate;
  int requestedSeats;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Search"),
        ),
        body: Center(
            child: Container(
                padding: EdgeInsets.all(20.0),
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      getDropdownButton(
                          fromLocation,
                          (chosenCity) => setState(() {
                                fromLocation = chosenCity;
                              }),
                          'From'),
                      getDropdownButton(
                          toLocation,
                          (chosenCity) => setState(() {
                                toLocation = chosenCity;
                              }),
                          'To'),
                      requestedSeatsInputBox(),
                      dateInputBox(),
                      submitButton(),
                    ]))));
  }

  Widget getDropdownButton(buttonValue, Function onChangeFunc, hint) {
    return Container(
        margin: EdgeInsets.fromLTRB(0, 10, 0, 10),
        padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
        decoration: BoxDecoration(color: Colors.grey[200]),
        child: DropdownButton(
            hint: Text(hint),
            value: buttonValue,
            items: cities
                .map((city) => DropdownMenuItem(
                    child: Text(
                      city,
                      textAlign: TextAlign.center,
                    ),
                    value: city))
                .toList(),
            isExpanded: true,
            onChanged: (chosenCity) => onChangeFunc(chosenCity)));
  }

  Widget submitButton() {
    return TextButton(
      child: const Text(
        'SUBMIT',
        style: TextStyle(fontWeight: FontWeight.bold),
      ),
      onPressed: (() => {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => HomeScreen(initialIndex: 0)))
          }),
    );
  }

  dateInputBox() {
    return Container(
        margin: EdgeInsets.fromLTRB(0, 10, 0, 10),
        padding: EdgeInsets.fromLTRB(5, 0, 20, 0),
        width: double.infinity,
        decoration: BoxDecoration(color: Colors.grey[200]),
        child: FlatButton(
            onPressed: () async {
              _selectDate(context);
            },
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                    departureDate != null
                        ? "${departureDate.toLocal()}".split(' ')[0]
                        : 'Choose a date',
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey[700]
                    ),
                    textAlign: TextAlign.start))));
  }

  _selectDate(BuildContext context) async {
    final DateTime picked = await showDatePicker(
      context: context,
      initialDate: departureDate != null? departureDate : DateTime.now(), // Refer step 1
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(Duration(days: 365)),
    );
    if (picked != null && picked != departureDate)
      setState(() {
        departureDate = picked;
      });
  }

  Widget requestedSeatsInputBox() {
    return Container(
        margin: EdgeInsets.fromLTRB(0, 10, 0, 10),
        padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
        decoration: BoxDecoration(color: Colors.grey[200]),
        child: TextField(
            keyboardType: TextInputType.number,
            decoration: InputDecoration(
                border: InputBorder.none,
                enabledBorder: InputBorder.none,
                disabledBorder: InputBorder.none,
                hintText: "Minimum available seats"),
            inputFormatters: <TextInputFormatter>[
              FilteringTextInputFormatter.digitsOnly
            ]) // Only numbers can be entered
        );
  }
}
