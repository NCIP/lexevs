@echo off
java -Xmx200m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.lexevs.dao.database.operation.tools.ScriptProducingLauncher %*