@echo off
cd /d "%~dp0"
javaw --module-path javafx-lib --add-modules javafx.controls,javafx.fxml,javafx.swing --enable-native-access=javafx.graphics -jar PDF-UTILITY-TOOL-1.0-SNAPSHOT.jar
pause