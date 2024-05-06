# Welcome to the JUtilities!
This repository contains the Java utility library of [mhahnFr][1].

## Usage
Use this library by integrating it as a dependency of your project.

To do so, you can:
- Download it from [GitHub Packages][2].
- Download a [release][3].
- Build it yourself.

### GitHub Packages
To use the JUtilities as dependency from GitHub Packages, follow the instructions [here][4].

It can be used as dependency in your Gradle build script as follows:
```groovy
repositories {
    maven {
        url = uri('https://maven.pkg.github.com/mhahnfr/jutilities')
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.token") ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation 'mhahnfr:jutilities:0.1.1'
}
```

### Release
The releases can also be used as dependency in your Gradle build script:
```groovy
repositories {
    ivy {
        url 'https://github.com/'
        patternLayout {
            artifact '/[organisation]/[module]/releases/download/v[revision]/[module]-[revision].jar'
        }
        metadataSources { artifact() }
    }
}

dependencies {
    implementation 'mhahnfr:jutilities:0.1.1'
}
```

### Building from source
To build a `jar` file that can be included in other projects, simply run `./gradlew jar`.

#### Git Submodule
You can add this repository as a `git submodule` to your project.

To use the JUtilities as submodule you can add the following to your `build.gradle`:
```groovy
dependencies {
    implementation project(':JUtilities')
}
```
and add it in your `settings.gradle`:
```groovy
include('JUtilities')
```

### Final notes
This library requires Java in version 19 or higher.

This library is licensed under the terms of the GNU GPL 3.0.

© Copyright 2017 - 2024 [mhahnFr][1]

[1]: https://github.com/mhahnFr
[2]: https://github.com/mhahnFr/JUtilities/packages/2132701
[3]: https://github.com/mhahnFr/JUtilities/releases/latest
[4]: https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package