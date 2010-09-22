@echo off
java -Xmx800m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.lexevs.dao.index.operation.tools.OptimizeLuceneIndexLauncher %*