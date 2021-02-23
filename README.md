# android-java-compat
Provides current Java functionality for older Android versions.

## Included in this project
This project includes:
* Implementation of the `Instant`, and `Duration` classes.
* Implementation of several related base classes.

Not all classes are fully implemented as we focused on our own use case first.
If you need anything not yet implemented please open a ticket accordingly.

## Getting started
In order to use this project, it must be added to your top level Android app project.
This is done as git submodule under the corresponding root for `java.*`.
E.g.:
```shell
git submodule add ../android-java-compat.git app/src/main/java/java
```

After adding the submodule no additional steps are needed.

## Copyright
Copyright (c) 2019-2021 nacamar GmbH, Germany. See [MIT License](LICENSE) for details.
