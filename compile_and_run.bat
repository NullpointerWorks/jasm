@java -jar "jasmc.jar" %1
@set "file=%1"
@set "file=%file:.="^&REM #%
@java -jar "jasm.jar" %file%.bin
@pause
