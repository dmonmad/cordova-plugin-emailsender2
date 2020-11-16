var exec = require('cordova/exec');

/**
 * EmailSender2 class
 * @constructor
 */

exports.sendEmail = function (ip, port, user, password, from, to, subject, body, success, error) {
    exec(success, error, 'EmailSender2', 'sendEmail', [ip, port, user, password, from, to, subject, body]);
};
