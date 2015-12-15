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
* In Eclipse : File/Import.../Android/Existing Android Code Into Workspace.
* Select eclipse directory from last distribution
* Finish to import the 5 projects
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
- Gradle Eclipse Plugin

Steps :
- Once git repository cloned, you need to generate pd-for-android eclipse
  project. To do so : 
  - move to pd-for-android folder and run *gradle generateEclipseDependencies*
  - you should have a directory *aarDependencies* containing generated eclipse projects.
- Then you need to generate eclipse projects for ppp, to do so :
  - move to root folder and run *gradle eclipse* command.
  - you should have eclipse files generated in projects folders (.project, .classpath, ...).

- Import all projects in eclipse :
  - **File/Import.../Existing eclipse projects**
  - Select the repository root folder
  - Select all projects except some samples project (you may import some of them now or later)
  - Press **Finish**.
  - You will have some (known) errors at import. To fix them, you need to define Android Target on all projects : Right click on each projects, then **properties/Android** and select your android target (ex: Android 4.4W), press **OK** to finish.
  - All projects should be OK now.

Note : if you want to import samples, you need to generate icons first (see tools/icon/readme.md for instructions)

Note : eclipse project generation have to be done in two phase because
gradle plugin used to convert aar dependencies to eclipse project is not
working well on multiple gradle project layout, it generates dependencies
in each projects (including all samples) and that's not what we want.

### create a new project :

- duplicate a sample project, change its name an copy your patch in assets
- add your project to settings.gradle in PdPartyPublisher project (first line, and dedicated line with the path)
- change app name res/values/strings.xml
- change package name with refactor -> rename
- change name in the manifest

### make a distribution

* have a clean local git repository on develop branch
* update version in main build.gradle for the release
* launch gradle task "release" on sources root (root project PdPartyPublisher)
* verify release is OK
* commit version change, add a tag on that commit and push both.
* create a release in github (from last pushed tag) and attach binary from PdPartyPublisher/build/PdPartyPublisher-vx.y.z.zip
* merge new tag in master branch and push master
* That's it.
