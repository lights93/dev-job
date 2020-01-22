module.exports = {
    // options...
    devServer: {
        proxy: {
            "/api": {
                target: 'http://localhost:9078/',
                changeOrigin: true
            }
        }
    }
}