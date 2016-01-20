@echo off
java -Xmx800m -XX:PermSize=256m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexevs.dao.index.operation.tools.OptimizeLuceneIndexLauncher %*