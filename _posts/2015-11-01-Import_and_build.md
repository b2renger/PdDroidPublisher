---
layout: post
title:  "How to import the project and build an app"
categories: How to, pd developer
published : true
---

This 'how to' assumes that you have completed the setup of your publishing stack.

If that the case you should have eclipse installed, aswell as the android sdk and the adt plugin for eclipse.

#1- Download last distribution

* Go [here](https://github.com/b2renger/PdDroidPublisher/releases) and dowload the latest release. 
* Extract it to a directory that is convenient for you.


#2- Import the project in eclipse

When starting up eclipse, it should prompt you to select a workspace. It is a good practice to seperate your source from your eclipse workspace. You could porbably create a folder to keep your eclipse workspace, and a folder were you keep the code of the release, and your own code for your applications.

![create a workspace]({{site.baseurl}}/img/import_and_build/workspace_creation.png)

Once your workspace is created go to the menu : *File/Import...*
and select */Android/Existing Android Code Into Workspace.* then click **Next**.


![import android code]({{site.baseurl}}/img/import_and_build/import_android_code.png)

Next to the *Root Directory* field click *Browse* and navigate to the folder containing the last distribution you just downloaded.

Finally click *Finish* to import the 5 projects, do not click *copy project into workspace* check box. As we want to keep our workspace and our code separated.

![import projects]({{site.baseurl}}/img/import_and_build/import_dialog.png)

You may have a few residual errors after importing the project, you can clear them by clicking the menu *Project/Clean* and select *clean all project* and click *ok*.

Now you should have a clean workspace to work on, and you can move forward to create a new application project.

![workspace ready]({{site.baseurl}}/img/import_and_build/eclipse_workspace_ready.png)

#3- Create a new application project

In your file explorer and a good text editor, you can setup a new working folder for you project, by duplicating the example folder and editting it manually :

* change app name field res/values/strings.xml.
* change name in the AndroidManifest.xml.

Then in eclipse, import this new folder, by following again the steps to import a project, only this time you'll only have to select the new project to import. If you cannot select it be sure that any of the names are not used twice if so, change it.

![import new project]({{site.baseurl}}/img/import_and_build/import-new-project.png)

In the package explorer you can now right click on the project and change its package name with the menu *Refactor/Rename*. Eclipse may ask you if you want to update your launch configurations, say **yes**.

You can finally launch the project on you android device by selecting the menu *Run/Run as/android application* - to test that everything is working before starting to code.










