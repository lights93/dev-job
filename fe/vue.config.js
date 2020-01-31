module.exports = {
    // options...
    devServer: {
        host: 'localhost',
        proxy: {
            "/api": {
                target: 'http://localhost:9078/',
                changeOrigin: true
            }
        }
    }
}