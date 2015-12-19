# PdDroidPublisher
Publish signed apk from your PdDroidParty patches

## How to use

requirements:
- Android ADK
- Eclipse (Tested with Eclipse Kepler)
- Android for eclipse (ADT)

Steps :
* download last distribution
* unzip it
* In Eclipse : File/Import.../General/Existing Projects Into Workspace.
* Select eclipse directory from last distribution
* Finish to import the 7 projects
* Edit Project named Example. This is a simple demo project you can start on :
  - change app name res/values/strings.xml
  - change package name with refactor -> rename
  - change name in the manifest
* Launch Example project as android application.
* Happy patching !

## How to contribute

### import project into Eclipse :

requirements:
- Android SDK
- GIT
- Eclipse (Tested with Eclipse Kepler)
- Android for eclipse (ADT)

Steps :
- Once git repository cloned, you have to generate eclipse projects. To do so,
  run eclipse.sh script.
- Import all projects in eclipse :
  - File/Import.../General/Existing Projects Into Workspace.
  - Select the repository root folder
  - Select all projects except some samples project (you may import some of them now or later)
  - Press **Finish**.
  - You will have some (known) errors at import. To fix them, you need to define Android Target on all projects : Right click on each projects, then **properties/Android** and select your android target (ex: Android 4.4W), press **OK** to finish.
  - All projects should be OK now.

### create a new project :

- duplicate a sample project, change its name an copy your patch in assets
- add your project to settings.gradle in PdPartyPublisher project (first line, and dedicated line with the path)
- then generate eclipse project by running this command in your sample directory :
  - ../../gradlew eclipse
  - then import eclipse project as usual.
- adapt the sample with specific
- change app name res/values/strings.xml
- change package name with refactor -> rename
- change name in the manifest

### make a distribution

* clone release branch (develop for instance) :
  * git clone -b develop git@github.com:b2renger/PdDroidPublisher.git
* run eclipse.sh to generate eclipse project files (required for release)
* update version in main build.gradle for the release
* launch gradle task "release" on sources root (root project PdPartyPublisher)
* verify release is OK
* commit version change, add a tag on that commit and push both.
* create a release in github (from last pushed tag) and attach binary from PdPartyPublisher/build/PdPartyPublisher-vx.y.z.zip
* merge new tag in master branch and push master
* That's it.
