call "../JDK/bin/javac.exe" -cp .;../../lib/pde.jar;./rsyntaxtextarea-3.0.3-SNAPSHOT.jar CeuFoldPlugin.java
call "../JDK/bin/jar.exe" cvf CeuFoldPlugin.jar CeuFoldParser.class CeuFoldPlugin.class