# cordova-plugin-emailsender2
 
## Description

This cordova plugin is created to allow sending emails in web/js.

Supports **Android ONLY** by now.

## Installation

```sh
$ cordova plugin add cordova-plugin-emailsender2
$ cordova prepare
```

## Usage

You can access this plugin by js object `window.cordova.plugin.EmailSender2`.

Example:

```js
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    // First of all, connect to ftp server address without protocol prefix. e.g. "192.168.1.1:21", "ftp.xfally.github.io"
    // Notice: address port is only supported for Android, if not given, default port 21 will be used.
    "ip", "port", "user", "password", "own@exampler.com", "destiny@bar.com", "Test subject!", "It works!", ["/storage/emulated/0/Android/data/io.ionic.starter/foo.pdf", "/storage/emulated/0/Android/data/io.ionic.starter/bar.pdf"]
    window.cordova.plugin.EmailSender2.sendEmail("ip", "port", "user", "password", "own@exampler.com", "destiny@bar.com", "Test subject!", "It works!", ["/storage/emulated/0/Android/data/io.ionic.starter/foo.pdf", "/storage/emulated/0/Android/data/io.ionic.starter/bar.pdf"], function(ok) {
        console.log(ok);
        
        }, function(error) {
            console.error("email couldn't be sent" + error);
        });

    }, function(error) {
        console.error("email couldn't be sent" + error);
    });
}
```

Please refer to [EmailSender2.js](./www/EmailSender2.js) for all available APIs and usages.

## Notice

1. The parameter "attachments" accepts an string array but it can be null if no attachments will be sent with the email.

## Thanks

- The Android native implementing is based on [javax.mail](https://github.com/javaee/javamail) jar (EPL) V2.0 & (GPL) v2 with Classpath Exception.

## License

Apache License 2.0. Refer to [LICENSE.md](./LICENSE.md) for more info.
