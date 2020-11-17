var exec = require('cordova/exec');

/**
 * Sends an email
 *
 * Notice: As many ftp server could not rm dir when it's not empty, so always recommended to `rm` all files under the dir at first before `rmdir`.
 *
 * @param {string} ip IP or hostname of the SMTP server.
 * @param {string} port Port of the SMTP server.
 * @param {string} user User to connect with.
 * @param {string} password Password to connect with.
 * @param {string} from Email from where the email will be sent.
 * @param {string} to Email where the email will be sent.
 * @param {string} subject Subject of the email.
 * @param {string} body Body of the email.
 * @param {string[]} attachments The files that you want to send(from the private folder). e.g. "/storage/emulated/0/Android/data/io.ionic.starter/foo.pdf"..
 * @param {function} successCallback The success callback. If triggered, means success.
 * @param {function} errorCallback The error callback. If triggered, means fail.
 */
exports.sendEmail = function (ip, port, user, password, from, to, subject, body, attachments, success, error) {
    if (attachments === void 0){
        console.log("its not null");
        console.log(attachments);
        console.log(attachments.length);
        exec(success, error, 'EmailSender2', 'sendEmail', [ip, port, user, password, from, to, subject, body, attachments.toString()]);
    }
    else {
        console.log("Its null");
        exec(success, error, 'EmailSender2', 'sendEmail', [ip, port, user, password, from, to, subject, body, ""]);
    }
};
