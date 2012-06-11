 * use android 2.2 or change in pom.xml
 * create a local.properties file with the following content matching your local settings:
   sdk.dir=/path/to/android-sdk
 * Install Maven >= 3.0.3 or use NetBeans
 * download https://github.com/mosabua/maven-android-sdk-deployer and run:
   export ANDROID_HOME=/path/to/android-sdk
   mvn install -P 2.2
 * mvn -DskipTests=true clean install 
   we get tons of warnings - see below [1] and 
   we needed to increase the heap size to 2g (see pom.xml)
   But it successfully stores a jar, dex and apk file under target dir,
   yeah!
 * mvn -Dandroid.device=usb android:deploy
 * mvn -Dandroid.device=usb android:run
   It runs! But when starting ElasticSearch I get classnotFoundExceptions 
   -> we need to fix this in proguard.cfg I guess?
   
   ##
   ERROR/AndroidRuntime(21904): java.lang.ExceptionInInitializerError
   ERROR/AndroidRuntime(21904):   at org.elasticsearch.node.internal.InternalNode.<init>(InternalNode.java:114)
   ERROR/AndroidRuntime(21904):   at org.elasticsearch.node.NodeBuilder.build(NodeBuilder.java:159)
   ERROR/AndroidRuntime(21904):   at de.jetsli.elasticsearch4a.ElasticNode.start(ElasticNode.java:67)

   ##
   ERROR/AndroidRuntime(21904): Caused by: java.lang.NoClassDefFoundError: java.lang.management.ManagementFactory
   ERROR/AndroidRuntime(21904):   at org.elasticsearch.monitor.jvm.JvmInfo.<clinit>(JvmInfo.java:46)

[1]
[INFO] warning: Ignoring InnerClasses attribute for an anonymous inner class
[INFO] (org.elasticsearch.common.joda.time.DateTimeZone$1) that doesn't come with an
[INFO] associated EnclosingMethod attribute. This class was probably produced by a
[INFO] compiler that did not target the modern .class file format. The recommended
[INFO] solution is to recompile the class from source, using an up-to-date compiler
[INFO] and without specifying any "-target" type options. The consequence of ignoring
[INFO] this warning is that reflective operations on this class will incorrectly
[INFO] indicate that it is *not* an inner class.
....