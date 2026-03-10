const axios = require('axios');

async function testApi() {
    try {
        // 1. Login
        console.log("Logging in...");
        const loginRes = await axios.post('http://localhost:8080/api/auth/login', {
            email: 'admin@unilim.fr',
            password: '1234'
        });
        const token = loginRes.data.token;
        console.log("Token obtained: " + token.substring(0, 20) + "...");

        // 2. GET users
        console.log("Fetching users...");
        const getRes = await axios.get('http://localhost:8080/api/app-users', {
            headers: { Authorization: `Bearer ${token}` }
        });
        console.log(`GET users successful, count: ${getRes.data.length}`);

        // 3. POST user
        console.log("Creating user...");
        const postRes = await axios.post('http://localhost:8080/api/app-users', {
            email: 'test' + Date.now() + '@unilim.fr',
            displayName: 'Test User',
            passwordHash: '1234',
            departement: 'INFO',
            role: 'TEACHER',
            status: 'ACTIVE'
        }, {
            headers: { Authorization: `Bearer ${token}` }
        });
        console.log('User created:', postRes.data.id);
    } catch (error) {
        if (error.response) {
            console.error('Error Response:', error.response.status, error.response.data);
        } else {
            console.error('Error:', error.message);
        }
    }
}

testApi();
