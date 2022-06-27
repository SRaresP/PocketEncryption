# ENCOUNTERED BUGS / POSSIBLE ISSUES

-Crash: happens when creating / editing a file when entering the app the second time after the encryption has already been initialised
    -FIXED: had to synchronize the keystore accessing...so now the multithreading in the generate(...) method doesn't matter and I feel sad
    -console mentioned that only secret keys are accepted when calling the cipher.init() method.
    -key and iv were both null
    -full console:
        E/CRYPTOHANDLER: Only SecretKey is supported
        java.security.InvalidKeyException: Only SecretKey is supported
        at com.android.org.conscrypt.OpenSSLCipher.checkAndSetEncodedKey(OpenSSLCipher.java:473)
        at com.android.org.conscrypt.OpenSSLCipher.engineInit(OpenSSLCipher.java:307)
        at javax.crypto.Cipher.tryTransformWithProvider(Cipher.java:2980)
        at javax.crypto.Cipher.tryCombinations(Cipher.java:2891)
        at javax.crypto.Cipher$SpiAndProviderUpdater.updateAndGetSpiAndProvider(Cipher.java:2796)
        at javax.crypto.Cipher.chooseProvider(Cipher.java:773)
        at javax.crypto.Cipher.init(Cipher.java:1288)
        at javax.crypto.Cipher.init(Cipher.java:1223)
        at com.example.offlinepasswordmanager.cryptography.CryptoHandler.encrypt(CryptoHandler.java:173)
        at com.example.offlinepasswordmanager.storage.EncryptedStorageController.add(EncryptedStorageController.java:117)
        at com.example.offlinepasswordmanager.ui.TextEditActivity.lambda$onCreate$0$com-example-offlinepasswordmanager-ui-TextEditActivity(TextEditActivity.java:52)
        at com.example.offlinepasswordmanager.ui.TextEditActivity$$ExternalSyntheticLambda1.onClick(Unknown Source:13)
        at android.view.View.performClick(View.java:8160)
        at android.widget.TextView.performClick(TextView.java:16222)
        at android.view.View.performClickInternal(View.java:8137)
        at android.view.View.access$3700(View.java:888)
        at android.view.View$PerformClick.run(View.java:30236)
        at android.os.Handler.handleCallback(Handler.java:938)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:246)
        at android.app.ActivityThread.main(ActivityThread.java:8653)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:602)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1130)
        D/AndroidRuntime: Shutting down VM
        E/AndroidRuntime: FATAL EXCEPTION: main
        Process: com.example.offlinepasswordmanager, PID: 5478
        java.lang.NullPointerException: Attempt to get length of null array
        at java.io.FileOutputStream.write(FileOutputStream.java:376)
        at com.example.offlinepasswordmanager.storage.EncryptedStorageController.add(EncryptedStorageController.java:119)
        at com.example.offlinepasswordmanager.ui.TextEditActivity.lambda$onCreate$0$com-example-offlinepasswordmanager-ui-TextEditActivity(TextEditActivity.java:52)
        at com.example.offlinepasswordmanager.ui.TextEditActivity$$ExternalSyntheticLambda1.onClick(Unknown Source:13)
        at android.view.View.performClick(View.java:8160)
        at android.widget.TextView.performClick(TextView.java:16222)
        at android.view.View.performClickInternal(View.java:8137)
        at android.view.View.access$3700(View.java:888)
        at android.view.View$PerformClick.run(View.java:30236)
        at android.os.Handler.handleCallback(Handler.java:938)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:246)
        at android.app.ActivityThread.main(ActivityThread.java:8653)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:602)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1130)
        I/Process: Sending signal. PID: 5478 SIG: 9
        Disconnected from the target VM, address: 'localhost:53455', transport: 'socket'