# Jetpack Compose BLE Scanner with Room, JUnit 5, and Koin

Scan your BLE devices with my new Jetpack Compose app. BLE scans include:

* The device name, if known
* The MAC address
* A list of advertised services
* Manufacturer specific data, when possible
* and a timestamp of when the device was last seen

Includes partial support for [Microsoft Beacon](https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-cdp/77b446d0-8cea-4821-ad21-fabdf4d9a569).

## Video 1: Prepopulated Room Database, Data Layer Setup, and Room JUnit 5 Tests

In this video, I go over my data layer, which includes a pre-populated Room database. I’ll also 
review my corresponding JUnit 5 tests. This video features:

* Nordic's [Bluetooth Numbers Database](https://github.com/NordicSemiconductor/bluetooth-numbers-database)
* DB Browser for SQLite
* Room Entities, Dao
* Koin Dependency Injection
* JUnit 5 data layer and Room tests