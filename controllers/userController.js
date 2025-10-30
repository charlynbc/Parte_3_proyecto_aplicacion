const User = require('../models/User');
const jwt = require('jsonwebtoken');

const generateToken = (userId) => {
    return jwt.sign({ userId }, process.env.JWT_SECRET, { expiresIn: '7d' });
};

exports.register = async (req, res) => {
    try {
        const { username, email, password } = req.body;
        
        // Check if user already exists
        const existingUser = await User.findOne({ 
            $or: [{ email }, { username }] 
        });
        
        if (existingUser) {
            return res.status(400).json({ 
                message: 'Usuario ya existe con ese email o username' 
            });
        }

        // Create new user
        const user = new User({ username, email, password });
        await user.save();

        const token = generateToken(user._id);
        
        res.status(201).json({
            message: 'Usuario creado exitosamente',
            token,
            user: {
                id: user._id,
                username: user.username,
                email: user.email,
                role: user.role
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error del servidor', error: error.message });
    }
};

exports.login = async (req, res) => {
    try {
        const { email, password } = req.body;
        
        // Find user and include password for comparison
        const user = await User.findOne({ email }).select('+password');
        
        if (!user || !(await user.comparePassword(password))) {
            return res.status(401).json({ message: 'Credenciales inválidas' });
        }

        if (!user.isActive) {
            return res.status(401).json({ message: 'Cuenta desactivada' });
        }

        const token = generateToken(user._id);
        
        res.json({
            message: 'Login exitoso',
            token,
            user: {
                id: user._id,
                username: user.username,
                email: user.email,
                role: user.role
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error del servidor', error: error.message });
    }
};

exports.getProfile = async (req, res) => {
    try {
        const user = await User.findById(req.user.userId).select('-password');
        res.json(user);
    } catch (error) {
        res.status(500).json({ message: 'Error del servidor', error: error.message });
    }
};

exports.updateProfile = async (req, res) => {
    try {
        const updates = req.body;
        delete updates.password; // Prevent password update through this route
        
        const user = await User.findByIdAndUpdate(
            req.user.userId, 
            updates, 
            { new: true, runValidators: true }
        ).select('-password');
        
        res.json({ message: 'Perfil actualizado', user });
    } catch (error) {
        res.status(500).json({ message: 'Error del servidor', error: error.message });
    }
};
