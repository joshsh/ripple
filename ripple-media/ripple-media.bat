:: Windows launcher script for Ripple (with media extension)

if not "%JAVA_OPTIONS%" == "" goto gotOpts
set JAVA_OPTIONS=-Xms32M -Xmx512M
:gotOpts

java %JAVA_OPTIONS% %JAVA_ARGS% -jar target\ripple-media-*-standalone.jar %1 %2 %3 %4 %5 %6 %7 %8 %9

:: TODO: return Ripple's exit code

