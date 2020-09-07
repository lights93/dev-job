module.exports = {
    // options...
    devServer: {
        host: 'localhost',
        proxy: {
            "/api": {
                target: 'http://localhost:9078/',
                changeOrigin: true
            },
            "/auth": {
                target: 'http://localhost:9078/',
                changeOrigin: true,
                pathRewrite: {'^/auth': ''}
            },
            "/logout": {
                target: 'http://localhost:9078/',
                changeOrigin: true,
            }
        }
    }
}