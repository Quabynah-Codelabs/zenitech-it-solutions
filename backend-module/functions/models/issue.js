const mongoose = require('mongoose');
const validator = require('validator');

var schema = new mongoose.Schema({
    key: {
        required: true,
        type: String,
        unique: true
    },
    description: String,
    category: String,
    timestamp: {
        type: Number,
        default: new Date().getTime()
    },
    synced: {
        type: Boolean,
        default: false
    }
});

// Export model
module.exports = mongoose.model('Issue', schema);