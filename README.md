# Welcome to the JUtilities!
This repository contains the Java utility library of [mhahnFr][1].

## Usage
To build a `jar` file that can be included in other projects,
simply run `./gradlew build` (on Windows, use `gradlew.bat build`).

It is recommended to add this repository as a `git submodule`
to your project.  
To use this library as submodule, simply add it to your `build.gradle`
dependencies:
```groovy
dependencies {
    implementation project(':JUtilities')
}
```
and add it to your `settings.gradle`:
```groovy
include('JUtilities')
```

### Final notes
This library requires Java in version 19 or higher.

This library is licensed under the terms of the GPL 3.0.

© Copyright 2017 - 2024 [mhahnFr][1]

[1]: https://github.com/mhahnFr