# PdDroidPublisher
Publish signed apk from your PdDroidParty patches

# import project into Eclipse :

requirements:
- Android SDK
- GIT
- Eclipse (Tested with Eclipse Kepler)
- Gradle Eclipse Plugin

Steps :
- Once git repository cloned, you need to init all submodules :
```  
  $ git submodule update --init --recursive
```
- Import all projects in eclipse :
  - **File/Import.../Gradle/Gradle Project**
  - Select the repository root folder and press **Build Model**
  - Select all projects except some samples project (you may import some of them now or later)
  - Press **Finish**.
  - You will have some (known) errors at import. To fix them, you need to define Android Target on all projects : Right click on each projects, then **properties/Android** and select your android target (ex: Android 4.4W), press **OK** to finish.
  - The midi project needs to be cleaned (since bin directory has been unfortunately source controled). Right click on the project and **clean...**
  - All projects should be OK now.

Note : if you want to import samples, you need to generate icons first (see tools/icon/readme.md for instructions)

# create a new project :

- duplicate a sample project, change its name an copy your patch in assets
- add your project to settings.gradle in PdPartyPublisher project (first line, and dedicated line with the path)
- change app name res/values/strings.xml
- change package name with refactor -> rename
- change name in the manifest

