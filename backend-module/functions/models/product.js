const mongoose = require('mongoose');
const validator = require('validator');

// Create schema
var productSchema = new mongoose.Schema({
    key: {
        required: true,
        type: String,
        unique: true
    },
    name: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value)
        }
    },
    price: {
        required: true,
        type: Number,
        validate: (value) => {
            return !validator.isEmpty(value) && validator.isNumeric(value);
        }
    },
    category: String,
    
    createdAt: {
        required: false,
        type: Number,
        default: new Date().getTime()
    },
    updatedAt: {
        required: false,
        type: Number,
        default: new Date().getTime()
    }
});

// Export model
module.exports = mongoose.model('Product', productSchema);