# PocketEncryption
Development source of an app designed to provide offline encryption services for any text, initially, with later possibility to expand into file and folder encryption later.
This project is nearing alpha. Will update readme as I make progress.

The app's backbone is pretty much done now and can almost be used.

# Currently working on:

-Fixing the storage controller, again

-Fixing the cryptography to/from multiple of 16 bytes methods because apparently they don't work, again

-UI

# TODO:

-Input checking

-User warnings for everything that goes wrong (use Toasts? use a dedicated log inside UI? use popups?)

-Use threading to ensure responsiveness of UI


# Done:
-Implemented easy-to-use encryption utilities using modern algorithms

-Encryption key secure storage using the Android Key Store

-Easy-use file r/w utilities
