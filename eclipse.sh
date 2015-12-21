## Eclipse project generator script

# generate eclipse project for all dependencies

cd pd-for-android
bash ../gradlew generateEclipseDependencies

# fix native library location in pd-core project
# temporary remove arm64-v8a not working libs

cd aarDependencies/org.puredata.android-pd-core-1.0.0-rc3
mv jni/* libs/
rm -rf libs/arm64-v8a
cd ../..

# clean unwanted eclipse files for current dummy project.

rm .project
rm .classpath
rm project.properties

cd ..

# generate eclipse project for ppp modules

bash gradlew eclipse

# After that, you can import all eclipse project in your workspace.