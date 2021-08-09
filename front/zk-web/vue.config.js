module.exports = {
    devServer: {
        proxy: {
            '/zk': {
                ws: false,
                target: 'http://127.0.0.1:8080',
                changeOrigin: true,
                pathRewrite: {
                    '^/': ''
                }
            }
        }
    }
}