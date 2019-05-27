const mongoose = require('mongoose');
const validator = require('validator');

// Create schema
var cartSchema = new mongoose.Schema({
    key: {
        required: true,
        type: String,
        unique: true
    },
    product: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value)
        }
    },
    user: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value)
        }
    },
    createdAt: {
        required: false,
        type: Number,
        default: new Date().getTime()
    }
});

// Export model
module.exports = mongoose.model('Cart', cartSchema);