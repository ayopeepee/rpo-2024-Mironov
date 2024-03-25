::set ABI=armaebi-v7a
::set ABI=x86
::set ABI=arm64-v8a
set ABI=x86_64

set ANDROID_NDK=C:\Users\topde\AppData\Local\Android\Sdk\ndk\26.2.11394342
set TOOL_CHAIN=%ANDROID_NDK%\build\cmake\android.toolchain.cmake
set CMAKE=C:\Users\topde\AppData\Local\Android\Sdk\cmake\3.22.1\bin\cmake.exe

mkdir %ABI%
cd %ABI%

set PATH=%PATH%;C:\Users\topde\AppData\Local\Android\Sdk\cmake\3.22.1\bin

%CMAKE% ..\..\mbedtls -GNinja -DCMAKE_SYSTEM_NAME=Android -DCMAKE_SYSTEM_VERSION=21 -DANDROID_ABI=%ABI% -DCMAKE_TOOLCHAIN_FILE=%TOOL_CHAIN% -DUSE_SHARED_MBEDTLS_LIBRARY=On -DENABLE_TESTING=Off

%CMAKE% --build .