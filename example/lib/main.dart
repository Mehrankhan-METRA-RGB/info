import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:info/info.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _system = 'Unknown';
  String _device = 'Unknown';
  String _battery = 'Unknown';
  String _thermal = 'Unknown';
  String _soc = 'Unknown';
  String _sensors = 'Unknown';
  final Info _infoPlugin = Info();

  @override
  void initState() {
    super.initState();
    _init();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  void _init() async {
    String? system;
    String? device;
    String? battery;
    String? thermal;
    String? soc;
    String? sensors;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      system = await _infoPlugin.system() ?? 'Unknown SYSTEM DATA';
      device = await _infoPlugin.device() ?? 'Unknown DEVICE DATA';
      battery = await _infoPlugin.battery() ?? 'Unknown BATTERY DATA';
      thermal = await _infoPlugin.thermal() ?? 'Unknown THERMAL DATA';
      soc = await _infoPlugin.soc() ?? 'Unknown SOC DATA';
      sensors = await _infoPlugin.sensors() ?? 'Unknown SENSORS DATA';
    } on PlatformException {
      system = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _battery = battery!;
      _system = system!;
      _device = device!;
      _thermal = thermal!;
      _sensors = sensors!;
      _soc = soc!;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView(
          children: [
            _tile(title: "SYSTEM INFO", data: _system),
            _tile(title: "DEVICE INFO", data: _device),
            _tile(title: "BATTERY INFO", data: _battery),
            _tile(title: "THERMAL INFO", data: _thermal),
            _tile(title: "SENSORS INFO", data: _sensors),
            _tile(title: "SOC INFO", data: _soc),
          ],
        ),
      ),
    );
  }

  ///DUMMY TILE FOR LISTVIEW
  Widget _tile({required String title, required String data}) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Card(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                title,
                style: Theme.of(context).textTheme.titleLarge,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                data,
                style: Theme.of(context).textTheme.bodyMedium,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
