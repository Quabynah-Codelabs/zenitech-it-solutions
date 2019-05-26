const mongoose = require('mongoose');
const validator = require('validator');

// Create schema
var userSchema = new mongoose.Schema({
    key: {
        required: true,
        type: String,
        unique: true
    },
    email: {
        required: true,
        type: String,
        unique: true,
        lowercase: true,
        validate: (value) => {
            return !validator.isEmpty(value) && validator.isEmail(value)
        }
    },
    password: {
        required: true,
        type: String
    },
    salt: String,
    name: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value)
        }
    },
    avatar: String,
    type: String,
    token: {
        required: false,
        type: String,
        default: null
    },
    createdAt: Number
});

// Export model
module.exports = mongoose.model('Customer', userSchema);