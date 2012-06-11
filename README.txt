 * use android 2.2 or change in pom.xml
 * create a local.properties file with the following content matching your local settings:
   sdk.dir=/path/to/android-sdk
 * Install Maven >= 3.0.3 or use NetBeans
 * download https://github.com/mosabua/maven-android-sdk-deployer and run:
   export ANDROID_HOME=/home/peterk/Programme/android-sdk-linux_x86
   mvn install -P 2.2

 * mvn -DskipTests=true clean install # ERROR
   mvn -Dandroid.device=usb android:deploy
   mvn -Dandroid.device=usb android:run

 we get already an Error while 'install':
[INFO] warning: Ignoring InnerClasses attribute for an anonymous inner class
[INFO] (org.elasticsearch.common.joda.time.DateTimeZone$1) that doesn't come with an
[INFO] associated EnclosingMethod attribute. This class was probably produced by a
[INFO] compiler that did not target the modern .class file format. The recommended
[INFO] solution is to recompile the class from source, using an up-to-date compiler
[INFO] and without specifying any "-target" type options. The consequence of ignoring
[INFO] this warning is that reflective operations on this class will incorrectly
[INFO] indicate that it is *not* an inner class.
[INFO] 
[INFO] UNEXPECTED TOP-LEVEL ERROR:
[INFO] java.lang.OutOfMemoryError: GC overhead limit exceeded
[INFO] 	at com.android.dx.ssa.SsaMethod.getDefinitionForRegister(SsaMethod.java:418)
[INFO] 	at com.android.dx.ssa.back.RegisterAllocator.getDefinitionSpecForSsaReg(RegisterAllocator.java:100)
[INFO] 	at com.android.dx.ssa.back.FirstFitLocalCombiningAllocator.ssaSetToSpecs(FirstFitLocalCombiningAllocator.java:986)
[INFO] 	at com.android.dx.ssa.back.FirstFitLocalCombiningAllocator.fitPlanForRange(FirstFitLocalCombiningAllocator.java:921)
[INFO] 	at com.android.dx.ssa.back.FirstFitLocalCombiningAllocator.findRangeAndAdjust(FirstFitLocalCombiningAllocator.java:822)
[INFO] 	at com.android.dx.ssa.back.FirstFitLocalCombiningAllocator.adjustAndMapSourceRangeRange(FirstFitLocalCombiningAllocator.java:720)
[INFO] 	at com.android.dx.ssa.back.FirstFitLocalCombiningAllocator.handleInvokeRangeInsns(FirstFitLocalCombiningAllocator.java:447)
[INFO] 	at com.android.dx.ssa.back.FirstFitLocalCombiningAllocator.allocateRegisters(FirstFitLocalCombiningAllocator.java:136)
[INFO] 	at com.android.dx.ssa.back.SsaToRop.convert(SsaToRop.java:105)
[INFO] 	at com.android.dx.ssa.back.SsaToRop.convertToRopMethod(SsaToRop.java:70)
[INFO] 	at com.android.dx.ssa.Optimizer.optimize(Optimizer.java:102)
[INFO] 	at com.android.dx.ssa.Optimizer.optimize(Optimizer.java:73)
[INFO] 	at com.android.dx.dex.cf.CfTranslator.processMethods(CfTranslator.java:273)
[INFO] 	at com.android.dx.dex.cf.CfTranslator.translate0(CfTranslator.java:134)
[INFO] 	at com.android.dx.dex.cf.CfTranslator.translate(CfTranslator.java:87)
[INFO] 	at com.android.dx.command.dexer.Main.processClass(Main.java:483)
[INFO] 	at com.android.dx.command.dexer.Main.processFileBytes(Main.java:455)
[INFO] 	at com.android.dx.command.dexer.Main.access$400(Main.java:67)
[INFO] 	at com.android.dx.command.dexer.Main$1.processFileBytes(Main.java:394)
[INFO] 	at com.android.dx.cf.direct.ClassPathOpener.processArchive(ClassPathOpener.java:245)
[INFO] 	at com.android.dx.cf.direct.ClassPathOpener.processOne(ClassPathOpener.java:131)
[INFO] 	at com.android.dx.cf.direct.ClassPathOpener.process(ClassPathOpener.java:109)
[INFO] 	at com.android.dx.command.dexer.Main.processOne(Main.java:418)
[INFO] 	at com.android.dx.command.dexer.Main.processAllFiles(Main.java:329)
[INFO] 	at com.android.dx.command.dexer.Main.run(Main.java:206)
[INFO] 	at com.android.dx.command.dexer.Main.main(Main.java:174)
[INFO] 	at com.android.dx.command.Main.main(Main.java:95)
