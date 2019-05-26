const functions = require('firebase-functions');
const admin = require('firebase-admin');
const app = require('./api')

// Initialize Firebase Admin
admin.initializeApp();

exports.api = functions.https.onRequest(app);