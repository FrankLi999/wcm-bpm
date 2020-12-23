import 'package:flutter/material.dart';

class UnknownScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "404!",
        ),
        leading: GestureDetector(
          onTap: () {
            Scaffold.of(context).openDrawer();
          },
          child: Icon(
            Icons.menu, // add custom icons also
          ),
        ),
        // backgroundColor: Colors.blue,
      ),
      body: Center(
        child: Text('404!'),
      ),
    );
  }
}
